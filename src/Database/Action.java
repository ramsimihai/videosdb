package Database;

public class Action {
	private int actionID;
	private String user;
	private String actionType;
	private String type;

	public Action(int actionID, String actionType, String type, String user) {
		this.user = user;
		this.actionID = actionID;
		this.actionType = actionType;
		this.type = type;
	}
	public String getType() {
		return type;
	}

	public int getActionID() {
		return actionID;
	}

	public String getActionType() {
		return actionType;
	}

	public String getUser() {
		return user;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setActionID(int actionID) {
		this.actionID = actionID;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	public void setUser(String user) {
		this.user = user;
	}
}
