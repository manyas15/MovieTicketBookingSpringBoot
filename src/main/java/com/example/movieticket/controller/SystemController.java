package com.example.movieticket.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.movieticket.service.BookingService;
import com.example.movieticket.service.MovieService;

@RestController
@RequestMapping("/api/system")
@CrossOrigin(origins = "*")
public class SystemController {

    @Autowired
    private MovieService movieService;

    @Autowired
    private BookingService bookingService;

    // Get system status and statistics
    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getSystemStatus() {
        try {
            Map<String, Object> status = new HashMap<>();
            status.put("status", "UP");
            status.put("totalMovies", movieService.getMovieCount());
            status.put("totalBookings", bookingService.getTotalBookings());
            status.put("pricePerSeat", bookingService.getPricePerSeat());
            status.put("timestamp", System.currentTimeMillis());

            return ResponseEntity.ok(status);
        } catch (Exception e) {
            Map<String, Object> errorStatus = new HashMap<>();
            errorStatus.put("status", "DOWN");
            errorStatus.put("error", e.getMessage());
            return ResponseEntity.internalServerError().body(errorStatus);
        }
    }

    // Initialize sample data (useful for reset)
    @PostMapping("/init-data")
    public ResponseEntity<String> initializeData() {
        try {
            movieService.loadSampleData();
            return ResponseEntity.ok("Sample data loaded successfully");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Failed to load sample data: " + e.getMessage());
        }
    }

    // Health check endpoint
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> healthCheck() {
        Map<String, String> health = new HashMap<>();
        health.put("status", "healthy");
        health.put("version", "1.0.0");
        health.put("service", "Movie Ticket Booking System");
        return ResponseEntity.ok(health);
    }

    // Get application info
    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> getAppInfo() {
        Map<String, Object> info = new HashMap<>();
        info.put("applicationName", "Movie Ticket Booking System");
        info.put("version", "1.0.0");
        info.put("description", "A Spring Boot application for booking movie tickets");
        info.put("developer", "Your Name");
        info.put("framework", "Spring Boot 3.5.6");
        info.put("javaVersion", System.getProperty("java.version"));

        return ResponseEntity.ok(info);
    }
}
