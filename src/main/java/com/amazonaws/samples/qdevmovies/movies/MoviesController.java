package com.amazonaws.samples.qdevmovies.movies;

import com.amazonaws.samples.qdevmovies.utils.MovieIconUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

@Controller
public class MoviesController {
    private static final Logger logger = LogManager.getLogger(MoviesController.class);

    @Autowired
    private MovieService movieService;

    @Autowired
    private ReviewService reviewService;

    @GetMapping("/movies")
    public String getMovies(org.springframework.ui.Model model) {
        logger.info("Fetching movies");
        model.addAttribute("movies", movieService.getAllMovies());
        model.addAttribute("showSearchForm", true);
        model.addAttribute("searchPerformed", false);
        return "movies";
    }

    @GetMapping("/movies/search")
    public String searchMovies(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "id", required = false) Long id,
            @RequestParam(value = "genre", required = false) String genre,
            org.springframework.ui.Model model) {
        
        logger.info("Ahoy! Search request received - name: '{}', id: {}, genre: '{}'", name, id, genre);
        
        try {
            // Validate ID parameter if provided
            if (id != null && id <= 0) {
                logger.warn("Arrr! Invalid movie ID provided: {}", id);
                model.addAttribute("movies", movieService.getAllMovies());
                model.addAttribute("showSearchForm", true);
                model.addAttribute("searchPerformed", true);
                model.addAttribute("searchError", "Shiver me timbers! That ID be invalid, ye scurvy dog! Movie IDs must be greater than 0.");
                model.addAttribute("searchName", name);
                model.addAttribute("searchId", "");
                model.addAttribute("searchGenre", genre);
                return "movies";
            }
            
            // Perform the search
            List<Movie> searchResults = movieService.searchMovies(name, id, genre);
            
            // Prepare model attributes
            model.addAttribute("movies", searchResults);
            model.addAttribute("showSearchForm", true);
            model.addAttribute("searchPerformed", true);
            model.addAttribute("searchName", name != null ? name : "");
            model.addAttribute("searchId", id != null ? id.toString() : "");
            model.addAttribute("searchGenre", genre != null ? genre : "");
            
            // Add pirate-themed messages based on results
            if (searchResults.isEmpty()) {
                // Check if any search criteria was provided
                boolean hasSearchCriteria = (name != null && !name.trim().isEmpty()) ||
                                          id != null ||
                                          (genre != null && !genre.trim().isEmpty());
                
                if (!hasSearchCriteria) {
                    model.addAttribute("searchError", "Arrr! Ye need to provide some search criteria, matey! Fill in at least one field to start yer treasure hunt.");
                } else {
                    model.addAttribute("searchMessage", "Blimey! No movies found matching yer search, ye landlubber! Try different search terms or set sail for new waters.");
                }
            } else {
                String pirateMessage = searchResults.size() == 1 ? 
                    "Yo ho ho! Found 1 treasure in our movie chest!" :
                    "Yo ho ho! Found " + searchResults.size() + " treasures in our movie chest!";
                model.addAttribute("searchMessage", pirateMessage);
            }
            
        } catch (NumberFormatException e) {
            logger.error("Arrr! Invalid number format in search parameters: {}", e.getMessage());
            model.addAttribute("movies", movieService.getAllMovies());
            model.addAttribute("showSearchForm", true);
            model.addAttribute("searchPerformed", true);
            model.addAttribute("searchError", "Batten down the hatches! That ID format be wrong, ye scallywag! Use numbers only.");
            model.addAttribute("searchName", name);
            model.addAttribute("searchId", "");
            model.addAttribute("searchGenre", genre);
        } catch (Exception e) {
            logger.error("Arrr! Unexpected error during movie search: {}", e.getMessage(), e);
            model.addAttribute("movies", movieService.getAllMovies());
            model.addAttribute("showSearchForm", true);
            model.addAttribute("searchPerformed", true);
            model.addAttribute("searchError", "Shiver me timbers! Something went wrong with yer search, matey! Try again or abandon ship!");
            model.addAttribute("searchName", name);
            model.addAttribute("searchId", id != null ? id.toString() : "");
            model.addAttribute("searchGenre", genre);
        }
        
        return "movies";
    }

    @GetMapping("/movies/{id}/details")
    public String getMovieDetails(@PathVariable("id") Long movieId, org.springframework.ui.Model model) {
        logger.info("Fetching details for movie ID: {}", movieId);
        
        Optional<Movie> movieOpt = movieService.getMovieById(movieId);
        if (!movieOpt.isPresent()) {
            logger.warn("Movie with ID {} not found", movieId);
            model.addAttribute("title", "Movie Not Found");
            model.addAttribute("message", "Movie with ID " + movieId + " was not found.");
            return "error";
        }
        
        Movie movie = movieOpt.get();
        model.addAttribute("movie", movie);
        model.addAttribute("movieIcon", MovieIconUtils.getMovieIcon(movie.getMovieName()));
        model.addAttribute("allReviews", reviewService.getReviewsForMovie(movie.getId()));
        
        return "movie-details";
    }
}