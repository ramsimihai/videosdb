package classes;

import Database.Database;
import actor.ActorsAwards;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class Actor {
	private String name;
	private String careerDescription;
	private ArrayList<String> filmography;
	private HashMap<ActorsAwards, Integer> awards;

	public Actor() {}

	public Actor(String name, String careerDescription, ArrayList<String> filmography, HashMap<ActorsAwards, Integer> awards) {
		this.name = name;
		this.careerDescription = careerDescription;
		this.filmography = filmography;
		this.awards	= awards;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setAwards(HashMap<ActorsAwards, Integer> awards) {
		this.awards = awards;
	}

	public void setCareerDescription(String careerDescription) {
		this.careerDescription = careerDescription;
	}

	public void setFilmography(ArrayList<String> filmography) {
		this.filmography = filmography;
	}

	public HashMap<ActorsAwards, Integer> getAwards() {
		return awards;
	}

	public String getName() {
		return name;
	}

	public ArrayList<String> getFilmography() {
		return filmography;
	}

	public String getCareerDescription() {
		return careerDescription;
	}

	public Double getAverageRating() {
		int noRatings = 0;
		double averageRating = 0.0;

		for (String title : filmography) {
			List<Video> videos = Database.getInstance().getVideosList().stream().filter(checkVideo -> {
				if (checkVideo.getTitle().equals(title))
						return true;

				return false;
			}).collect(Collectors.toList());

			for (Video video : videos) {
				if (video != null) {
					double rating = video.getRating();
					if (rating != 0.0) {
						averageRating += rating;
						noRatings++;
					}
				}
			}
		}

		if (noRatings != 0) {
			return averageRating / noRatings;
		}

		return 0.0;
	}

	public Integer getTotalNoAwards() {
		Integer cnt = 0;

		for (Integer noAwards : awards.values()) {
			cnt += noAwards;
		}

		return cnt;
	}
}
