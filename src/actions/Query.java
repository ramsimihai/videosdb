package actions;

import Database.Database;
import classes.Actor;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Query extends Action {
	private int number;
	private List<List<String>> filters = new ArrayList<>();
	private String sortType;
	private String criteria;

	public Query(int actionID, String actionType, String type, String user,
				 String sortType, String criteria, int number, List<List<String>> filters) {
		super(actionID, actionType, type, user);
		this.number = number;
		this.sortType = sortType;
		this.criteria = criteria;
		this.filters.add(new ArrayList<>(filters.get(0)));
		this.filters.add(new ArrayList<>(filters.get(1)));
		this.filters.add(filters.get(2));
		this.filters.add(filters.get(3));
	}

	public int getNumber() {
		return number;
	}

	public List<List<String>> getFilters() {
		return filters;
	}

	List<Actor> filteredActorsList(List<Actor> actors, List<List<String>> filters) {
		System.out.println(filters);

		if (filters.get(3) != null && !filters.get(3).isEmpty()) {
			//System.out.println(filters);
			List<String> requiredAwardsList = filters.get(3).stream()
					.map(String::toLowerCase)
					.toList();
			//System.out.println(requiredAwardsList);
			actors = actors.stream().filter(actor -> {
				List<String> actorAwardsList = actor.getAwards().keySet().stream()
						.map(award -> String.valueOf(award).toLowerCase())
						.toList();
				int counter = 0;
				for (String requiredAward : requiredAwardsList) {
					if (actorAwardsList.contains(requiredAward)) {
						counter++;
					}
				}

				if (counter >= requiredAwardsList.size()) {
					return true;
				}
				return false;
			}).collect(Collectors.toList());
		}

		if (filters.get(2) != null && !filters.get(2).isEmpty()) {
			//System.out.println(filters);
			actors = actors.stream().filter(actor -> {
				for (String keyword : filters.get(2)) {
					String regex = "[ -]" + keyword + "[ ,.!?'-]";
					Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
					Matcher matcher = pattern.matcher(actor.getCareerDescription());

					if (!matcher.find()) {
						return false;
					}
				}

				return true;
			}).collect(Collectors.toList());
		}

		for (Actor actor : actors) {
			System.out.print(actor.getName() + " ");
		}
		System.out.println("");
		return actors;
	}

	public String queryActors() {
		List<Actor> filteredActors = filteredActorsList(Database.getInstance().getActorsList(), this.filters);

		List<Actor> sortedList = null;
		switch (this.getType()) {
			case "filter_description":
				Comparator<Actor> nameComparator = Comparator.comparing(Actor::getName);

				if (sortType.equals("desc")) {
					sortedList = filteredActors.stream().sorted(nameComparator.reversed()).limit(this.number).collect(Collectors.toList());
					break;
				}

				sortedList = filteredActors.stream().sorted(nameComparator).limit(this.number).collect(Collectors.toList());
				break;
			case "average":
				Comparator<Actor> averageComparator = Comparator.comparing(Actor::getAverageRating);

				if (sortType.equals("desc")) {
					sortedList = filteredActors.stream().sorted(averageComparator.reversed()).limit(this.number).collect(Collectors.toList());
					break;
				}

				sortedList = filteredActors.stream().sorted(averageComparator).limit(this.number).collect(Collectors.toList());
				break;
			case "awards":
				Comparator<Actor> awardsComparator = Comparator.comparing(Actor::getTotalNoAwards);

				if (sortType.equals("desc")) {
					sortedList = filteredActors.stream().sorted(awardsComparator.reversed()).limit(this.number).collect(Collectors.toList());
					break;
				}

				sortedList = filteredActors.stream().sorted(awardsComparator).limit(this.number).collect(Collectors.toList());
				break;
		}
		return "Query: " + sortedList;
	}

	public String queryMovies() {
		return "ceva";
	}

	public String queryUsers() {
		return "ceva";
	}

	@Override
	public JSONObject execute() {
		JSONObject myObj = new JSONObject();
		myObj.put("id", getActionID());
		switch (getType()) {
			case "actors":
				myObj.put("message", this.queryActors());
				break;
//			case "movies":
//				myObj.put("message", this.queryMovies());
//				break;
//			case "users":
//				myObj.put("message", this.queryUsers());
//				break;

		}
		return myObj;
	}
}
