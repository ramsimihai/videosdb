package users;

import video.Video;
import database.Database;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class PremiumUser extends User {
    /**
     * constructor to instantiate new Premium User object
     * @param username username
     * @param subscriptionType subscriptionType
     * @param history history of videos seen
     * @param favoriteMovies list of favourites movies
     */
    public PremiumUser(
            final String username,
            final String subscriptionType,
            final Map<String, Integer> history,
            final ArrayList<String> favoriteMovies) {
        super(username, subscriptionType, history, favoriteMovies);
    }

    @Override
    public final String popularRecommendation() {
        List<Video> popularVideos =
                // gets the list of unseen videos
                Database.getInstance().getVideosList().stream()
                        // filters it by appareance in the history of the user
                        .filter(video -> !this.getHistory().containsKey(video.getTitle()))
                        .toList();
        if (popularVideos.isEmpty()) {
            return "PopularRecommendation cannot be applied!";
        }

        // iterates through the list of popular videos by total views
        // and matches after a genre
        Video mostPopular = popularVideos.get(0);
        for (Video video : popularVideos) {
            if (video.getPopularGenreViews() > mostPopular.getPopularGenreViews()) {
                mostPopular = video;
            }
        }

        return "PopularRecommendation result: " + mostPopular.getTitle();
    }

    @Override
    public final String favouriteRecommendation() {
        List<Video> unseenVideos =
                // gets the list of unseen videos
                Database.getInstance().getVideosList().stream()
                        .filter(video -> !this.getHistory().containsKey(video.getTitle()))
                        .toList();

        // comparator used to compare videos after favourite counter
        Comparator<Video> favouriteVideoComparator = Comparator.comparing(Video::getFavCount);

        // gets the most favourited video that should be recommended for a user
        Video mostFaveVideo = unseenVideos.stream().max(favouriteVideoComparator).orElse(null);

        if (mostFaveVideo != null) {
            return "FavoriteRecommendation result: " + mostFaveVideo.getTitle();
        }

        return "FavoriteRecommendation cannot be applied!";
    }

    @Override
    public final String searchRecommendation(final String genre) {
        List<Video> unseenVideos =
                // gets the list of unseen videos
                Database.getInstance().getVideosList().stream()
                        .filter(video -> !this.getHistory().containsKey(video.getTitle()))
                        .toList();

        // comparator used to compare videos after rating and title
        Comparator<Video> ratingComparator =
                Comparator.comparing(Video::getRating).thenComparing(Video::getTitle);
        List<Video> genreUnseenVideos =
                // gets the list of unseen videos witch match with a specified genre
                unseenVideos.stream()
                        .filter(
                                video -> {
                                    for (String checkGenre : video.getGenres()) {
                                        if (checkGenre.equals(genre)) {
                                            return true;
                                        }
                                    }

                                    return false;
                                })
                        .sorted(ratingComparator)
                        .toList();

        // build the message for the output
        if (genreUnseenVideos != null && genreUnseenVideos.size() > 0) {
            StringBuilder searchVideos = new StringBuilder();
            int counter = 0;
            for (Video video : genreUnseenVideos) {
                counter++;
                if (counter == genreUnseenVideos.size()) {
                    searchVideos.append(video.getTitle());
                    break;
                }

                searchVideos.append(video.getTitle()).append(", ");
            }

            return "SearchRecommendation result: [" + searchVideos + "]";
        } else {
            return "SearchRecommendation cannot be applied!";
        }
    }
}
