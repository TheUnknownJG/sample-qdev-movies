# Movie Service - Spring Boot Demo Application

A simple movie catalog web application built with Spring Boot, demonstrating Java application development best practices with a swashbuckling pirate twist!

## Features

- **Movie Catalog**: Browse 12 classic movies with detailed information
- **Movie Details**: View comprehensive information including director, year, genre, duration, and description
- **🏴‍☠️ Movie Search & Filtering**: Hunt for treasure with our pirate-themed search functionality!
  - Search by movie name (partial matching, case-insensitive)
  - Filter by movie ID (exact match)
  - Filter by genre (partial matching, case-insensitive)
  - Combine multiple search criteria for precise treasure hunting
  - Pirate-themed error messages and success notifications
- **Customer Reviews**: Each movie includes authentic customer reviews with ratings and avatars
- **Responsive Design**: Mobile-first design that works on all devices
- **Modern UI**: Dark theme with gradient backgrounds and smooth animations
- **Pirate-Themed Interface**: Arrr! Search for movies with authentic pirate language and styling

## Technology Stack

- **Java 8**
- **Spring Boot 2.0.5**
- **Maven** for dependency management
- **Log4j 2.20.0**
- **JUnit 5.8.2**
- **Thymeleaf** for server-side templating

## Quick Start

### Prerequisites

- Java 8 or higher
- Maven 3.6+

### Run the Application

```bash
git clone https://github.com/<youruser>/sample-qdev-movies.git
cd sample-qdev-movies
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

### Access the Application

- **Movie List**: http://localhost:8080/movies
- **Movie Search**: http://localhost:8080/movies/search (with query parameters)
- **Movie Details**: http://localhost:8080/movies/{id}/details (where {id} is 1-12)

## Building for Production

```bash
mvn clean package
java -jar target/sample-qdev-movies-0.1.0.jar
```

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/amazonaws/samples/qdevmovies/
│   │       ├── MoviesApplication.java    # Main Spring Boot application
│   │       ├── MoviesController.java     # REST controller for movie endpoints
│   │       ├── MovieService.java         # Business logic with search functionality
│   │       ├── Movie.java                # Movie data model
│   │       ├── Review.java               # Review data model
│   │       └── utils/
│   │           ├── MovieIconUtils.java   # Movie icon utilities
│   │           └── MovieUtils.java       # Movie validation utilities
│   └── resources/
│       ├── application.yml               # Application configuration
│       ├── movies.json                   # Movie data source
│       ├── mock-reviews.json             # Mock review data
│       ├── log4j2.xml                    # Logging configuration
│       └── templates/
│           ├── movies.html               # Movie list with search form
│           └── movie-details.html        # Movie detail page
└── test/                                 # Comprehensive unit tests
    ├── MovieServiceTest.java             # Service layer tests
    └── MoviesControllerTest.java         # Controller tests with search functionality
```

## API Endpoints

### Get All Movies
```
GET /movies
```
Returns an HTML page displaying all movies with ratings, basic information, and a pirate-themed search form.

### Search Movies (🏴‍☠️ New Feature!)
```
GET /movies/search
```
Hunt for movie treasures using various search criteria! Returns an HTML page with filtered results and pirate-themed messages.

**Query Parameters:**
- `name` (optional): Movie name to search for (case-insensitive partial match)
- `id` (optional): Specific movie ID to find (exact match, must be positive integer)
- `genre` (optional): Genre to filter by (case-insensitive partial match)

**Examples:**
```
# Search by movie name
http://localhost:8080/movies/search?name=Prison

# Search by specific ID
http://localhost:8080/movies/search?id=1

# Search by genre
http://localhost:8080/movies/search?genre=Drama

# Combine multiple criteria (Arrr! Advanced treasure hunting!)
http://localhost:8080/movies/search?name=The&genre=Crime

# Search with partial genre match
http://localhost:8080/movies/search?genre=Sci-Fi
```

**Pirate-Themed Responses:**
- **Success**: "Yo ho ho! Found X treasures in our movie chest!"
- **No Results**: "Blimey! No movies found matching yer search, ye landlubber!"
- **Invalid Input**: "Shiver me timbers! That ID be invalid, ye scurvy dog!"
- **No Criteria**: "Arrr! Ye need to provide some search criteria, matey!"

### Get Movie Details
```
GET /movies/{id}/details
```
Returns an HTML page with detailed movie information and customer reviews.

**Parameters:**
- `id` (path parameter): Movie ID (1-12)

**Example:**
```
http://localhost:8080/movies/1/details
```

## Search Functionality Features

### 🏴‍☠️ Pirate-Themed Search Interface
- **Treasure Chest Design**: Search form styled like a pirate treasure chest
- **Pirate Language**: All labels, buttons, and messages use authentic pirate terminology
- **Interactive Elements**: Hover effects and animations for buttons
- **Responsive Design**: Works perfectly on mobile devices

### Advanced Search Capabilities
- **Case-Insensitive Matching**: Search works regardless of capitalization
- **Partial String Matching**: Find movies with partial name or genre matches
- **Multiple Criteria Support**: Combine name, ID, and genre filters
- **Input Validation**: Proper handling of invalid IDs and empty criteria
- **Form State Preservation**: Search criteria are maintained after submission

### Error Handling & Edge Cases
- **Empty Search Results**: Friendly pirate messages when no movies match
- **Invalid Parameters**: Clear error messages for invalid input
- **Missing Criteria**: Helpful guidance when no search terms are provided
- **Service Exceptions**: Graceful handling of unexpected errors
- **Input Sanitization**: Proper trimming and validation of search terms

## Testing

The application includes comprehensive test coverage for all search functionality:

### Running Tests
```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=MovieServiceTest
mvn test -Dtest=MoviesControllerTest
```

### Test Coverage
- **MovieServiceTest**: 20+ test cases covering all search scenarios
- **MoviesControllerTest**: 15+ test cases for controller endpoints
- **Edge Case Testing**: Invalid inputs, empty results, error conditions
- **Pirate Message Testing**: Verification of themed responses

## Troubleshooting

### Port 8080 already in use

Run on a different port:
```bash
mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=8081
```

### Build failures

Clean and rebuild:
```bash
mvn clean compile
```

### Search not working

1. Check that all search parameters are properly URL-encoded
2. Verify movie IDs are positive integers
3. Check application logs for pirate-themed error messages
4. Ensure JavaScript is enabled for form interactions

## Contributing

This project is designed as a demonstration application. Feel free to:
- Add more movies to the catalog
- Enhance the UI/UX with more pirate themes
- Extend search functionality with additional filters
- Improve the responsive design
- Add more pirate language and personality

### Adding New Movies

Edit `/src/main/resources/movies.json` and add new movie objects:
```json
{
  "id": 13,
  "movieName": "New Adventure",
  "director": "New Director",
  "year": 2024,
  "genre": "Adventure",
  "description": "A thrilling new adventure!",
  "duration": 120,
  "imdbRating": 4.5
}
```

## License

This sample code is licensed under the MIT-0 License. See the LICENSE file.

---

**Ahoy matey! 🏴‍☠️ Set sail and discover yer favorite movies with our treasure hunting search feature! Arrr!**
