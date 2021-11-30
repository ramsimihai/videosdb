package classes;

import actor.ActorsAwards;

import java.util.ArrayList;
import java.util.HashMap;

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
}
