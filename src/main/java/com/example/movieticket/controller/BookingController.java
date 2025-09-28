package com.example.movieticket.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.movieticket.model.Booking;
import com.example.movieticket.service.BookingService;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    // Get all bookings - simple approach
    @GetMapping
    public List<Booking> getAllBookings() {
        return bookingService.getAllBookings();
    }

    // Get a specific booking by ID
    @GetMapping("/{id}")
    public Booking getBookingById(@PathVariable int id) {
        return bookingService.findBookingById(id);
    }

    // Book tickets - simple approach
    @PostMapping
    public String bookTickets(@RequestBody BookingRequest request) {
        Booking booking = bookingService.bookTickets(
                request.getMovieId(),
                request.getShowId(),
                request.getSeats(),
                request.getCustomerName()
        );
        return "Booking successful! ID: " + booking.getId();
    }

    // Simple booking request class
    public static class BookingRequest {

        private int movieId;
        private int showId;
        private List<Integer> seats;
        private String customerName;

        // Basic getters and setters
        public int getMovieId() {
            return movieId;
        }

        public void setMovieId(int movieId) {
            this.movieId = movieId;
        }

        public int getShowId() {
            return showId;
        }

        public void setShowId(int showId) {
            this.showId = showId;
        }

        public List<Integer> getSeats() {
            return seats;
        }

        public void setSeats(List<Integer> seats) {
            this.seats = seats;
        }

        public String getCustomerName() {
            return customerName;
        }

        public void setCustomerName(String customerName) {
            this.customerName = customerName;
        }
    }
}
