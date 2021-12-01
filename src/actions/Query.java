package actions;

import Database.Database;
import classes.*;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.lang.Integer.parseInt;

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
		if (filters.get(3) != null && !filters.get(3).isEmpty()) {
			List<String> requiredAwardsList = filters.get(3).stream()
					.map(String::toLowerCase)
					.toList();
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

		return actors;
	}

	public String queryActors() {
		List<Actor> filteredActors = filteredActorsList(Database.getInstance().getActorsList(), this.filters);

		List<Actor> sortedList = null;
		switch (this.getType()) {
			case "filter_description":
				Comparator<Actor> nameComparator = Comparator.comparing(Actor::getName);

				if (sortType.equals("desc")) {
					nameComparator = nameComparator.reversed();
				}

				sortedList = filteredActors.stream().sorted(nameComparator).limit(this.number).collect(Collectors.toList());
				break;
			case "average":
				Comparator<Actor> averageComparator = Comparator.comparing(Actor::getAverageRating);

				if (sortType.equals("desc")) {
					averageComparator = averageComparator.reversed();
				}

				sortedList = filteredActors.stream().sorted(averageComparator).limit(this.number).collect(Collectors.toList());
				break;
			case "awards":
				Comparator<Actor> awardsComparator = Comparator.comparing(Actor::getTotalNoAwards).thenComparing(Actor::getName);

				if (sortType.equals("desc")) {
					awardsComparator = awardsComparator.reversed();
				}

				sortedList = filteredActors.stream().sorted(awardsComparator).limit(this.number).collect(Collectors.toList());
				break;
		}

		if (sortedList == null) {
			sortedList = filteredActors;
		}

		StringBuilder actorsNameList = new StringBuilder();
		int counter = 0;
		for (Actor actor : sortedList) {
			counter++;
			if (counter == sortedList.size()) {
				actorsNameList.append(actor.getName());
				break;
			}
			actorsNameList.append(actor.getName() + " ");
		}

		return "Query result: [" + actorsNameList + "]";
	}

	public List<Movie> filteredMoviesList(List<Movie> movies, List<List<String>> filters) {
		if (filters.get(0) != null && !filters.get(0).isEmpty()) {
			movies = movies.stream().filter(movie -> {
				if (filters.get(0).get(0) == null) {
					return false;
				} else if (movie.getYear() == Integer.parseInt(filters.get(0).get(0))) {
					return true;
				}

				return false;
			}).toList();
		}

		if (filters.get(1) != null && !filters.get(1).isEmpty()) {
			movies = movies.stream().filter(movie -> {
				for (String genre : movie.getGenres()) {
					if (genre.equals(filters.get(1).get(0))) {
						return true;
					}
				}
				return false;
			}).toList();
		}

		return movies;
	}

	public String queryMovies() {
		List<Movie> filteredMovies = filteredMoviesList(Database.getInstance().getMoviesList(), filters);
		List<Movie> sortedList = null;
		switch (criteria) {
			case "rating":
				Comparator<Movie> ratingComparator = Comparator.comparing(Movie::getRating).thenComparing(Movie::getTitle);

				if (sortType.equals("desc")) {
					ratingComparator = ratingComparator.reversed();
				}

				sortedList = filteredMovies.stream().sorted(ratingComparator).limit(number).collect(Collectors.toList());
				break;
			case "favorite":
				Comparator<Movie> favComparator = Comparator.comparing(Movie::getFavCount).thenComparing(Movie::getTitle);

				if (sortType.equals("desc")) {
					favComparator = favComparator.reversed();
				}

				sortedList = filteredMovies.stream().sorted(favComparator).limit(number).collect(Collectors.toList());
				break;
			case "longest":
				Comparator<Movie> longestComparator = Comparator.comparing(Movie::getDuration).thenComparing(Movie::getTitle);
				if (sortType.equals("desc")) {
					longestComparator = longestComparator.reversed();
				}

				sortedList = filteredMovies.stream().sorted(longestComparator).limit(number).collect(Collectors.toList());

				break;
			case "most_viewed":
				Comparator<Movie> mostViewedComparator = Comparator.comparing(Movie::getViewCount).thenComparing(Movie::getTitle);

				if (sortType.equals("desc")) {
					mostViewedComparator = mostViewedComparator.reversed();
				}

				sortedList = filteredMovies.stream().sorted(mostViewedComparator).limit(number).collect(Collectors.toList());
				break;
		}
		if (sortedList == null) {
			sortedList = filteredMovies;
		}

		StringBuilder moviesNameList = new StringBuilder();
		int counter = 0;
		for (Movie movie : sortedList) {
			counter++;
			if (counter == sortedList.size()) {
				moviesNameList.append(movie.getTitle());
				break;
			}

			moviesNameList.append(movie.getTitle() + " ");
		}

		return "Query result: [" + moviesNameList + "]";
	}

	public List<Show> filteredShowsList(List<Show> shows, List<List<String>> filters) {
		if (filters.get(0) != null && !filters.get(0).isEmpty()) {
			shows = shows.stream().filter(show -> {
				if (filters.get(0).get(0) == null) {
					return false;
				} else if (show.getYear() == Integer.parseInt(filters.get(0).get(0))) {
					return true;
				}

				return false;
			}).toList();
		}

		if (filters.get(1) != null && !filters.get(1).isEmpty()) {
			shows = shows.stream().filter(show -> {
				for (String genre : show.getGenres()) {
					if (genre.equals(filters.get(1).get(0))) {
						return true;
					}
				}
				return false;
			}).toList();
		}

		return shows;
	}

	public String queryShows() {
		List<Show> filteredShows = filteredShowsList(Database.getInstance().getShowsList(), filters);

		List<Show> sortedList = null;
		switch (criteria) {
			case "rating":
				Comparator<Show> ratingComparator = Comparator.comparing(Show::getRating).thenComparing(Show::getTitle);

				if (sortType.equals("desc")) {
					ratingComparator = ratingComparator.reversed();
				}

				sortedList = filteredShows.stream().sorted(ratingComparator).limit(number).collect(Collectors.toList());
				break;
			case "favorite":
				Comparator<Show> favComparator = Comparator.comparing(Show::getFavCount).thenComparing(Show::getTitle);

				if (sortType.equals("desc")) {
					favComparator = favComparator.reversed();
				}

				sortedList = filteredShows.stream().sorted(favComparator).limit(number).collect(Collectors.toList());
				break;
			case "longest":
				Comparator<Show> longestComparator = Comparator.comparing(Show::getDuration).thenComparing(Show::getTitle);
				if (sortType.equals("desc")) {
					longestComparator = longestComparator.reversed();
				}

				sortedList = filteredShows.stream().sorted(longestComparator).limit(number).collect(Collectors.toList());

				break;
			case "most_viewed":
				Comparator<Show> mostViewedComparator = Comparator.comparing(Show::getViewCount).thenComparing(Show::getTitle);

				if (sortType.equals("desc")) {
					mostViewedComparator = mostViewedComparator.reversed();
				}

				sortedList = filteredShows.stream().sorted(mostViewedComparator).limit(number).collect(Collectors.toList());
				break;
		}
		if (sortedList == null) {
			sortedList = filteredShows;
		}

		StringBuilder showNameList = new StringBuilder();
		int counter = 0;
		for (Show show : sortedList) {
			counter++;
			if (counter == sortedList.size()) {
				showNameList.append(show.getTitle());
				break;
			}

			showNameList.append(show.getTitle() + " ");
		}

		return "Query result: [" + showNameList + "]";
	}

	public String queryUsers() {
		List<User> sortedList = Database.getInstance().getUsersList();
		Comparator<User> noRatingsComparator = Comparator.comparing(User::getNoRatingsGiven).thenComparing(User::getUsername);

		if (sortType.equals("desc")) {
			noRatingsComparator = noRatingsComparator.reversed();
		}

		sortedList = sortedList.stream().sorted(noRatingsComparator).limit(number).collect(Collectors.toList());

		StringBuilder usersNameList = new StringBuilder();
		int counter = 0;
		for (User user : sortedList) {
			counter++;
			if (counter == sortedList.size()) {
				usersNameList.append(user.getUsername());
				break;
			}

			usersNameList.append(user.getUsername() + " ");
		}

		return "Query result: [" + usersNameList + "]";
	}

	@Override
	public JSONObject execute() {
		JSONObject myObj = new JSONObject();
		myObj.put("id", getActionID());
		switch (getType()) {
			case "actors":
				myObj.put("message", this.queryActors());
				break;
			case "movies":
				myObj.put("message", this.queryMovies());
				break;
			case "shows":
				myObj.put("message", this.queryShows());
				break;
			case "users":
				myObj.put("message", this.queryUsers());
				break;

		}
		return myObj;
	}
}
