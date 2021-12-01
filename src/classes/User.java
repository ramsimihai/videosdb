package classes;

import Database.Database;
import entertainment.Season;

import java.util.ArrayList;
import java.util.Map;

public class User {
	private String username;
	private String subscriptionType;
	private Map<String, Integer> history;
	private ArrayList<String> favouriteVideos;
	private int noRatingsGiven;

	public User() {}

	public User(String username, String subscriptionType,
						 Map<String, Integer> history,
						 ArrayList<String> favoriteMovies) {
		this.username = username;
		this.subscriptionType = subscriptionType;
		this.favouriteVideos = favoriteMovies;
		this.history = history;

		this.noRatingsGiven = 0;
	}

	public int getNoRatingsGiven() {
		return noRatingsGiven;
	}

	public void setFavouriteVideos(ArrayList<String> favoriteMovies) {
		this.favouriteVideos = favoriteMovies;
	}

	public void setHistory(Map<String, Integer> history) {
		this.history = history;
	}

	public void setSubscriptionType(String subscriptionType) {
		this.subscriptionType = subscriptionType;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public ArrayList<String> getFavouriteVideos() {
		return favouriteVideos;
	}

	public Map<String, Integer> getHistory() {
		return history;
	}

	public String getSubscriptionType() {
		return subscriptionType;
	}

	public String getUsername() {
		return username;
	}

	public void incNoRatingsGiven() {
		this.noRatingsGiven++;
	}

	public String addToFavourites(String title) {
		if (getHistory().containsKey(title)) {
			Video video = Database.getInstance().getVideosList().stream().filter(foundVideo -> foundVideo.getTitle().equals(title)).findAny().orElse(null);
			for (String faveVideo : getFavouriteVideos()) {
				if (faveVideo.equals(title)) {
					return "error -> " + title + " is already in favourite list";
				}
			}

			getFavouriteVideos().add(title);
			incNoRatingsGiven();
			return "success -> " + title + " was added as favourite";
		}
		return "error -> " + title + " is not seen";
	}

	public String addToViewed(String title) {
		if (getHistory().containsKey(title)) {
			getHistory().replace(title, getHistory().get(title) + 1);
		} else {
			getHistory().put(title, 1);
		}
		return "success -> " + title + " with total views of " + getHistory().get(title);
	}

	public String addRating(String title, Double grade) {
		if (getHistory().containsKey(title)) {
			Movie movie = Database.getInstance().getMoviesList().stream().filter(foundMovie -> foundMovie.getTitle().equals(title)).findAny().orElse(null);

			if (movie == null) {
				return "error -> " + title + " is not seen";
			}

			if (movie.getRatings().containsKey(this)) {
				return "error -> " + title + " has been already rated";
			}

			movie.getRatings().put(this, grade);
			incNoRatingsGiven();
			return "success -> " + title + " was rated with " + grade + " by " + this.getUsername();

		}
		return "error -> " + title + " is not seen";
	}

	public String addRating(String title, Double grade, int season) {
		if (getHistory().containsKey(title)) {
			Show show = Database.getInstance().getShowsList().stream().filter(foundShow -> foundShow.getTitle().equals(title)).findAny().orElse(null);
			for (Season currSeason : show.getSeasons()) {
				if (currSeason.getCurrentSeason() == season) {
					if (currSeason.getRatings().containsKey(this)) {
						return "error -> " + title + " has been already rated";
					}

					currSeason.getRatings().put(this, grade);
					return "success -> " + title + " was rated with " + grade + " by " + this.getUsername();
				}
			}
		}
		return "error -> " + title + " is not seen";
	}
}
