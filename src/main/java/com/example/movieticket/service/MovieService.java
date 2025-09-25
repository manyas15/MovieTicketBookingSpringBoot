package com.example.movieticket.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.movieticket.model.Movie;
import com.example.movieticket.model.Show;

@Service
public class MovieService {
    private Map<Integer, Movie> movies;
    private int nextMovieId;
    private int nextShowId;

    public MovieService() {
        this.movies = new HashMap<>();
        this.nextMovieId = 1;
        this.nextShowId = 1;
    }

    // Load some sample movies and shows for testing
    public void loadSampleData() {
        // Movie 1: The Matrix Resurrections
        Movie movie1 = new Movie(nextMovieId++, "The Matrix Resurrections", "Sci-Fi", 148);
        movie1.addShow(new Show(nextShowId++, "2:00 PM", 15));
        movie1.addShow(new Show(nextShowId++, "5:00 PM", 15));
        movie1.addShow(new Show(nextShowId++, "8:00 PM", 15));
        movies.put(movie1.getId(), movie1);

        // Movie 2: Inception
        Movie movie2 = new Movie(nextMovieId++, "Inception", "Thriller", 148);
        movie2.addShow(new Show(nextShowId++, "3:00 PM", 20));
        movie2.addShow(new Show(nextShowId++, "6:00 PM", 20));
        movies.put(movie2.getId(), movie2);

        // Movie 3: Avengers Endgame
        Movie movie3 = new Movie(nextMovieId++, "Avengers Endgame", "Action", 181);
        movie3.addShow(new Show(nextShowId++, "1:00 PM", 25));
        movie3.addShow(new Show(nextShowId++, "7:00 PM", 25));
        movies.put(movie3.getId(), movie3);
    }

    // Get all movies
    public List<Movie> getAllMovies() {
        return new ArrayList<>(movies.values());
    }

    // Find a movie by ID
    public Movie findMovieById(int movieId) {
        return movies.get(movieId);
    }

    // Find a show by movie ID and show ID
    public Show findShow(int movieId, int showId) {
        Movie movie = findMovieById(movieId);
        if (movie != null) {
            return movie.findShowById(showId);
        }
        return null;
    }

    // Add a new movie
    public void addMovie(Movie movie) {
        movie.setId(nextMovieId++);
        movies.put(movie.getId(), movie);
    }

    // Add a show to an existing movie
    public boolean addShowToMovie(int movieId, String showTime, int totalSeats) {
        Movie movie = findMovieById(movieId);
        if (movie != null) {
            Show show = new Show(nextShowId++, showTime, totalSeats);
            movie.addShow(show);
            return true;
        }
        return false;
    }

    // Check if movie exists
    public boolean movieExists(int movieId) {
        return movies.containsKey(movieId);
    }

    // Get movie count
    public int getMovieCount() {
        return movies.size();
    }
}
