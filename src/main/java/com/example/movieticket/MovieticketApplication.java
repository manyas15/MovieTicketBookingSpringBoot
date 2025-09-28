package com.example.movieticket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// Main Spring Boot Application Class
// This annotation enables auto-configuration and component scanning
@SpringBootApplication
public class MovieticketApplication {

    // Main method - entry point of the application
    public static void main(String[] args) {
        SpringApplication.run(MovieticketApplication.class, args);
    }

}
