package com.example.movieticket.runner;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.movieticket.service.BookingService;
import com.example.movieticket.service.MovieService;

@Component
public class AppRunner implements CommandLineRunner {

    private MovieService movieService;
    private BookingService bookingService;

    public AppRunner(MovieService movieService, BookingService bookingService) {
        this.movieService = movieService;
        this.bookingService = bookingService;
    }

    @Override
    public void run(String... args) {
        // Load sample data when the application starts
        movieService.loadSampleData();

        System.out.println("Movie Ticket Booking System started successfully!");
        System.out.println("Visit: http://localhost:8080");
    }
}
