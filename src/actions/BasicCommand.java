package actions;

import database.Database;
import users.User;
import org.json.JSONObject;

public class BasicCommand extends Action {
    private String title;
    private double grade;
    private int season;

    /**
     * constructor to instantiate an object of a Basic Command
     * @param actionID number of the action
     * @param actionType type -> command
     * @param type objects on which commands are performed
     * @param user username
     * @param title title of the video
     * @param grade grade to rate a show
     * @param season get the number of season to be operated
     */
    public BasicCommand(final int actionID, final String actionType,
                        final String type, final String user,
                        final String title, final double grade,
                        final int season) {
        super(actionID, actionType, type, user);
        this.title = title;
        this.grade = grade;
        this.season = season;
    }

    /**
     * title getter
     */
    public String getTitle() {
        return title;
    }

    /**
     * grade getter
     */
    public double getGrade() {
        return grade;
    }

    /**
     * season getter
     */
    public int getSeason() {
        return season;
    }

    /**
     * the execution of a basic command in function of type
     * @return
     */
    @Override
    public JSONObject execute() {
        JSONObject myObj = new JSONObject();
        myObj.put("id", getActionID());

        User ourUser = Database.getInstance().getUsersList()
                .stream().filter(user -> user.getUsername().equals(this.getUser()))
                .findAny().orElse(null);
        String message = null;
        switch (this.getType()) {
            case "favorite" -> {
                assert ourUser != null;
                message = ourUser.addToFavourites(title);
            }
            case "view" -> {
                assert ourUser != null;
                message = ourUser.addToViewed(title);
            }
            case "rating" -> {
                assert ourUser != null;
                if (season > 0) {
                    message = ourUser.addRating(title, grade, season);
                } else {
                    message = ourUser.addRating(title, grade);
                }
            }
            default -> {
                return null;
            }
        }
        myObj.put("message", message);
        return myObj;
    }
}
