package database;

import actions.Commander;
import actor.Actor;
import actor.ActorsAwards;


import video.Movie;
import users.PremiumUser;
import video.Show;
import users.StandardUser;
import users.User;
import video.Video;
import fileio.ActionInputData;
import fileio.ActorInputData;
import fileio.MovieInputData;
import fileio.SerialInputData;
import fileio.UserInputData;
import org.json.simple.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Database {
    /**
     * instance of a database
     */
    private static Database database = null;
    /**
     * list of all actors in database
     */
    private final List<Actor> actorsList = new ArrayList<>();
    /**
     * list of all movies in database
     */
    private final List<Movie> moviesList = new ArrayList<>();
    /**
     * list of all shows in database
     */
    private final List<Show> showsList = new ArrayList<>();
    /**
     * list of all users in database
     */
    private final List<User> usersList = new ArrayList<>();
    /**
     * list of all videos in database
     */
    private final List<Video> videosList = new ArrayList<>();
    /**
     * commander that gets the actions and executes them
     */
    private final Commander controlPanel = new Commander();
    /**
     * map of genres and most viewed by people
     */
    private final Map<String, Integer> genreMap = new HashMap<>();

    private Database() {
    }

    /**
     * @return gets the instance of Database
     */
    public static Database getInstance() {
        if (database == null) {
            database = new Database();
        }

        return database;
    }

    /**
     * inserts all the actors in the database
     * @param inputActorsList input list of actors
     */
    public void addActors(final List<ActorInputData> inputActorsList) {
        inputActorsList.forEach(
                actor -> {
                    Actor newActor =
                            new Actor(
                                    actor.getName(),
                                    actor.getCareerDescription(),
                                    actor.getFilmography(),
                                    (HashMap<ActorsAwards, Integer>) actor.getAwards());
                    database.actorsList.add(newActor);
                });
    }

    /**
     * inserts all the movies in the database
     * @param inputMoviesList input list of movies
     */
    public void addMovies(final List<MovieInputData> inputMoviesList) {
        inputMoviesList.forEach(
                movie -> {
                    Movie newMovie =
                            new Movie(
                                    movie.getTitle(),
                                    movie.getCast(),
                                    movie.getGenres(),
                                    movie.getYear(),
                                    movie.getDuration());
                    database.moviesList.add(newMovie);
                });
    }

    /**
     * inserts all the shows in the database
     * @param inputShowsList input list of shows
     */
    public void addShows(final List<SerialInputData> inputShowsList) {
        inputShowsList.forEach(
                show -> {
                    Show newShow =
                            new Show(
                                    show.getTitle(),
                                    show.getCast(),
                                    show.getGenres(),
                                    show.getYear(),
                                    show.getNumberSeason(),
                                    show.getSeasons());
                    database.showsList.add(newShow);
                });
    }

    /**
     * inserts all the users in the database
     * @param inputUsersList input list of users
     */
    public void addUsers(final List<UserInputData> inputUsersList) {
        inputUsersList.forEach(
                user -> {
                    User newUser;
                    if (user.getSubscriptionType().equals("BASIC")) {
                        newUser =
                                new StandardUser(
                                        user.getUsername(),
                                        user.getSubscriptionType(),
                                        user.getHistory(),
                                        user.getFavoriteMovies());
                    } else {
                        newUser =
                                new PremiumUser(
                                        user.getUsername(),
                                        user.getSubscriptionType(),
                                        user.getHistory(),
                                        user.getFavoriteMovies());
                    }

                    database.usersList.add(newUser);
                });
    }

    /**
     * adds movies and shows to a list of videos in the database
     */
    public void addVideos() {
        database.videosList.addAll(this.moviesList);
        database.videosList.addAll(this.showsList);
    }

    /**
     * adds actions to the commander and then will be executed
     * @param inputActions list of actions
     * @return an Array of JSONObjects with messages for the output
     */
    public JSONArray addCommands(final List<ActionInputData> inputActions) {
        controlPanel.addActions(inputActions);
        return controlPanel.execute();
    }

    public List<User> getUsersList() {
        return usersList;
    }

    public List<Video> getVideosList() {
        return videosList;
    }

    public List<Actor> getActorsList() {
        return actorsList;
    }

    public List<Movie> getMoviesList() {
        return moviesList;
    }

    public List<Show> getShowsList() {
        return showsList;
    }

    public Map<String, Integer> getGenreMap() {
        return genreMap;
    }

    /**
     * clear the database
     */
    public void clear() {
        usersList.clear();
        moviesList.clear();
        actorsList.clear();
        showsList.clear();
        videosList.clear();
        controlPanel.clear();
    }
}
