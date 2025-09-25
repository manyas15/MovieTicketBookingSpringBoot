package com.example.movieticket.model;

import java.util.ArrayList;
import java.util.List;

public class Movie {

    private int id;
    private String title;
    private String genre;
    private int duration; // in minutes
    private List<Show> shows;

    public Movie(int id, String title) {
        this.id = id;
        this.title = title;
        this.genre = "General";
        this.duration = 120;
        this.shows = new ArrayList<>();
    }

    public Movie(int id, String title, String genre, int duration) {
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.duration = duration;
        this.shows = new ArrayList<>();
    }

    // Add a show to this movie
    public void addShow(Show show) {
        shows.add(show);
    }

    // Find a show by ID
    public Show findShowById(int showId) {
        for (Show show : shows) {
            if (show.getId() == showId) {
                return show;
            }
        }
        return null;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public List<Show> getShows() {
        return shows;
    }

    public void setShows(List<Show> shows) {
        this.shows = shows;
    }
}
