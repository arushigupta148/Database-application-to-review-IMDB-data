CREATE TABLE MOVIES(id Integer NOT NULL, title varchar2(500), imdbID Integer, spanishTitle varchar2(500), imdbPictureURL varchar2(500), year Integer, rtID varchar2(500), rtAllCriticsRating float, rtAllCriticsNumReviews Integer, rtAllCriticsNumFresh Integer, rtAllCriticsNumRotten Integer, rtAllCriticsScore Integer, rtTopCriticsRating float, rtTopCriticsNumReviews Integer, rtTopCriticsNumFresh Integer, rtTopCriticsNumRotten Integer, rtTopCriticsScore Integer, rtAudienceRating float, rtAudienceNumRatings Integer, rtAudienceScore Integer, rtPictureURL varchar2(500), PRIMARY KEY(id) );

CREATE TABLE MOVIE_ACTORS (movieID Integer references MOVIES(id), actorID varchar2(500), actorName varchar2(500), ranking Integer);

CREATE TABLE MOVIE_COUNTRIES (movieID Integer references MOVIES(id), country varchar2(500) );

CREATE TABLE MOVIE_DIRECTORS (movieID Integer references MOVIES(id), directorID varchar2(500), directorName varchar2(500) );

CREATE TABLE MOVIE_GENRES (movieID Integer references MOVIES(id), genre varchar2(500));

CREATE TABLE MOVIE_LOCATIONS (movieID Integer references MOVIES(id), location1 varchar2(500), location2 varchar2(500), location3 varchar2(500), location4 varchar2(500) );

CREATE TABLE TAGS (id Integer PRIMARY KEY, value varchar2(500));

CREATE TABLE USER_TAGGEDMOVIES (userID Integer, movieID Integer references MOVIES(id), tagID Integer references TAGS(id), PRIMARY KEY (userID, movieID, tagID) );

CREATE TABLE MOVIE_TAGS (movieID Integer references MOVIES(id), tagID Integer references TAGS(id), tagWeight Integer, PRIMARY KEY(movieID, tagID, tagWeight));

CREATE TABLE USER_RATEDMOVIES (userID Integer, movieID Integer references MOVIES(id), rating float, PRIMARY KEY (userID, movieID) );

CREATE TABLE USER_RATEDMOVIESTIMESTAMPS (userID Integer, movieID Integer references MOVIES(id), rating float, timestamp varchar2(500), PRIMARY KEY(userID, movieID));

CREATE TABLE USER_TAGGEDMOVIESTIMESTAMPS (userID Integer, movieID Integer references MOVIES(id), tagID Integer references TAGS(id), timestamp varchar2(500), PRIMARY KEY(userID, movieID, tagID));