package actions;

import Database.Action;

public class Command extends Action {
	private String title;
	private int grade;
	private int season;

	public Command(int actionID, String actionType, String type, String user, String title, int grade, int season) {
		super(actionID, actionType, type, user);
		this.title = title;
		this.grade = grade;
		this.season = season;
	}

	public String getTitle() {
		return title;
	}

	public int getGrade() {
		return grade;
	}

	public int getSeason() {
		return season;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setGrade(int grade) {
		this.grade = grade;
	}

	public void setSeason(int season) {
		this.season = season;
	}
}
