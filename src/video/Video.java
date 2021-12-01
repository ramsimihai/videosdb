package video;

import database.Database;
import users.User;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class Video {
    private String title;
    private int year;
    private ArrayList<String> cast;
    private ArrayList<String> genres;
    private int favCount;
    private int viewCount;
    private HashMap<User, Double> ratings;
    private int numberOfSeasons;

    public Video() {
    }

    /**
     * constructor to initialize the fields of an object
     * of a Class which extends Video
     * @param title the name of a video
     * @param cast the cast of a vide
     * @param genres all the genres of a video
     * @param year the year when the video has been produced
     */
    public Video(final String title, final ArrayList<String> cast,
                 final ArrayList<String> genres, final int year) {
        this.title = title;
        this.year = year;
        this.cast = cast;
        this.genres = genres;
        this.favCount = 0;
        this.viewCount = 0;
        this.ratings = new HashMap<>();
        this.numberOfSeasons = 0;
    }

    /**
     * getter of ratings
     * @return
     */
    public final HashMap<User, Double> getRatings() {
        return ratings;
    }

    /**
     * getter of counter of users that favourite a video
     * @return
     */
    public final int getFavCount() {
        return favCount;
    }

    /**
     * getter of counter of users that viewed a video
     * @return
     */
    public final int getViewCount() {
        return viewCount;
    }

    /**
     * increase the counter of users that favourite a video
     */
    public final void increaseFavCount() {
        this.favCount++;
    }

    /**
     * increase the counter of users that viewed a video
     */
    public final void increaseViewCount() {
        this.viewCount++;
    }

    /**
     * getter of year when the movie was produced
     */
    public final int getYear() {
        return year;
    }

    /**
     * get the cast that plays in a video
     */
    public final ArrayList<String> getCast() {
        return cast;
    }

    public final void setNumberOfSeasons(int numberOfSeasons) {
        this.numberOfSeasons = numberOfSeasons;
    }

    /**
     * get the title of a video
     */
    public final String getTitle() {
        return title;
    }

    /**
     * get the genres of a video
     */
    public final ArrayList<String> getGenres() {
        return genres;
    }

    public abstract double getRating();

    public abstract int getDuration();

    /**
     * get the number of total views for the most viewed genre
     * @return
     */
    public final int getPopularGenreViews() {
        int views = 0;
        for (String genre : genres) {
            if (Database.getInstance().getGenreMap().containsKey(genre)
                    && Database.getInstance().getGenreMap().get(genre) > views) {
                views = Database.getInstance().getGenreMap().get(genre);
            }
        }
        return views;
    }
}
