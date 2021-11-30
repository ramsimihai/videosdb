package classes;

import entertainment.Season;

import java.util.ArrayList;

public class Show extends Video {
	private int numberOfSeasons;
	private ArrayList<Season> seasons;

	public Show() {}

	public Show(String title, ArrayList<String> cast, ArrayList<String> genres, int year, int numberOfSeasons, ArrayList<Season> seasons) {
		super(title, cast, genres, year);
		this.numberOfSeasons = numberOfSeasons;
		this.seasons = seasons;
	}

	public int getNumberOfSeasons() {
		return numberOfSeasons;
	}

	public ArrayList<Season> getSeasons() {
		return seasons;
	}

	public void setNumberOfSeasons(int numberOfSeasons) {
		this.numberOfSeasons = numberOfSeasons;
	}

	public void setSeasons(ArrayList<Season> seasons) {
		this.seasons = seasons;
	}
}
