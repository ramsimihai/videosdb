package fileio;

import actor.ActorsAwards;

import java.util.ArrayList;
import java.util.Map;

/**
 * Information about an actor, retrieved from parsing the input test files
 *
 * <p>DO NOT MODIFY
 */
public final class ActorInputData {
  /** description of the actor's career */
  private final String careerDescription;
  /** awards won by the actor */
  private final Map<ActorsAwards, Integer> awards;
  /** actor name */
  private String name;
  /** videos starring actor */
  private ArrayList<String> filmography;

  public ActorInputData(
      final String name,
      final String careerDescription,
      final ArrayList<String> filmography,
      final Map<ActorsAwards, Integer> awards) {
    this.name = name;
    this.careerDescription = careerDescription;
    this.filmography = filmography;
    this.awards = awards;
  }

  public String getName() {
    return name;
  }

  public void setName(final String name) {
    this.name = name;
  }

  public ArrayList<String> getFilmography() {
    return filmography;
  }

  public void setFilmography(final ArrayList<String> filmography) {
    this.filmography = filmography;
  }

  public Map<ActorsAwards, Integer> getAwards() {
    return awards;
  }

  public String getCareerDescription() {
    return careerDescription;
  }

  @Override
  public String toString() {
    return "ActorInputData{"
        + "name='"
        + name
        + '\''
        + ", careerDescription='"
        + careerDescription
        + '\''
        + ", filmography="
        + filmography
        + '}';
  }
}
