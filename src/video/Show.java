package video;

import entertainment.Season;

import java.util.ArrayList;

public class Show extends Video {
    private ArrayList<Season> seasons;

    public Show(
            final String title,
            final ArrayList<String> cast,
            final ArrayList<String> genres,
            final int year,
            final int numberOfSeasons,
            final ArrayList<Season> seasons) {
        super(title, cast, genres, year);
        super.setNumberOfSeasons(numberOfSeasons);
        this.seasons = seasons;
    }

    public final int getDuration() {
        int duration = 0;
        for (Season season : seasons) {
            duration += season.getDuration();
        }

        return duration;
    }

    public final ArrayList<Season> getSeasons() {
        return seasons;
    }

    public final void setSeasons(final ArrayList<Season> seasons) {
        this.seasons = seasons;
    }

    public final double getRating() {
        double sum = 0.0;
        for (Season season : seasons) {
            if (season.getRatings().isEmpty()) {
                continue;
            }

            double currSum = 0.0;
            for (Double rating : season.getRatings().values()) {
                currSum += rating;
            }

            sum += currSum / season.getRatings().size();
        }

        if (sum != 0.0) {
            return sum / seasons.size();
        }

        return 0.0;
    }
}
