package Database;


import actor.ActorsAwards;
import classes.*;
import fileio.ActorInputData;
import fileio.MovieInputData;
import fileio.SerialInputData;
import fileio.UserInputData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Database {
	private final List<Actor> actorsList = new ArrayList<>();
	private final List<Movie> moviesList = new ArrayList<>();
	private final List<Show> showsList = new ArrayList<>();
	private final List<User> usersList = new ArrayList<>();
	private final List<Video> videosList = new ArrayList<>();
	private static Database database = null;

	private Database() {}

	public static Database getInstance() {
		if (database == null) {
			database = new Database();
		}

		return database;
	}

	public static int getNumberOfInstances() {
		if (database != null) {
			return 1;
		}

		return 0;
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
			Video videoMovie = new Video(movie.getTitle(), movie.getCast(), movie.getGenres(), movie.getYear());
			database.videosList.add(videoMovie);
		});
		this.showsList.stream().forEach(show -> {
			Video showsVideo = new Video(show.getTitle(), show.getCast(), show.getGenres(), show.getYear());
			database.videosList.add(showsVideo);
		});
	}

}
