package com.amazonaws.samples.qdevmovies.movies;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive test suite for MovieService search functionality
 * Arrr! These tests be making sure our treasure hunting methods work ship-shape!
 */
public class MovieServiceTest {

    private MovieService movieService;

    @BeforeEach
    public void setUp() {
        movieService = new MovieService();
    }

    @Test
    @DisplayName("Test getAllMovies returns all movies from JSON")
    public void testGetAllMovies() {
        List<Movie> movies = movieService.getAllMovies();
        assertNotNull(movies, "Movies list should not be null");
        assertEquals(12, movies.size(), "Should load 12 movies from JSON file");
    }

    @Test
    @DisplayName("Test getMovieById with valid ID")
    public void testGetMovieByIdValid() {
        Optional<Movie> movie = movieService.getMovieById(1L);
        assertTrue(movie.isPresent(), "Movie with ID 1 should exist");
        assertEquals("The Prison Escape", movie.get().getMovieName());
        assertEquals("John Director", movie.get().getDirector());
    }

    @Test
    @DisplayName("Test getMovieById with invalid ID")
    public void testGetMovieByIdInvalid() {
        Optional<Movie> movie = movieService.getMovieById(999L);
        assertFalse(movie.isPresent(), "Movie with ID 999 should not exist");
    }

    @Test
    @DisplayName("Test getMovieById with null ID")
    public void testGetMovieByIdNull() {
        Optional<Movie> movie = movieService.getMovieById(null);
        assertFalse(movie.isPresent(), "Movie with null ID should not exist");
    }

    @Test
    @DisplayName("Test getMovieById with zero or negative ID")
    public void testGetMovieByIdZeroOrNegative() {
        Optional<Movie> movie1 = movieService.getMovieById(0L);
        Optional<Movie> movie2 = movieService.getMovieById(-1L);
        assertFalse(movie1.isPresent(), "Movie with ID 0 should not exist");
        assertFalse(movie2.isPresent(), "Movie with negative ID should not exist");
    }

    // Search functionality tests - Arrr! The treasure hunting begins!

    @Test
    @DisplayName("Test searchMovies with movie name - exact match")
    public void testSearchMoviesByNameExact() {
        List<Movie> results = movieService.searchMovies("The Prison Escape", null, null);
        assertEquals(1, results.size(), "Should find exactly one movie");
        assertEquals("The Prison Escape", results.get(0).getMovieName());
    }

    @Test
    @DisplayName("Test searchMovies with movie name - partial match")
    public void testSearchMoviesByNamePartial() {
        List<Movie> results = movieService.searchMovies("Prison", null, null);
        assertEquals(1, results.size(), "Should find movie with 'Prison' in name");
        assertEquals("The Prison Escape", results.get(0).getMovieName());
    }

    @Test
    @DisplayName("Test searchMovies with movie name - case insensitive")
    public void testSearchMoviesByNameCaseInsensitive() {
        List<Movie> results = movieService.searchMovies("prison escape", null, null);
        assertEquals(1, results.size(), "Should find movie regardless of case");
        assertEquals("The Prison Escape", results.get(0).getMovieName());
    }

    @Test
    @DisplayName("Test searchMovies with movie name - no matches")
    public void testSearchMoviesByNameNoMatch() {
        List<Movie> results = movieService.searchMovies("Nonexistent Movie", null, null);
        assertTrue(results.isEmpty(), "Should return empty list for non-existent movie");
    }

    @Test
    @DisplayName("Test searchMovies by ID only")
    public void testSearchMoviesById() {
        List<Movie> results = movieService.searchMovies(null, 2L, null);
        assertEquals(1, results.size(), "Should find exactly one movie by ID");
        assertEquals("The Family Boss", results.get(0).getMovieName());
        assertEquals(2L, results.get(0).getId());
    }

    @Test
    @DisplayName("Test searchMovies by invalid ID")
    public void testSearchMoviesByInvalidId() {
        List<Movie> results = movieService.searchMovies(null, 999L, null);
        assertTrue(results.isEmpty(), "Should return empty list for invalid ID");
    }

    @Test
    @DisplayName("Test searchMovies by genre - exact match")
    public void testSearchMoviesByGenreExact() {
        List<Movie> results = movieService.searchMovies(null, null, "Drama");
        assertFalse(results.isEmpty(), "Should find movies with Drama genre");
        assertTrue(results.stream().anyMatch(m -> m.getGenre().contains("Drama")));
    }

    @Test
    @DisplayName("Test searchMovies by genre - partial match")
    public void testSearchMoviesByGenrePartial() {
        List<Movie> results = movieService.searchMovies(null, null, "Crime");
        assertFalse(results.isEmpty(), "Should find movies with Crime in genre");
        assertTrue(results.stream().allMatch(m -> m.getGenre().toLowerCase().contains("crime")));
    }

    @Test
    @DisplayName("Test searchMovies by genre - case insensitive")
    public void testSearchMoviesByGenreCaseInsensitive() {
        List<Movie> results = movieService.searchMovies(null, null, "action");
        assertFalse(results.isEmpty(), "Should find movies regardless of case");
        assertTrue(results.stream().allMatch(m -> m.getGenre().toLowerCase().contains("action")));
    }

    @Test
    @DisplayName("Test searchMovies with multiple criteria - name and genre")
    public void testSearchMoviesMultipleCriteria() {
        List<Movie> results = movieService.searchMovies("The", null, "Drama");
        assertFalse(results.isEmpty(), "Should find movies matching both name and genre");
        assertTrue(results.stream().allMatch(m -> 
            m.getMovieName().toLowerCase().contains("the") && 
            m.getGenre().toLowerCase().contains("drama")));
    }

    @Test
    @DisplayName("Test searchMovies with all criteria")
    public void testSearchMoviesAllCriteria() {
        List<Movie> results = movieService.searchMovies("The Prison Escape", 1L, "Drama");
        assertEquals(1, results.size(), "Should find exactly one movie matching all criteria");
        Movie movie = results.get(0);
        assertEquals("The Prison Escape", movie.getMovieName());
        assertEquals(1L, movie.getId());
        assertEquals("Drama", movie.getGenre());
    }

    @Test
    @DisplayName("Test searchMovies with conflicting criteria")
    public void testSearchMoviesConflictingCriteria() {
        List<Movie> results = movieService.searchMovies("The Prison Escape", 2L, "Drama");
        assertTrue(results.isEmpty(), "Should return empty list when criteria conflict");
    }

    @Test
    @DisplayName("Test searchMovies with empty/null criteria")
    public void testSearchMoviesEmptyCriteria() {
        List<Movie> results1 = movieService.searchMovies(null, null, null);
        List<Movie> results2 = movieService.searchMovies("", null, "");
        List<Movie> results3 = movieService.searchMovies("   ", null, "   ");
        
        assertTrue(results1.isEmpty(), "Should return empty list for all null criteria");
        assertTrue(results2.isEmpty(), "Should return empty list for empty string criteria");
        assertTrue(results3.isEmpty(), "Should return empty list for whitespace-only criteria");
    }

    @Test
    @DisplayName("Test searchMoviesByName convenience method")
    public void testSearchMoviesByNameConvenience() {
        List<Movie> results = movieService.searchMoviesByName("Family");
        assertEquals(1, results.size(), "Should find movie with 'Family' in name");
        assertEquals("The Family Boss", results.get(0).getMovieName());
    }

    @Test
    @DisplayName("Test searchMoviesByName with null/empty input")
    public void testSearchMoviesByNameNullEmpty() {
        List<Movie> results1 = movieService.searchMoviesByName(null);
        List<Movie> results2 = movieService.searchMoviesByName("");
        List<Movie> results3 = movieService.searchMoviesByName("   ");
        
        assertTrue(results1.isEmpty(), "Should return empty list for null name");
        assertTrue(results2.isEmpty(), "Should return empty list for empty name");
        assertTrue(results3.isEmpty(), "Should return empty list for whitespace name");
    }

    @Test
    @DisplayName("Test searchMoviesByGenre convenience method")
    public void testSearchMoviesByGenreConvenience() {
        List<Movie> results = movieService.searchMoviesByGenre("Sci-Fi");
        assertFalse(results.isEmpty(), "Should find movies with Sci-Fi genre");
        assertTrue(results.stream().allMatch(m -> m.getGenre().toLowerCase().contains("sci-fi")));
    }

    @Test
    @DisplayName("Test searchMoviesByGenre with null/empty input")
    public void testSearchMoviesByGenreNullEmpty() {
        List<Movie> results1 = movieService.searchMoviesByGenre(null);
        List<Movie> results2 = movieService.searchMoviesByGenre("");
        List<Movie> results3 = movieService.searchMoviesByGenre("   ");
        
        assertTrue(results1.isEmpty(), "Should return empty list for null genre");
        assertTrue(results2.isEmpty(), "Should return empty list for empty genre");
        assertTrue(results3.isEmpty(), "Should return empty list for whitespace genre");
    }

    @Test
    @DisplayName("Test search results maintain movie data integrity")
    public void testSearchResultsDataIntegrity() {
        List<Movie> results = movieService.searchMovies("The", null, null);
        assertFalse(results.isEmpty(), "Should find movies with 'The' in name");
        
        for (Movie movie : results) {
            assertNotNull(movie.getMovieName(), "Movie name should not be null");
            assertNotNull(movie.getDirector(), "Director should not be null");
            assertNotNull(movie.getGenre(), "Genre should not be null");
            assertTrue(movie.getId() > 0, "Movie ID should be positive");
            assertTrue(movie.getYear() > 0, "Year should be positive");
            assertTrue(movie.getDuration() > 0, "Duration should be positive");
            assertTrue(movie.getImdbRating() >= 0, "IMDB rating should be non-negative");
        }
    }
}