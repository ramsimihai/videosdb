package classes;

import java.util.ArrayList;
import java.util.Map;

public class User {
	private String username;
	private String subscriptionType;
	private Map<String, Integer> history;
	private ArrayList<String> favouriteMovies;

	public User() {}

	public User(String username, String subscriptionType,
						 Map<String, Integer> history,
						 ArrayList<String> favoriteMovies) {
		this.username = username;
		this.subscriptionType = subscriptionType;
		this.favouriteMovies = favoriteMovies;
		this.history = history;
	}

	public void setFavoriteMovies(ArrayList<String> favoriteMovies) {
		this.favouriteMovies = favoriteMovies;
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

	public ArrayList<String> getFavoriteMovies() {
		return favouriteMovies;
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
}
