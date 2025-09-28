package com.example.movieticket.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.movieticket.model.Booking;
import com.example.movieticket.model.Movie;
import com.example.movieticket.model.Show;

@Service
public class BookingService {

    private Map<Integer, Booking> bookings;
    private int nextBookingId;
    private double pricePerSeat = 500.0; // ₹500 per seat
    private MovieService movieService;

    // Simple Features - YOUR BEGINNER CONTRIBUTION
    private Map<String, Double> simpleCoupons; // Basic discount system
    private int totalBookingsCount; // Simple counter for statistics

    public BookingService(MovieService movieService) {
        this.movieService = movieService;
        this.bookings = new HashMap<>();
        this.nextBookingId = 1;

        // Initialize simple features - YOUR BEGINNER CONTRIBUTION
        initializeSimpleCoupons();
        this.totalBookingsCount = 0;
    }

    // YOUR CONTRIBUTION: Simple discount coupons for beginners
    private void initializeSimpleCoupons() {
        this.simpleCoupons = new HashMap<>();
        simpleCoupons.put("STUDENT10", 10.0);   // 10% student discount
        simpleCoupons.put("WEEKEND20", 20.0);   // 20% weekend special  
        simpleCoupons.put("FAMILY15", 15.0);    // 15% family discount
    }

    // Simple ticket booking method - Basic Spring Boot concept
    public Booking bookTickets(int movieId, int showId, List<Integer> seatNumbers, String customerName) {
        return bookTicketsWithCoupon(movieId, showId, seatNumbers, customerName, null);
    }

    // YOUR CONTRIBUTION: Simple coupon booking system for beginners
    public Booking bookTicketsWithCoupon(int movieId, int showId, List<Integer> seatNumbers,
            String customerName, String couponCode) {
        // Find the movie
        Movie movie = movieService.findMovieById(movieId);
        if (movie == null) {
            return null; // Movie not found
        }

        // Find the show
        Show show = movie.findShowById(showId);
        if (show == null) {
            return null; // Show not found
        }

        // Check if all requested seats are available
        for (int seatNumber : seatNumbers) {
            if (!show.isSeatAvailable(seatNumber)) {
                return null; // Seat not available
            }
        }

        // Book all the seats
        for (int seatNumber : seatNumbers) {
            show.bookSeat(seatNumber);
        }

        // YOUR CONTRIBUTION: Simple price calculation with discount
        double totalPrice = calculateSimplePrice(seatNumbers.size(), couponCode);

        // Create the booking
        Booking booking = new Booking(nextBookingId++, movieId, movie.getTitle(),
                showId, show.getShowTime(), new ArrayList<>(seatNumbers),
                totalPrice, customerName);

        // Save the booking and update counter
        bookings.put(booking.getId(), booking);
        totalBookingsCount++;

        return booking;
    }

    // Cancel a booking
    public boolean cancelBooking(int bookingId) {
        Booking booking = bookings.get(bookingId);
        if (booking == null) {
            return false; // Booking not found
        }

        // Find the show and free up the seats
        Show show = movieService.findShow(booking.getMovieId(), booking.getShowId());
        if (show != null) {
            for (int seatNumber : booking.getSeats()) {
                show.cancelSeat(seatNumber);
            }
        }

        // Remove the booking
        bookings.remove(bookingId);
        return true;
    }

    // Get all bookings
    public List<Booking> getAllBookings() {
        return new ArrayList<>(bookings.values());
    }

    // Find booking by ID
    public Booking findBookingById(int bookingId) {
        return bookings.get(bookingId);
    }

    // Get bookings for a specific customer
    public List<Booking> getBookingsByCustomer(String customerName) {
        List<Booking> customerBookings = new ArrayList<>();
        for (Booking booking : bookings.values()) {
            if (booking.getCustomerName().equalsIgnoreCase(customerName)) {
                customerBookings.add(booking);
            }
        }
        return customerBookings;
    }

    // Check if booking exists
    public boolean bookingExists(int bookingId) {
        return bookings.containsKey(bookingId);
    }

    // Set price per seat
    public void setPricePerSeat(double price) {
        this.pricePerSeat = price;
    }

    // Get price per seat
    public double getPricePerSeat() {
        return pricePerSeat;
    }

    // ========================================
    // YOUR CONTRIBUTION: Simple Fee Management (Beginner-Friendly)
    // ========================================
    /**
     * YOUR CONTRIBUTION: Simple price calculation with basic discount
     */
    private double calculateSimplePrice(int numberOfSeats, String couponCode) {
        double basePrice = numberOfSeats * pricePerSeat;

        // Apply simple coupon discount if provided
        if (couponCode != null && !couponCode.isEmpty() && simpleCoupons.containsKey(couponCode.toUpperCase())) {
            double discountPercent = simpleCoupons.get(couponCode.toUpperCase());
            double discountAmount = basePrice * (discountPercent / 100);
            return basePrice - discountAmount;
        }

        return basePrice;
    }

    /**
     * YOUR CONTRIBUTION: Check if coupon is valid (simple validation)
     */
    public boolean isValidCoupon(String couponCode) {
        if (couponCode == null || couponCode.isEmpty()) {
            return false;
        }
        return simpleCoupons.containsKey(couponCode.toUpperCase());
    }

    /**
     * YOUR CONTRIBUTION: Get all available coupons (simple feature)
     */
    public Map<String, Double> getAvailableCoupons() {
        return new HashMap<>(simpleCoupons);
    }

    /**
     * YOUR CONTRIBUTION: Simple booking statistics - Total bookings made
     */
    public int getTotalBookings() {
        return totalBookingsCount;
    }
}
