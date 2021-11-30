package actions;

import fileio.ActionInputData;
import org.json.JSONObject;
import org.json.simple.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class Commander {
	private List<Action> commands;
	
	public Commander() {
		this.commands = new ArrayList<>();
	}

	public void addActions(List<ActionInputData> commands) {
		commands.stream().forEach(command -> {
			Action newAction;
			switch (command.getActionType()) {
				case "command":
					newAction = new BasicCommand(command.getActionId(),
							command.getActionType(),
							command.getType(), command.getUsername(),
							command.getTitle(), command.getGrade(),
							command.getSeasonNumber());
					this.commands.add(newAction);
					break;
				case "query":
					newAction = new Query(command.getActionId(), command.getActionType(), command.getObjectType(), command.getUsername(),
						command.getSortType(), command.getCriteria(), command.getNumber(), command.getFilters());
					this.commands.add(newAction);
					break;
				case "recommendation":
					break;
			}
		});
	}

	public JSONArray execute() {
		JSONArray array = new JSONArray();
		this.commands.stream().forEach(command -> array.add(command.execute()));
		return array;
	}
}
