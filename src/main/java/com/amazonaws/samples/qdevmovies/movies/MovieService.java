package com.amazonaws.samples.qdevmovies.movies;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

@Service
public class MovieService {
    private static final Logger logger = LogManager.getLogger(MovieService.class);
    private final List<Movie> movies;
    private final Map<Long, Movie> movieMap;

    public MovieService() {
        this.movies = loadMoviesFromJson();
        this.movieMap = new HashMap<>();
        for (Movie movie : movies) {
            movieMap.put(movie.getId(), movie);
        }
    }

    private List<Movie> loadMoviesFromJson() {
        List<Movie> movieList = new ArrayList<>();
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("movies.json");
            if (inputStream != null) {
                Scanner scanner = new Scanner(inputStream, StandardCharsets.UTF_8.name());
                String jsonContent = scanner.useDelimiter("\\A").next();
                scanner.close();
                
                JSONArray moviesArray = new JSONArray(jsonContent);
                for (int i = 0; i < moviesArray.length(); i++) {
                    JSONObject movieObj = moviesArray.getJSONObject(i);
                    movieList.add(new Movie(
                        movieObj.getLong("id"),
                        movieObj.getString("movieName"),
                        movieObj.getString("director"),
                        movieObj.getInt("year"),
                        movieObj.getString("genre"),
                        movieObj.getString("description"),
                        movieObj.getInt("duration"),
                        movieObj.getDouble("imdbRating")
                    ));
                }
            }
        } catch (Exception e) {
            logger.error("Failed to load movies from JSON: {}", e.getMessage());
        }
        return movieList;
    }

    public List<Movie> getAllMovies() {
        return movies;
    }

    public Optional<Movie> getMovieById(Long id) {
        if (id == null || id <= 0) {
            return Optional.empty();
        }
        return Optional.ofNullable(movieMap.get(id));
    }

    /**
     * Searches for movies based on provided criteria with pirate flair!
     * Arrr! This method be the treasure map to finding yer favorite films, matey!
     * 
     * @param name Movie name to search for (case-insensitive partial match)
     * @param id Specific movie ID to find
     * @param genre Genre to filter by (case-insensitive partial match)
     * @return List of movies matching the search criteria
     */
    public List<Movie> searchMovies(String name, Long id, String genre) {
        logger.info("Ahoy! Starting treasure hunt for movies with criteria - name: '{}', id: {}, genre: '{}'", 
                   name, id, genre);
        
        List<Movie> searchResults = new ArrayList<>();
        
        // If no search criteria provided, return empty list with a pirate message
        if ((name == null || name.trim().isEmpty()) && 
            id == null && 
            (genre == null || genre.trim().isEmpty())) {
            logger.warn("Arrr! No search criteria provided, ye scurvy dog! Returning empty treasure chest.");
            return searchResults;
        }
        
        // Start with all movies and filter down
        for (Movie movie : movies) {
            boolean matches = true;
            
            // Filter by ID if provided (exact match)
            if (id != null) {
                matches = matches && movie.getId() == id.longValue();
            }
            
            // Filter by name if provided (case-insensitive partial match)
            if (name != null && !name.trim().isEmpty()) {
                matches = matches && movie.getMovieName().toLowerCase()
                    .contains(name.trim().toLowerCase());
            }
            
            // Filter by genre if provided (case-insensitive partial match)
            if (genre != null && !genre.trim().isEmpty()) {
                matches = matches && movie.getGenre().toLowerCase()
                    .contains(genre.trim().toLowerCase());
            }
            
            if (matches) {
                searchResults.add(movie);
            }
        }
        
        if (searchResults.isEmpty()) {
            logger.info("Shiver me timbers! No movies found matching yer search criteria. The treasure chest be empty, matey!");
        } else {
            logger.info("Yo ho ho! Found {} movies in our treasure hunt!", searchResults.size());
        }
        
        return searchResults;
    }

    /**
     * Searches for movies by name only - a simpler treasure hunt!
     * 
     * @param movieName Name to search for (case-insensitive partial match)
     * @return List of movies with names matching the search term
     */
    public List<Movie> searchMoviesByName(String movieName) {
        if (movieName == null || movieName.trim().isEmpty()) {
            logger.warn("Arrr! Empty movie name provided for search, ye landlubber!");
            return new ArrayList<>();
        }
        
        return searchMovies(movieName, null, null);
    }

    /**
     * Searches for movies by genre only - find all films of a particular type!
     * 
     * @param genreType Genre to search for (case-insensitive partial match)
     * @return List of movies matching the genre
     */
    public List<Movie> searchMoviesByGenre(String genreType) {
        if (genreType == null || genreType.trim().isEmpty()) {
            logger.warn("Arrr! Empty genre provided for search, ye scallywag!");
            return new ArrayList<>();
        }
        
        return searchMovies(null, null, genreType);
    }
}
