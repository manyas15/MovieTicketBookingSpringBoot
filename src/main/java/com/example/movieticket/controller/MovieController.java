package com.example.movieticket.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.movieticket.model.Movie;
import com.example.movieticket.model.Show;
import com.example.movieticket.service.MovieService;

@RestController
@RequestMapping("/api/movies")
@CrossOrigin(origins = "*") // For frontend integration
public class MovieController {

    @Autowired
    private MovieService movieService;

    // Get all movies
    @GetMapping
    public ResponseEntity<List<Movie>> getAllMovies() {
        try {
            List<Movie> movies = movieService.getAllMovies();
            return ResponseEntity.ok(movies);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // Get a specific movie by ID
    @GetMapping("/{id}")
    public ResponseEntity<Movie> getMovieById(@PathVariable int id) {
        try {
            Movie movie = movieService.findMovieById(id);
            if (movie != null) {
                return ResponseEntity.ok(movie);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // Get all shows for a specific movie
    @GetMapping("/{movieId}/shows")
    public ResponseEntity<List<Show>> getShowsForMovie(@PathVariable int movieId) {
        try {
            Movie movie = movieService.findMovieById(movieId);
            if (movie != null) {
                return ResponseEntity.ok(movie.getShows());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // Get a specific show for a movie
    @GetMapping("/{movieId}/shows/{showId}")
    public ResponseEntity<Show> getShow(@PathVariable int movieId, @PathVariable int showId) {
        try {
            Show show = movieService.findShow(movieId, showId);
            if (show != null) {
                return ResponseEntity.ok(show);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // Get available seats for a show
    @GetMapping("/{movieId}/shows/{showId}/seats")
    public ResponseEntity<List<Integer>> getAvailableSeats(@PathVariable int movieId, @PathVariable int showId) {
        try {
            Show show = movieService.findShow(movieId, showId);
            if (show != null) {
                return ResponseEntity.ok(show.getAvailableSeats());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
