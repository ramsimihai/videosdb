package actions;

import fileio.ActionInputData;
import org.json.simple.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class Commander {
    private final List<Action> commands;

    /**
     * instantiating a list of commands
     */
    public Commander() {
        this.commands = new ArrayList<>();
    }

    /**
     * adds all the input actions into the commander so it will be executed
     * @param inputCommands List of input Commands
     */
    public void addActions(final List<ActionInputData> inputCommands) {
        inputCommands.forEach(command -> {
            Action newAction;
            switch (command.getActionType()) {
                case "command" -> {
                    newAction = new BasicCommand(command.getActionId(),
                            command.getActionType(), command.getType(),
                            command.getUsername(), command.getTitle(),
                            command.getGrade(), command.getSeasonNumber());
                    this.commands.add(newAction);
                }
                case "query" -> {
                    newAction = new Query(command.getActionId(),
                            command.getActionType(), command.getObjectType(),
                            command.getUsername(), command.getSortType(),
                            command.getCriteria(), command.getNumber(),
                            command.getFilters());
                    this.commands.add(newAction);
                }
                case "recommendation" -> {
                    newAction = new Recommendation(command.getActionId(),
                            command.getActionType(),
                            command.getType(),
                            command.getUsername(),
                            command.getGenre());
                    this.commands.add(newAction);
                }
                default -> {
                    return;
                }
            }
        });
    }

    /**
     * execute all the commands from added to the commander
     * @return an Array of JSONObjects for the output
     */
    public JSONArray execute() {
        JSONArray array = new JSONArray();

        for (Action command : commands) {
            array.add(command.execute());
        }

        return array;
    }

    /**
     * clear the commander
     */
    public void clear() {
        commands.clear();
    }
}
