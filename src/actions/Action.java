package actions;

import org.json.JSONObject;

public abstract class Action {
    private final int actionID;
    private String user;
    private String actionType;
    private String type;

    /**
     * constructor to initialize the fields of an object
     * of a Class which extends Action
     * @param actionID ID of the action
     * @param actionType type of action (command / query / rec)
     * @param type type of objects on which the action are applied
     * @param user get the username
     */
    public Action(final int actionID,
                  final String actionType,
                  final String type,
                  final String user) {
        this.user = user;
        this.actionID = actionID;
        this.actionType = actionType;
        this.type = type;
    }

    /**
     * get object type to do commands on
     */
    public final String getType() {
        return type;
    }

    /**
     * getter of an actionID
     */
    public final int getActionID() {
        return actionID;
    }

    /**
     * getter of an action type
     */
    public final String getActionType() {
        return actionType;
    }

    /**
     * getter of a username
     */
    public final String getUser() {
        return user;
    }

    /**
     * execute a command in function of what type of action (query, command, recommend)
     * @return JSONObject after the execution of a command
     */
    public abstract JSONObject execute();
}
