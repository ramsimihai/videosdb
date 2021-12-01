package classes;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class Video {
	private String title;
	private int year;
	private ArrayList<String> cast;
	private ArrayList<String> genres;
	private int favCount;
	private int viewCount;
	private HashMap<User, Double> ratings;
	private int numberOfSeasons;

	public Video() {}

	public Video(String title, ArrayList<String> cast, ArrayList<String> genres, int year) {
		this.title = title;
		this.year = year;
		this.cast = cast;
		this.genres = genres;
		this.favCount = 0;
		this.viewCount = 0;
		this.ratings = new HashMap<>();
		this.numberOfSeasons = 0;
	}

	public HashMap<User, Double> getRatings() {
		return ratings;
	}

	public int getFavCount() {
		return favCount;
	}

	public int getViewCount() {
		return viewCount;
	}

	public void increaseFavCount() {
		this.favCount++;
	}

	public int getYear() {
		return year;
	}

	public ArrayList<String> getCast() {
		return cast;
	}

	public String getTitle() {
		return title;
	}

	public ArrayList<String> getGenres() {
		return genres;
	}

	public void setGenres(ArrayList<String> genres) {
		this.genres = genres;
	}

	public void setCast(ArrayList<String> cast) {
		this.cast = cast;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setFavCount(int favCount) {
		this.favCount = favCount;
	}

	public void setRatings(HashMap<User, Double> ratings) {
		this.ratings = ratings;
	}

	public void setViewCount(int viewCount) {
		this.viewCount = viewCount;
	}

	public int getNumberOfSeasons() {
		return numberOfSeasons;
	}

	public void setNumberOfSeasons(int numberOfSeasons) {
		this.numberOfSeasons = numberOfSeasons;
	}

	public abstract double getRating();

	public abstract int getDuration();
}
