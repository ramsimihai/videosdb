package actions;

import Database.Database;
import classes.User;
import org.json.JSONObject;

public class BasicCommand extends Action {
	private String title;
	private double grade;
	private int season;

	public BasicCommand(int actionID, String actionType, String type, String user, String title, double grade, int season) {
		super(actionID, actionType, type, user);
		this.title = title;
		this.grade = grade;
		this.season = season;
	}

	public String getTitle() {
		return title;
	}

	public double getGrade() {
		return grade;
	}

	public int getSeason() {
		return season;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setGrade(int grade) {
		this.grade = grade;
	}

	public void setSeason(int season) {
		this.season = season;
	}

	@Override
	public JSONObject execute() {
		JSONObject myObj = new JSONObject();
		myObj.put("id", getActionID());

		User ourUser = Database.getInstance().getUsersList().stream().filter(user -> user.getUsername().equals(this.getUser())).findAny().orElse(null);

		String message = null;
		switch (this.getType()) {
			case "favorite":
				message = ourUser.addToFavourites(title);
				break;
			case "view":
				message = ourUser.addToViewed(title);
			case "rating":
				if (season > 0) {
					message = ourUser.addRating(title, grade, season);
				} else {
					message = ourUser.addRating(title, grade);
				}
		}
		myObj.put("message", message);
		return myObj;
	}
}
