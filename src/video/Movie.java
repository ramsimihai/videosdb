package video;

import java.util.ArrayList;

public class Movie extends Video {
    private int duration;

    public Movie() {
    }

    public Movie(
            final String title, final ArrayList<String> cast, final ArrayList<String> genres,
            final int year, final int duration) {
        super(title, cast, genres, year);
        this.duration = duration;
    }

    public final int getDuration() {
        return duration;
    }

    public final void setDuration(final int duration) {
        this.duration = duration;
    }

    public final double getRating() {
        double sum = 0.0;
        int noMovies = 0;

        for (Double rating : super.getRatings().values()) {
            if (rating != 0) {
                sum += rating;
                noMovies++;
            }
        }

        if (sum != 0.0) {
            return sum / noMovies;
        }

        return 0.0;
    }
}
