package users;

import video.Movie;
import video.Show;
import video.Video;
import database.Database;
import entertainment.Season;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class User {
    /**
     * User's username
     */
    private final String username;
    /**
     * Type of subscription of a user
     */
    private final String subscriptionType;
    /**
     * The history of videos seen by a user
     */
    private final Map<String, Integer> history;
    /**
     * Movies added to favourite by a user
     */
    private final ArrayList<String> favouriteVideos;
    /**
     * Total no. of ratings given by a user to all videos
     */
    private int noRatingsGiven;

    /**
     * constructor to initialize the fields of an object
     * of a Class which extends User
     * @param username username
     * @param subscriptionType subscriptionType
     * @param history history of videos seen
     * @param favoriteMovies list of favourites movies
     */
    public User(final String username,
                final String subscriptionType,
                final Map<String, Integer> history,
                final ArrayList<String> favoriteMovies) {
        this.username = username;
        this.subscriptionType = subscriptionType;
        this.history = new HashMap<>();
        // adds all the videos from the history to the list of seen videos
        history.forEach((key, val) -> {
            for (int i = 0; i < val; i++) {
                addToViewed(key);
            }
        });
        this.favouriteVideos = new ArrayList<>();
        // adds all the videos which are user's favourite to the list of favourites
        for (String fav : favoriteMovies) {
            addToFavourites(fav);
        }
        this.noRatingsGiven = 0;
    }

    /**
     * @return number of ratings given by a user
     */
    public int getNoRatingsGiven() {
        return noRatingsGiven;
    }

    /**
     * @return number of favourites videos of a user
     */
    public ArrayList<String> getFavouriteVideos() {
        return favouriteVideos;
    }

    /**
     * @return history of videos seen of a user
     */
    public Map<String, Integer> getHistory() {
        return history;
    }

    /**
     * @return the username of the user
     */
    public String getUsername() {
        return username;
    }

    /**
     * increments the number of ratings given by a user
     */
    public void incNoRatingsGiven() {
        this.noRatingsGiven++;
    }

    /**
     * get the most popular unseen video and recommends it
     * to the user
     * @return a JSONObject that contains the message got
     *         after the execution of the program
     */
    public abstract String popularRecommendation();

    /**
     * get the most favourited unseen video from the platform
     * and recommends it to the user
     * @return a String that is the message for a JSONObject
     *         which contains the output after the execution of a command
     */
    public abstract String favouriteRecommendation();

    /**
     * get unseen videos from the platform that are of
     * a specified genre
     * @param genre genre to search unseen videos
     * @return a String that is the message for a JSONObject
     *         which contains the output after the execution of a command
     */
    public abstract String searchRecommendation(String genre);

    /**
     * add a movie in the list of favourites videos
     * @param title the title of the video that wants to be added
     * @return a String that is the message for a JSONObject
     *         which contains the output after the execution of a command
     */
    public String addToFavourites(final String title) {
        // checks if there are any videos with the specified title
        if (getHistory().containsKey(title)) {
            Video video =
                    // gets the list of videos from the DB
                    Database.getInstance().getVideosList().stream()
                            // get it to a stream and filters it
                            .filter(foundVideo -> foundVideo.getTitle().equals(title))
                            // searches for a video that matches the filters
                            .findAny()
                            .orElse(null);

            assert video != null;

            for (String faveVideo : getFavouriteVideos()) {
                if (faveVideo.equals(title)) {
                    return "error -> " + title + " is already in favourite list";
                }
            }

            // adds to the list of favourites video
            getFavouriteVideos().add(title);
            video.increaseFavCount();

            return "success -> " + title + " was added as favourite";
        }
        return "error -> " + title + " is not seen";
    }

    /**
     * add a video into the viewed list of a user
     * @param title the title of the video which wants to be added
     * @return a String that is the message for a JSONObject
     *         which contains the output after the execution of a command
     */
    public String addToViewed(final String title) {
        // checks if there are any videos with the specified title in user's history
        if (getHistory().containsKey(title)) {
            getHistory().replace(title, getHistory().get(title) + 1);
        } else {
            getHistory().put(title, 1);
        }
        Video video =
                // gets the list of videos from the DB
                Database.getInstance().getVideosList().stream()
                        // tranforms it to a stream and filters
                        .filter(foundVideo -> foundVideo.getTitle().equals(title))
                        // searches for any apparition of that fitlered video
                        .findAny()
                        .orElse(null);

        if (video != null) {
            video.increaseViewCount();
        }

        // adds the video in the genre maps so that the platform could know most liked genres
        Map<String, Integer> genreMap = Database.getInstance().getGenreMap();
        assert video != null;
        for (String genre : video.getGenres()) {
            if (genreMap.containsKey(genre)) {
                genreMap.replace(genre, genreMap.get(genre) + 1);
            } else {
                genreMap.put(genre, 1);
            }
        }
        return "success -> " + title + " was viewed with total views of " + getHistory().get(title);
    }

    /**
     * add rating for a specified movie by a user
     * @param title the name of the specified video
     * @param grade the rating grade
     * @return a String that is the message for a JSONObject
     *         which contains the output after the execution of a command
     */
    public String addRating(final String title, final Double grade) {
        // checks if there is any movie with that title
        if (getHistory().containsKey(title)) {
            Movie movie =
                    // gets the list of movies from the DB
                    Database.getInstance().getMoviesList().stream()
                            // tranforms it into a stream and filter it
                            .filter(foundMovie -> foundMovie.getTitle().equals(title))
                            // gets first movie that matches
                            .findAny()
                            .orElse(null);

            if (movie == null) {
                return "error -> " + title + " is not seen";
            }

            if (movie.getRatings().containsKey(this)) {
                return "error -> " + title + " has been already rated";
            }

            movie.getRatings().put(this, grade);
            incNoRatingsGiven();
            return "success -> " + title + " was rated with " + grade + " by " + this.getUsername();
        }
        return "error -> " + title + " is not seen";
    }

    /**
     * add rating for a specified show by a user
     * @param title the name of the specified TV Show
     * @param grade the rating grade for the TV Show
     * @param season the season that wants to be rated
     * @return
     */
    public String addRating(final String title, final Double grade, final int season) {
        // checks if there is any show with that title
        if (getHistory().containsKey(title)) {
            Show show =
                    // gets the list of shows
                    Database.getInstance().getShowsList().stream()
                            // transforms it into a stream and filters it
                            .filter(foundShow -> foundShow.getTitle().equals(title))
                            // gets the first match
                            .findAny()
                            .orElse(null);

            assert show != null;

            // get through every season that is wanted to be rated
            for (Season currSeason : show.getSeasons()) {
                if (currSeason.getCurrentSeason() == season) {
                    if (currSeason.getRatings().containsKey(this)) {
                        return "error -> " + title + " has been already rated";
                    }

                    currSeason.getRatings().put(this, grade);
                    incNoRatingsGiven();
                    return "success -> " + title + " was rated with "
                            + grade + " by " + this.getUsername();
                }
            }
        }
        return "error -> " + title + " is not seen";
    }
}
