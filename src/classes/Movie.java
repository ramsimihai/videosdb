package classes;

import java.util.ArrayList;

public class Movie extends Video {
	private int duration;

	public Movie() {}

	public Movie(String title, ArrayList<String> cast, ArrayList<String> genres, int year, int duration) {
		super(title, cast, genres, year);
		this.duration = duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public int getDuration() {
		return duration;
	}

	public double getRating() {
		double sum = 0.0;
		int noMovies = 0;

		for (Double rating : super.getRatings().values()) {
			if (rating != 0) {
				sum += rating;
				noMovies++;
			}
		}

		if (sum != 0.0) {
			return sum / noMovies;
		}

		return 0.0;
	}
}
