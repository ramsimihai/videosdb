package Database;


import actions.Commander;
import actor.ActorsAwards;
import classes.*;
import fileio.*;

import org.json.JSONObject;
import org.json.simple.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Database {
	private List<Actor> actorsList = new ArrayList<>();
	private List<Movie> moviesList = new ArrayList<>();
	private List<Show> showsList = new ArrayList<>();
	private List<User> usersList = new ArrayList<>();
	private List<Video> videosList = new ArrayList<>();
	private Commander controlPanel = new Commander();

	private static Database database = null;

	private Database() {}

	public static Database getInstance() {
		if (database == null) {
			database = new Database();
		}

		return database;
	}

	public void addActors(List<ActorInputData> actorsList) {
		actorsList.stream().forEach(actor -> {
			Actor newActor = new Actor(actor.getName(),
					actor.getCareerDescription(),
					actor.getFilmography(),
					(HashMap<ActorsAwards, Integer>) actor.getAwards());
			database.actorsList.add(newActor);
		});
	}

	public void addMovies(List<MovieInputData> moviesList) {
		moviesList.stream().forEach(movie -> {
			Movie newMovie = new Movie(movie.getTitle(),
					movie.getCast(),
					movie.getGenres(),
					movie.getYear(),
					movie.getDuration());
			database.moviesList.add(newMovie);
		});
	}

	public void addShows(List<SerialInputData> showsList) {
		showsList.stream().forEach(show -> {
			Show newShow = new Show(show.getTitle(),
					show.getCast(),
					show.getGenres(),
					show.getYear(),
					show.getNumberSeason(),
					show.getSeasons());
			database.showsList.add(newShow);
		});
	}

	public void addUsers(List<UserInputData> usersList) {
		usersList.stream().forEach(user -> {
			User newUser = new User(user.getUsername(),
					user.getSubscriptionType(),
					user.getHistory(),
					user.getFavoriteMovies());
			database.usersList.add(newUser);
		});
	}

	public void addVideos() {
		this.moviesList.stream().forEach(movie -> {
			database.videosList.add(movie);
		});
		this.showsList.stream().forEach(show -> {
			database.videosList.add(show);
		});
	}

	public JSONArray addCommands(List<ActionInputData> actions) {
		controlPanel.addActions(actions);
		return controlPanel.execute();
	}

	public List<User> getUsersList() {
		return usersList;
	}

	public List<Video> getVideosList() {
		return videosList;
	}

	public List<Actor> getActorsList() {
		return actorsList;
	}

	public List<Movie> getMoviesList() {
		return moviesList;
	}

	public List<Show> getShowsList() {
		return showsList;
	}

	public void clear() {
		usersList.clear();
		moviesList.clear();
		actorsList.clear();
		showsList.clear();
		videosList.clear();
		controlPanel = new Commander();
	}
}
