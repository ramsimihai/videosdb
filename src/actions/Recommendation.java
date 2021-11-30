package actions;

import Database.Database;
import classes.User;
import org.json.JSONObject;

public class Recommendation extends Action {

	public Recommendation(int actionID, String actionType, String type, String user) {
		super(actionID, actionType, type, user);
	}

	@Override
	public JSONObject execute() {
		JSONObject myObj = new JSONObject();
		switch (this.getType()) {
			case "standard":
				User ourUser = Database.getInstance().getUsersList().stream()
						.filter(user -> user.getUsername().equals(super.getUser()))
						.findAny().orElse(null);
				Database.getInstance().getVideosList().stream().forEach(video -> {


					// TODO: trebuie facuta gasirea in functie de first video
//					if (!ourUser.getHistory().containsKey(video.getTitle())) {
//						return video.getTitle();
//					}

				});
				break;
		}
		return myObj;
	}
}
