package com.example.movieticket.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.example.movieticket.model.Movie;

/**
 * Repository class for Movie entity - In-Memory Implementation Provides data
 * access methods for Movie operations Uses HashMap for in-memory storage (no
 * database required)
 */
@Repository
public class MovieRepository {

    private final Map<Integer, Movie> movies = new HashMap<>();

    /**
     * Save a movie to the repository
     *
     * @param movie Movie to save
     * @return Saved movie
     */
    public Movie save(Movie movie) {
        movies.put(movie.getId(), movie);
        return movie;
    }

    /**
     * Find movie by ID
     *
     * @param id Movie ID
     * @return Optional containing the movie if found
     */
    public Optional<Movie> findById(Integer id) {
        return Optional.ofNullable(movies.get(id));
    }

    /**
     * Find all movies
     *
     * @return List of all movies
     */
    public List<Movie> findAll() {
        return new ArrayList<>(movies.values());
    }

    /**
     * Delete movie by ID
     *
     * @param id Movie ID to delete
     */
    public void deleteById(Integer id) {
        movies.remove(id);
    }

    /**
     * Check if movie exists by ID
     *
     * @param id Movie ID
     * @return true if exists, false otherwise
     */
    public boolean existsById(Integer id) {
        return movies.containsKey(id);
    }

    /**
     * Count total movies
     *
     * @return Number of movies
     */
    public long count() {
        return movies.size();
    }

    /**
     * Find movies by genre (case-insensitive)
     *
     * @param genre Movie genre to search for
     * @return List of movies matching the genre
     */
    public List<Movie> findByGenre(String genre) {
        return movies.values().stream()
                .filter(movie -> movie.getGenre().equalsIgnoreCase(genre))
                .collect(Collectors.toList());
    }

    /**
     * Find movies by title containing the search term (case-insensitive)
     *
     * @param title Title search term
     * @return List of movies with matching titles
     */
    public List<Movie> findByTitleContaining(String title) {
        return movies.values().stream()
                .filter(movie -> movie.getTitle().toLowerCase().contains(title.toLowerCase()))
                .collect(Collectors.toList());
    }

    /**
     * Find movies by duration range
     *
     * @param minDuration Minimum duration in minutes
     * @param maxDuration Maximum duration in minutes
     * @return List of movies within the duration range
     */
    public List<Movie> findByDurationBetween(int minDuration, int maxDuration) {
        return movies.values().stream()
                .filter(movie -> movie.getDuration() >= minDuration && movie.getDuration() <= maxDuration)
                .collect(Collectors.toList());
    }

    /**
     * Find all movies ordered by title
     *
     * @return List of all movies sorted by title
     */
    public List<Movie> findAllByOrderByTitle() {
        return movies.values().stream()
                .sorted((m1, m2) -> m1.getTitle().compareToIgnoreCase(m2.getTitle()))
                .collect(Collectors.toList());
    }

    /**
     * Find movies that have shows
     *
     * @return List of movies that have at least one show
     */
    public List<Movie> findMoviesWithShows() {
        return movies.values().stream()
                .filter(movie -> !movie.getShows().isEmpty())
                .collect(Collectors.toList());
    }

    /**
     * Count movies by genre
     *
     * @param genre Genre to count
     * @return Number of movies in the genre
     */
    public long countByGenre(String genre) {
        return movies.values().stream()
                .filter(movie -> movie.getGenre().equalsIgnoreCase(genre))
                .count();
    }

    /**
     * Check if a movie exists by title (case-insensitive)
     *
     * @param title Movie title to check
     * @return true if movie exists, false otherwise
     */
    public boolean existsByTitle(String title) {
        return movies.values().stream()
                .anyMatch(movie -> movie.getTitle().equalsIgnoreCase(title));
    }

    /**
     * Clear all movies (for testing)
     */
    public void deleteAll() {
        movies.clear();
    }
}
