package actor;

import database.Database;
import video.Video;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Actor {
    private String name;
    private String careerDescription;
    private ArrayList<String> filmography;
    private HashMap<ActorsAwards, Integer> awards;

    public Actor() {
    }

    /**
     * constructor to instantiate an Actor object
     * @param name name of Actor
     * @param careerDescription career description of an actor
     * @param filmography all movies / tv shows that an actor played in
     * @param awards all awards that an actor has got in the past
     */
    public Actor(
            final String name,
            final String careerDescription,
            final ArrayList<String> filmography,
            final HashMap<ActorsAwards, Integer> awards) {
        this.name = name;
        this.careerDescription = careerDescription;
        this.filmography = filmography;
        this.awards = awards;
    }

    /**
     * get map of awards
     * @return hashmap of awards and no of awards
     */
    public HashMap<ActorsAwards, Integer> getAwards() {
        return awards;
    }

    /**
     * getter of name
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * getter of Movies in which an actor played
     * @return list of strings (movies)
     */
    public ArrayList<String> getFilmography() {
        return filmography;
    }

    /**
     * getter of career description of an actor played
     * @return String CareerDescription
     */
    public String getCareerDescription() {
        return careerDescription;
    }

    /**
     * get average rating of an Actor
     * @return double rating
     */
    public Double getAverageRating() {
        int noRatings = 0;
        double averageRating = 0.0;

        for (String title : filmography) {
            List<Video> videos =
                    Database.getInstance().getVideosList().stream()
                            .filter(checkVideo -> checkVideo.getTitle().equals(title))
                            .toList();

            for (Video video : videos) {
                if (video != null) {
                    double rating = video.getRating();
                    if (rating != 0.0) {
                        averageRating += rating;
                        noRatings++;
                    }
                }
            }
        }

        if (noRatings != 0) {
            return averageRating / noRatings;
        }

        return 0.0;
    }

    /**
     * get total no of awards gotten by an actor
     * @return Integer no of awards
     */
    public Integer getTotalNoAwards() {
        Integer cnt = 0;

        for (Integer noAwards : awards.values()) {
            cnt += noAwards;
        }

        return cnt;
    }
}
