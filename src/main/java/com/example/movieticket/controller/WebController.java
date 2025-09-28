package com.example.movieticket.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    // Home page
    @GetMapping("/")
    public String home() {
        return "index";
    }

    // Movies page
    @GetMapping("/movies")
    public String movies() {
        return "movies";
    }

    // Booking page
    @GetMapping("/booking")
    public String booking() {
        return "booking";
    }

    // View bookings page
    @GetMapping("/my-bookings")
    public String myBookings() {
        return "bookings";
    }
}
