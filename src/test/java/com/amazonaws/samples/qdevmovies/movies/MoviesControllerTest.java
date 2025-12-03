package com.amazonaws.samples.qdevmovies.movies;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.ui.Model;
import org.springframework.ui.ExtendedModelMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive test suite for MoviesController including search functionality
 * Arrr! These tests be making sure our controller handles all requests like a proper pirate captain!
 */
public class MoviesControllerTest {

    private MoviesController moviesController;
    private Model model;
    private MockMovieService mockMovieService;
    private ReviewService mockReviewService;

    // Test data - our treasure chest of movies!
    private static final Movie TEST_MOVIE_1 = new Movie(1L, "Test Movie", "Test Director", 2023, "Drama", "Test description", 120, 4.5);
    private static final Movie TEST_MOVIE_2 = new Movie(2L, "Action Hero", "Action Director", 2022, "Action", "Action description", 110, 4.0);
    private static final Movie TEST_MOVIE_3 = new Movie(3L, "Comedy Gold", "Comedy Director", 2021, "Comedy", "Funny description", 95, 3.5);

    @BeforeEach
    public void setUp() {
        moviesController = new MoviesController();
        model = new ExtendedModelMap();
        
        // Create mock services with test data
        mockMovieService = new MockMovieService();
        mockReviewService = new MockReviewService();
        
        // Inject mocks using reflection
        try {
            java.lang.reflect.Field movieServiceField = MoviesController.class.getDeclaredField("movieService");
            movieServiceField.setAccessible(true);
            movieServiceField.set(moviesController, mockMovieService);
            
            java.lang.reflect.Field reviewServiceField = MoviesController.class.getDeclaredField("reviewService");
            reviewServiceField.setAccessible(true);
            reviewServiceField.set(moviesController, mockReviewService);
        } catch (Exception e) {
            throw new RuntimeException("Failed to inject mock services", e);
        }
    }

    // Original tests for existing functionality

    @Test
    @DisplayName("Test getMovies returns movies view with search form")
    public void testGetMovies() {
        String result = moviesController.getMovies(model);
        
        assertNotNull(result);
        assertEquals("movies", result);
        assertTrue(model.containsAttribute("movies"));
        assertTrue(model.containsAttribute("showSearchForm"));
        assertTrue(model.containsAttribute("searchPerformed"));
        assertEquals(Boolean.TRUE, model.getAttribute("showSearchForm"));
        assertEquals(Boolean.FALSE, model.getAttribute("searchPerformed"));
    }

    @Test
    @DisplayName("Test getMovieDetails with valid ID")
    public void testGetMovieDetails() {
        String result = moviesController.getMovieDetails(1L, model);
        
        assertNotNull(result);
        assertEquals("movie-details", result);
        assertTrue(model.containsAttribute("movie"));
        assertTrue(model.containsAttribute("movieIcon"));
        assertTrue(model.containsAttribute("allReviews"));
    }

    @Test
    @DisplayName("Test getMovieDetails with invalid ID returns error")
    public void testGetMovieDetailsNotFound() {
        String result = moviesController.getMovieDetails(999L, model);
        
        assertNotNull(result);
        assertEquals("error", result);
        assertTrue(model.containsAttribute("title"));
        assertTrue(model.containsAttribute("message"));
    }

    // New tests for search functionality - Arrr! The treasure hunting tests!

    @Test
    @DisplayName("Test searchMovies with valid name parameter")
    public void testSearchMoviesByName() {
        String result = moviesController.searchMovies("Test", null, null, model);
        
        assertEquals("movies", result);
        assertTrue(model.containsAttribute("movies"));
        assertTrue(model.containsAttribute("showSearchForm"));
        assertTrue(model.containsAttribute("searchPerformed"));
        assertTrue(model.containsAttribute("searchMessage"));
        assertEquals("Test", model.getAttribute("searchName"));
        assertEquals("", model.getAttribute("searchId"));
        assertEquals("", model.getAttribute("searchGenre"));
        
        @SuppressWarnings("unchecked")
        List<Movie> movies = (List<Movie>) model.getAttribute("movies");
        assertEquals(1, movies.size());
        assertEquals("Test Movie", movies.get(0).getMovieName());
    }

    @Test
    @DisplayName("Test searchMovies with valid ID parameter")
    public void testSearchMoviesById() {
        String result = moviesController.searchMovies(null, 2L, null, model);
        
        assertEquals("movies", result);
        assertTrue(model.containsAttribute("searchMessage"));
        assertEquals("", model.getAttribute("searchName"));
        assertEquals("2", model.getAttribute("searchId"));
        assertEquals("", model.getAttribute("searchGenre"));
        
        @SuppressWarnings("unchecked")
        List<Movie> movies = (List<Movie>) model.getAttribute("movies");
        assertEquals(1, movies.size());
        assertEquals("Action Hero", movies.get(0).getMovieName());
    }

    @Test
    @DisplayName("Test searchMovies with no results")
    public void testSearchMoviesNoResults() {
        String result = moviesController.searchMovies("Nonexistent", null, null, model);
        
        assertEquals("movies", result);
        assertTrue(model.containsAttribute("searchMessage"));
        assertFalse(model.containsAttribute("searchError"));
        
        @SuppressWarnings("unchecked")
        List<Movie> movies = (List<Movie>) model.getAttribute("movies");
        assertTrue(movies.isEmpty());
        
        String message = (String) model.getAttribute("searchMessage");
        assertTrue(message.contains("Blimey!"));
        assertTrue(message.contains("landlubber"));
    }

    @Test
    @DisplayName("Test searchMovies with no search criteria")
    public void testSearchMoviesNoCriteria() {
        String result = moviesController.searchMovies(null, null, null, model);
        
        assertEquals("movies", result);
        assertTrue(model.containsAttribute("searchError"));
        assertFalse(model.containsAttribute("searchMessage"));
        
        String error = (String) model.getAttribute("searchError");
        assertTrue(error.contains("Arrr!"));
        assertTrue(error.contains("search criteria"));
        assertTrue(error.contains("matey"));
    }

    @Test
    @DisplayName("Test searchMovies with invalid ID (negative)")
    public void testSearchMoviesInvalidIdNegative() {
        String result = moviesController.searchMovies(null, -1L, null, model);
        
        assertEquals("movies", result);
        assertTrue(model.containsAttribute("searchError"));
        assertFalse(model.containsAttribute("searchMessage"));
        
        String error = (String) model.getAttribute("searchError");
        assertTrue(error.contains("Shiver me timbers!"));
        assertTrue(error.contains("invalid"));
        assertTrue(error.contains("scurvy dog"));
        
        assertEquals("", model.getAttribute("searchId"));
    }

    @Test
    @DisplayName("Test searchMovies success message for single result")
    public void testSearchMoviesSuccessMessageSingle() {
        String result = moviesController.searchMovies("Test", null, null, model);
        
        assertEquals("movies", result);
        assertTrue(model.containsAttribute("searchMessage"));
        
        String message = (String) model.getAttribute("searchMessage");
        assertTrue(message.contains("Yo ho ho!"));
        assertTrue(message.contains("Found 1 treasure"));
    }

    @Test
    public void testMovieServiceIntegration() {
        List<Movie> movies = mockMovieService.getAllMovies();
        assertEquals(3, movies.size());
        assertEquals("Test Movie", movies.get(0).getMovieName());
    }

    // Mock service classes for testing

    private static class MockMovieService extends MovieService {
        private boolean shouldThrowException = false;

        public void setShouldThrowException(boolean shouldThrowException) {
            this.shouldThrowException = shouldThrowException;
        }

        @Override
        public List<Movie> getAllMovies() {
            return Arrays.asList(TEST_MOVIE_1, TEST_MOVIE_2, TEST_MOVIE_3);
        }
        
        @Override
        public Optional<Movie> getMovieById(Long id) {
            if (id == 1L) {
                return Optional.of(TEST_MOVIE_1);
            } else if (id == 2L) {
                return Optional.of(TEST_MOVIE_2);
            } else if (id == 3L) {
                return Optional.of(TEST_MOVIE_3);
            }
            return Optional.empty();
        }

        @Override
        public List<Movie> searchMovies(String name, Long id, String genre) {
            if (shouldThrowException) {
                throw new RuntimeException("Test exception");
            }

            List<Movie> results = new ArrayList<>();
            
            // Simple mock logic for testing
            if (name != null && name.toLowerCase().contains("test")) {
                results.add(TEST_MOVIE_1);
            }
            if (name != null && name.toLowerCase().contains("action")) {
                results.add(TEST_MOVIE_2);
            }
            if (id != null && id == 2L) {
                results.clear();
                results.add(TEST_MOVIE_2);
            }
            if (genre != null && genre.toLowerCase().contains("comedy")) {
                results.clear();
                results.add(TEST_MOVIE_3);
            }
            
            return results;
        }
    }

    private static class MockReviewService extends ReviewService {
        @Override
        public List<Review> getReviewsForMovie(long movieId) {
            return new ArrayList<>();
        }
    }
}
