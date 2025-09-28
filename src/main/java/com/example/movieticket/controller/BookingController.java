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

        if (booking != null) {
            return "Booking successful! ID: " + booking.getId()
                    + ". Total Amount: ₹" + String.format("%.2f", booking.getTotalPrice());
        } else {
            return "Booking failed. Please check your details or try different seats.";
        }
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

    // ========================================
    // YOUR CONTRIBUTION: Simple Coupon Features (Beginner-Friendly) 
    // ========================================
    /**
     * YOUR CONTRIBUTION: Book tickets with simple coupon code support
     */
    @PostMapping("/with-coupon")
    public String bookTicketsWithCoupon(@RequestBody BookingRequestWithCoupon request) {
        Booking booking = bookingService.bookTicketsWithCoupon(
                request.getMovieId(),
                request.getShowId(),
                request.getSeats(),
                request.getCustomerName(),
                request.getCouponCode()
        );

        if (booking != null) {
            return "Booking successful with ID: " + booking.getId()
                    + ". Total Amount: ₹" + String.format("%.2f", booking.getTotalPrice());
        } else {
            return "Booking failed. Please check your details.";
        }
    }

    /**
     * YOUR CONTRIBUTION: Get available discount coupons (simple feature)
     */
    @GetMapping("/coupons")
    public java.util.Map<String, Double> getAvailableCoupons() {
        return bookingService.getAvailableCoupons();
    }

    /**
     * YOUR CONTRIBUTION: Check if coupon is valid (beginner validation)
     */
    @GetMapping("/coupons/{couponCode}/validate")
    public String validateCoupon(@PathVariable String couponCode) {
        if (bookingService.isValidCoupon(couponCode)) {
            return "Coupon " + couponCode + " is valid!";
        } else {
            return "Invalid coupon code: " + couponCode;
        }
    }

    /**
     * YOUR CONTRIBUTION: Simple booking statistics
     */
    @GetMapping("/stats")
    public String getBookingStats() {
        int totalBookings = bookingService.getTotalBookings();
        return "Total bookings made: " + totalBookings;
    }

    // YOUR CONTRIBUTION: Simple booking request with coupon support
    public static class BookingRequestWithCoupon {

        private int movieId;
        private int showId;
        private List<Integer> seats;
        private String customerName;
        private String couponCode;

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

        public String getCouponCode() {
            return couponCode;
        }

        public void setCouponCode(String couponCode) {
            this.couponCode = couponCode;
        }
    }
}
