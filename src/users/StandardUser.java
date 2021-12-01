package users;

import java.util.ArrayList;
import java.util.Map;

public class StandardUser extends User {
  /**
   * constructor to instantiate new Standard User object
   * @param username username
   * @param subscriptionType subscriptionType
   * @param history history of videos seen
   * @param favoriteMovies list of favourites movies
   */
  public StandardUser(
      final String username,
      final String subscriptionType,
      final Map<String, Integer> history,
      final ArrayList<String> favoriteMovies) {
    super(username, subscriptionType, history, favoriteMovies);
  }

  @Override
  public final String popularRecommendation() {
    return "PopularRecommendation cannot be applied!";
  }

  @Override
  public final String favouriteRecommendation() {
    return "FavoriteRecommendation cannot be applied!";
  }

  @Override
  public final String searchRecommendation(final String genre) {
    return "SearchRecommendation cannot be applied!";
  }
}
