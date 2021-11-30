package classes;

import entertainment.Season;

import java.util.ArrayList;

public class Show extends Video {
	private ArrayList<Season> seasons;

	public Show() {}

	public Show(String title, ArrayList<String> cast, ArrayList<String> genres, int year, int numberOfSeasons, ArrayList<Season> seasons) {
		super(title, cast, genres, year);
		super.setNumberOfSeasons(numberOfSeasons);
		this.seasons = seasons;
	}

	public ArrayList<Season> getSeasons() {
		return seasons;
	}

	public void setSeasons(ArrayList<Season> seasons) {
		this.seasons = seasons;
	}


	public double getRating() {
		double sum = 0.0;
		for (Season season : seasons) {
			if (season.getRatings().isEmpty()) {
				continue;
			}

			double currSum = 0.0;
			for (Double rating : season.getRatings().values()) {
				currSum += rating;
			}

			sum += currSum / season.getRatings().size();
		}

		if (sum != 0.0) {
			return sum / seasons.size();
		}

		return 0.0;
	}
}
