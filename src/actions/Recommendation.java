package actions;

import users.User;
import video.Video;
import database.Database;
import org.json.JSONObject;

import java.util.Comparator;

public class Recommendation extends Action {
    private final String genre;

    /**
     * constructor to instantiate an object of a recommendation
     * @param actionID number of the action
     * @param actionType type -> command
     * @param type objects on which commands are performed
     * @param user username
     */
    public Recommendation(
            final int actionID,
            final String actionType,
            final String type,
            final String user,
            final String genre) {
        super(actionID, actionType, type, user);
        this.genre = genre;
    }

    /**
     * execute a command in function of recommendations fields
     * @return JSONObject after the execution of a command
     */
    @Override
    public JSONObject execute() {
        JSONObject myObj = new JSONObject();
        myObj.put("id", getActionID());

        // get the user that performs the action
        User currUser = Database.getInstance().getUsersList()
                .stream()
                .filter(user -> user.getUsername().equals(super.getUser()))
                .findAny().orElse(null);

        switch (getType()) {
            case "standard" -> {
                Video unseenVideo = Database.getInstance()
                        // gets the videos list and filters it if the video's not seen
                        .getVideosList().stream().filter(video -> {
                    assert currUser != null;
                    return !currUser.getHistory().containsKey(video.getTitle());
                    // gets the first video which is not seen
                }).findFirst().orElse(null);

                if (unseenVideo == null) {
                    myObj.put("message", "StandardRecommendation cannot be applied!");
                    break;
                }

                myObj.put("message", "StandardRecommendation result: " + unseenVideo.getTitle());
            }
            case "best_unseen" -> {
                // comparator to get the best video unseen by rating
                Comparator<Video> ratingComparator = Comparator.comparing(Video::getRating);

                // get the list of videos
                Video notSeenVideo = Database.getInstance().getVideosList()
                        // transformes it into a stream and filters so it remains only unseen
                        // videos of the user
                        .stream().filter(video -> {
                    assert currUser != null;
                    return !currUser.getHistory().containsKey(video.getTitle());
                    // get the video with the most videos from a list
                }).max(ratingComparator).orElse(null);
                if (notSeenVideo == null) {
                    myObj.put("message", "BestRatedUnseenRecommendation cannot be applied!");
                    break;
                }
                myObj.put("message", "BestRatedUnseenRecommendation result: "
                        + notSeenVideo.getTitle());
            }
            case "popular" -> {
                assert currUser != null;
                myObj.put("message", currUser.popularRecommendation());
            }
            case "favorite" -> {
                assert currUser != null;
                myObj.put("message", currUser.favouriteRecommendation());
            }
            case "search" -> {
                assert currUser != null;
                myObj.put("message", currUser.searchRecommendation(genre));
            }
            default -> {
                return null;
            }
        }
        return myObj;
    }
}
