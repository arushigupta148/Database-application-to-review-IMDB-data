# Database-application-to-review-IMDB-data
Created an application containing dynamic components which were updated continuously as users made choices, allowing them to modify results interactively

populate.java: The file has INSERT statements for tables to insert data into the Database

createdb.sql: The file creates all required tables and includes constraints, indexes, and other DDL statements

dropdb.sql: The file drops all tables and other objects created by createdb.sql file

main.java: The file establishes connectivity with the DBMS, executes SQL queries to search the database, retrieves query results and parses the returned results to generate the output on the GUI

## The GUI displays:
a. List of movie genres

b. Countries where the movies are produced

c. Filming location country where movies are filmed

d. Critic’s rating which is Rotten Tomato all critics rating

e. No. of Reviews, which is the Rotten Tomatoes all critics' number of reviews

f. Movie year

g. Movie tags values

h. List of results

## Data statistics
The dataset includes 10 types of data objects: movies, movie_genres, movie_directors, movie_actors, movie_countries, movie_locations, tags, movie_tags, user_taggedmovies, and user_ratedmovies.
2113 users
10197 movies
20 movie genres
20809 movie genre assignments
4060 directors
95321 actors
72 countries
10197 country assignments
47899 location assignments
13222 tags
47957 tag assignments
855598 ratings
