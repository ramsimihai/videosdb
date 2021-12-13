# 321CA - Mihai Daniel Soare
# Object Oriented Programming Course

Homework 1 - VideosDB

warning: i hate uppercases so i wont use them too much

November 2021
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

* Video Platform & Database
    - the program is an implementation of a video platform which can store videos of different types
    actors that played in those, users and do operations on them;

__Classes__

- Database -> implemented with lazy instantiation a Singleton that is going to be used
            in all the platform and is going to be flushed after the use of it (in our case on
            a test)
            -> contains lists of Actors, Movies, Shows, Videos (abstract class), Users, a Commander
            control panel and genreMap;

- User -> abstract class that is going to be inherited by two types of users to use same methods;
       -> STANDARD -> can do only 2 actions;
       -> PREMIUM -> can do the default action+ 3 more actions;

- Actor

- Video -> abstract class that is going to be inherited by two types of videos Movies and TV Shows
           to use the same methods on children;
        -> Movie -> inherits Video;
        -> Show -> inherits Video;

- Commander -> a commander class where the actions will be extracted from an input JSON parsed List
               and then be executed in function of different types of actions;

- Action -> abstract class that is going to be inherited by three types of command such as Query
           Basic Command and Recommendation
         -> Basic Command
         -> Recommendation
         -> Query (on 3 differents objects)

~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

* How to use the program

__Entry Point__

- Database is instatiated firstly in `Main`, then the input of the Database is imported from
<InputLoader> to the lists of different types in the Database;

- The Actions which are going to be done to the lists from the Database are put in a Commander
and then are executed one by one;

__Tricks__

- I've been using a lot of streams (from API stream) which can transform lists / maps into streams
which are a lot easier to be filtered, compared, sorted and collected;
- I've been using Commander class so I could execute the actions gotten from every user;
- I have done some abstract classes like (User, Video, Action) so i could reuse the methods from
the parent class;
- I have done a lot of comparators so I could use the `stream.filter()` method at its finest

__Commands_

- There are 3 type of commands: Queries, Recommendations & Commands

1.1) Commands

- are the most basic one of the other 3;
- can be:
    *) Favorite -> adds a video in the list of favourites videos of a user
                -> checks if the video was seen
                -> updates the favouriteCount of the video / user
    *) View
                -> add a video in the list of viewed videos of a user
                -> checks if the video was already seen
                -> updates the viewedCount of the video / user
    *) Rate
                -> rate a video in function of what type of video is (movie, show)
                -> increments the number of ratings given by a user

1.2) Queries

- these are operations that doesnt affect the actual Database;
- the queries can be done on 4 differents objects such as Actors, Users and Videos:

    *) Actors -> on actors can be done other 3 queries:
             **) Average -> get the average rating in function of what movies has the actor
                            played in and get rating for every video;
             **) Awards  -> get the list of actors that has the specified awards
             **) Filter Description -> get the list of actors that contains specified words
                                       in their Career Description, do this by using a REGEX filter

    *) Videos -> on videos can be done 4 different sorting in function of some filters
            **) Rating
            **) Favourite
            **) Longest
            **) Most Viewed
            - mostly all of them are implemented in the same way, we use a strem to filter the list
            of objects by two different possible filters (year & genre), sort it by a comparator
            which is intuitive (like noRatingsGiven by an user or duration of every video)
            - then the limit is restricted in function of a number;

    *) Users -> get the users in function of no_ratings already calculatedi in the program

1.3) Recommendations

    **) really intuitive, get the videos which are not seen by a user then do some operations in
    function of a pattern;
        - the part where a genreMap is sorted ("not quite" was a little harder);
        - sort the videos after genre
        - then iterate through the list and get the video

~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

* Feedback

- this homework was done in 30 hours with 5h/sleep per night. it wasnt really so cool of you to
make a soft deadline, there is no point in doing that. Better do a hard deadline with a not so long
period so that people wouldnt be descouraged in doing it anymore...
ALSO: for real, put the next homework when the first one hasnt been finished yet?
- I would really like to do more about this homework, change some things (a lot of code is
duplicated and I would really love to have more time to new tricks so there wouldn't be that case)
- I had to search about a lot of things which havent been done yet in the laboratory (is that really
alright?)
- I really should've implement a find user by name in the list, but trust me I am so done with this
and really tired;
- TO get to do the homework you had to understand things from the tests and make an implementation
out of it instead of what the task on OCW was saying, make it more clear for the people
to understand and I sure hope next year will be a different one;

~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
