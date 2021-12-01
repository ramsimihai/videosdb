package actions;

import database.Database;
import actor.Actor;
import video.Movie;
import video.Show;
import users.User;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static common.Constants.INDEX_GET_YEAR;
import static common.Constants.INDEX_GET_GENRE;
import static common.Constants.INDEX_GET_WORDS;
import static common.Constants.INDEX_GET_AWARDS;

public class Query extends Action {
    private final int number;
    private final List<List<String>> filters = new ArrayList<>();
    private final String sortType;
    private final String criteria;

    /**
     *
     * @param actionID number of ID
     * @param actionType type of action (query, command, recommend)
     * @param type type of objects on which the actions are performed
     * @param user username
     * @param sortType sort type (desc / asc)
     * @param criteria criteria to do a query
     * @param number number of results from a query
     * @param filters filters used for filtering the list
     */
    public Query(final int actionID,
                 final String actionType,
                 final String type, final String user,
                 final String sortType, final String criteria,
                 final int number, final List<List<String>> filters) {
        super(actionID, actionType, type, user);
        this.number = number;
        this.sortType = sortType;
        this.criteria = criteria;
        this.filters.add(new ArrayList<>(filters.get(INDEX_GET_YEAR)));
        this.filters.add(new ArrayList<>(filters.get(INDEX_GET_GENRE)));
        this.filters.add(filters.get(INDEX_GET_WORDS));
        this.filters.add(filters.get(INDEX_GET_AWARDS));
    }

    public final int getNumber() {
        return number;
    }

    public final List<List<String>> getFilters() {
        return filters;
    }

    /**
     * get the filtered list of actors by only two arguments from the filters list
     * such as words that matches in a description and awards gotten by the actors
     * @param actors list of actors
     * @param filtersForActors list of filters for actors
     * @return a list of actors that is filtered
     */
    List<Actor> filteredActorsList(List<Actor> actors, final List<List<String>> filtersForActors) {
        if (filtersForActors.get(INDEX_GET_AWARDS) != null
                && !filtersForActors.get(INDEX_GET_AWARDS).isEmpty()) {
            List<String> requiredAwardsList = filtersForActors
                    .get(INDEX_GET_AWARDS)
                    .stream()
                    .map(String::toLowerCase).toList();

            actors = actors.stream().filter(actor -> {
                List<String> actorAwardsList = actor.getAwards().keySet()
                        .stream().map(award -> String.valueOf(award)
                                .toLowerCase()).toList();
                int counter = 0;
                for (String requiredAward : requiredAwardsList) {
                    if (actorAwardsList.contains(requiredAward)) {
                        counter++;
                    }
                }

                return counter >= requiredAwardsList.size();
            }).collect(Collectors.toList());
        }

        if (filtersForActors.get(INDEX_GET_WORDS) != null
                && !filtersForActors.get(INDEX_GET_WORDS).isEmpty()) {
            actors = actors.stream().filter(actor -> {
                for (String keyword : filtersForActors.get(INDEX_GET_WORDS)) {
                    String regex = "[ -]" + keyword + "[ ,.!?'-]";
                    Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
                    Matcher matcher = pattern.matcher(actor.getCareerDescription());

                    if (!matcher.find()) {
                        return false;
                    }
                }

                return true;
            }).collect(Collectors.toList());
        }

        return actors;
    }

    /**
     * the main body of the command which is executed in function of criteria
     * filter_description -> for description of actors
     * average -> for average rating of actors
     * awards -> for number of total awards
     * all criteria lists are firstly filtered
     * @return
     */
    public String queryActors() {
        List<Actor> filteredActors = filteredActorsList(
                Database.getInstance().getActorsList(), this.filters);

        List<Actor> sortedList;

        switch (criteria) {
            case "filter_description" -> {
                Comparator<Actor> nameComparator = Comparator.comparing(Actor::getName);
                if (sortType.equals("desc")) {
                    nameComparator = nameComparator.reversed();
                }
                sortedList = filteredActors.stream()
                        .sorted(nameComparator).limit(this.number)
                        .collect(Collectors.toList());
            }
            case "average" -> {
                Comparator<Actor> averageComparator = Comparator.comparing(Actor::getAverageRating)
                        .thenComparing(Actor::getName);
                if (sortType.equals("desc")) {
                    averageComparator = averageComparator.reversed();
                }
                sortedList = filteredActors.stream().sorted(averageComparator)
                        .filter(actor -> actor.getAverageRating() > 0)
                        .limit(this.number).collect(Collectors.toList());
            }
            case "awards" -> {
                Comparator<Actor> awardsComparator = Comparator.comparing(Actor::getTotalNoAwards)
                        .thenComparing(Actor::getName);
                if (sortType.equals("desc")) {
                    awardsComparator = awardsComparator.reversed();
                }
                sortedList = filteredActors.stream().sorted(awardsComparator).limit(this.number)
                        .collect(Collectors.toList());
            }
            default -> {
                return null;
            }
        }

        StringBuilder actorsNameList = new StringBuilder();
        int counter = 0;
        for (Actor actor : sortedList) {
            counter++;
            if (counter == sortedList.size()) {
                actorsNameList.append(actor.getName());
                break;
            }
            actorsNameList.append(actor.getName()).append(", ");
        }

        return "Query result: [" + actorsNameList + "]";
    }

    /**
     * get the filtered list of movies by only two arguments from the filters list
     * such as years and genres
     * @param movies list of actors
     * @param moviesFilters list of filters for actors
     * @return a list of actors that is filtered
     */
    public List<Movie> filteredMoviesList(List<Movie> movies,
                                          final List<List<String>> moviesFilters) {
        if (moviesFilters.get(INDEX_GET_YEAR) != null
                && moviesFilters.get(INDEX_GET_YEAR).get(0) != null) {
            movies = movies.stream().filter(movie -> {
                if (moviesFilters.get(INDEX_GET_YEAR).get(0) == null) {
                    return false;
                } else {
                    return movie.getYear() == Integer.parseInt(
                        moviesFilters.get(INDEX_GET_YEAR).get(0));
                }
            }).toList();
        }

        if (moviesFilters.get(INDEX_GET_GENRE) != null
                && moviesFilters.get(INDEX_GET_GENRE).get(0) != null) {
            movies = movies.stream().filter(movie -> {
                for (String genre : movie.getGenres()) {
                    if (genre.equals(moviesFilters.get(INDEX_GET_GENRE).get(0))) {
                        return true;
                    }
                }
                return false;
            }).toList();
        }

        return movies;
    }

    /**
     * the main body of the command which is executed in function of criteria
     * ratings -> for ratings of movies
     * favorite -> for no favorites entries in users history
     * longest -> for longest movie
     * most_viewed -> for no total views
     * @return
     */
    public String queryMovies() {
        List<Movie> filteredMovies = filteredMoviesList(Database
                .getInstance().getMoviesList(), filters);

        List<Movie> sortedList;
        switch (criteria) {
            case "ratings" -> {
                Comparator<Movie> ratingComparator = Comparator.comparing(Movie::getRating)
                        .thenComparing(Movie::getTitle);
                if (sortType.equals("desc")) {
                    ratingComparator = ratingComparator.reversed();
                }
                sortedList = filteredMovies.stream().sorted(ratingComparator)
                        .filter(movie -> movie.getRating() > 0)
                        .limit(number).collect(Collectors.toList());
            }
            case "favorite" -> {
                Comparator<Movie> favComparator = Comparator.comparing(Movie::getFavCount)
                        .thenComparing(Movie::getTitle);
                if (sortType.equals("desc")) {
                    favComparator = favComparator.reversed();
                }
                sortedList = filteredMovies.stream().sorted(favComparator).
                        filter(movie -> movie.getFavCount() > 0)
                        .limit(number).collect(Collectors.toList());
            }
            case "longest" -> {
                Comparator<Movie> longestComparator = Comparator.comparing(Movie::getDuration)
                        .thenComparing(Movie::getTitle);
                if (sortType.equals("desc")) {
                    longestComparator = longestComparator.reversed();
                }
                sortedList = filteredMovies.stream().sorted(longestComparator)
                        .limit(number).collect(Collectors.toList());
            }
            case "most_viewed" -> {
                Comparator<Movie> mostViewedComparator = Comparator.comparing(Movie::getViewCount)
                        .thenComparing(Movie::getTitle);
                if (sortType.equals("desc")) {
                    mostViewedComparator = mostViewedComparator.reversed();
                }
                sortedList = filteredMovies.stream().sorted(mostViewedComparator)
                        .filter(movie -> movie.getViewCount() > 0)
                        .limit(number).collect(Collectors.toList());
            }
            default -> {
                return null;
            }
        }

        StringBuilder moviesNameList = new StringBuilder();
        int counter = 0;
        for (Movie movie : sortedList) {
            counter++;
            if (counter == sortedList.size()) {
                moviesNameList.append(movie.getTitle());
                break;
            }

            moviesNameList.append(movie.getTitle()).append(", ");
        }

        return "Query result: [" + moviesNameList + "]";
    }

    /**
     * get the filtered list of shows by only two arguments from the filters list
     * such as years and genres
     * @param shows list of actors
     * @param showsFilters list of filters for actors
     * @return a list of actors that is filtered
     */
    public List<Show> filteredShowsList(List<Show> shows, final List<List<String>> showsFilters) {
        if (showsFilters.get(INDEX_GET_YEAR) != null
                && showsFilters.get(INDEX_GET_YEAR).get(0) != null) {
            shows = shows.stream().filter(show -> {
                if (filters.get(INDEX_GET_YEAR).get(0) == null) {
                    return false;
                } else {
                    return show.getYear() == Integer.parseInt(showsFilters
                            .get(INDEX_GET_YEAR).get(0));
                }
            }).toList();
        }

        if (showsFilters.get(INDEX_GET_GENRE) != null
                && showsFilters.get(INDEX_GET_GENRE).get(0) != null) {
            shows = shows.stream().filter(show -> {
                for (String genre : show.getGenres()) {
                    if (genre.equals(showsFilters.get(INDEX_GET_GENRE).get(0))) {
                        return true;
                    }
                }
                return false;
            }).toList();
        }

        return shows;
    }

    /**
     * the main body of the command which is executed in function of criteria
     * ratings -> for ratings of show
     * favorite -> for no favorites entries in users history
     * longest -> for longest show
     * most_viewed -> for no total views
     * @return
     */
    public String queryShows() {
        List<Show> filteredShows = filteredShowsList(Database.getInstance()
                .getShowsList(), filters);

        List<Show> sortedList;
        switch (criteria) {
            case "ratings" -> {
                Comparator<Show> ratingComparator = Comparator.comparing(Show::getRating)
                        .thenComparing(Show::getTitle);
                if (sortType.equals("desc")) {
                    ratingComparator = ratingComparator.reversed();
                }

                sortedList = filteredShows.stream().sorted(ratingComparator)
                        .filter(show -> show.getRating() > 0)
                        .limit(number).collect(Collectors.toList());
            }
            case "favorite" -> {
                Comparator<Show> favComparator = Comparator.comparing(Show::getFavCount)
                        .thenComparing(Show::getTitle);
                if (sortType.equals("desc")) {
                    favComparator = favComparator.reversed();
                }
                sortedList = filteredShows.stream().sorted(favComparator)
                        .filter(show -> show.getFavCount() > 0)
                        .limit(number).collect(Collectors.toList());
            }
            case "longest" -> {
                Comparator<Show> longestComparator = Comparator.comparing(Show::getDuration)
                        .thenComparing(Show::getTitle);
                if (sortType.equals("desc")) {
                    longestComparator = longestComparator.reversed();
                }
                sortedList = filteredShows.stream().sorted(longestComparator)
                        .limit(number).collect(Collectors.toList());
            }
            case "most_viewed" -> {
                Comparator<Show> mostViewedComparator = Comparator.comparing(Show::getViewCount)
                        .thenComparing(Show::getTitle);
                if (sortType.equals("desc")) {
                    mostViewedComparator = mostViewedComparator.reversed();
                }
                sortedList = filteredShows.stream().sorted(mostViewedComparator)
                        .filter(show -> show.getViewCount() > 0)
                        .limit(number).collect(Collectors.toList());
            }
            default -> {
                return null;
            }
        }

        StringBuilder showNameList = new StringBuilder();
        int counter = 0;
        for (Show show : sortedList) {
            counter++;
            if (counter == sortedList.size()) {
                showNameList.append(show.getTitle());
                break;
            }

            showNameList.append(show.getTitle()).append(", ");
        }

        return "Query result: [" + showNameList + "]";
    }

    /**
     * execution of the query command to a user object
     * by sorting the users in function of noRatingsGiven
     * @return the String that contains the message of the JSONObject
     */
    public String queryUsers() {
        List<User> sortedList = Database.getInstance().getUsersList();
        Comparator<User> noRatingsComparator = Comparator.comparing(User::getNoRatingsGiven)
                .thenComparing(User::getUsername);

        if (sortType.equals("desc")) {
            noRatingsComparator = noRatingsComparator.reversed();
        }

        sortedList = sortedList.stream().sorted(noRatingsComparator)
                .filter(user -> user.getNoRatingsGiven() > 0)
                .limit(number).collect(Collectors.toList());

        StringBuilder usersNameList = new StringBuilder();
        int counter = 0;
        for (User user : sortedList) {
            counter++;
            if (counter == sortedList.size()) {
                usersNameList.append(user.getUsername());
                break;
            }

            usersNameList.append(user.getUsername()).append(", ");
        }

        return "Query result: [" + usersNameList + "]";
    }

    /**
     * the execution of a query command in function of type
     * @return
     */
    @Override
    public JSONObject execute() {
        JSONObject myObj = new JSONObject();
        myObj.put("id", getActionID());
        switch (getType()) {
            case "actors" -> myObj.put("message", this.queryActors());
            case "movies" -> myObj.put("message", this.queryMovies());
            case "shows" -> myObj.put("message", this.queryShows());
            case "users" -> myObj.put("message", this.queryUsers());
            default -> {
                return null;
            }
        }
        return myObj;
    }
}
