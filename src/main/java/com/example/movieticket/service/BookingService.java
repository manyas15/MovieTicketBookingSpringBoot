package com.example.movieticket.service;

import com.example.movieticket.model.Booking;
import com.example.movieticket.model.Movie;
import com.example.movieticket.model.Show;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BookingService {

    private Map<Integer, Booking> bookings;
    private int nextBookingId;
    private double pricePerSeat = 10.0; // $10 per seat
    private MovieService movieService;

    public BookingService(MovieService movieService) {
        this.movieService = movieService;
        this.bookings = new HashMap<>();
        this.nextBookingId = 1;
    }

    // Book tickets for a show
    public Booking bookTickets(int movieId, int showId, List<Integer> seatNumbers, String customerName) {
        // Find the movie
        Movie movie = movieService.findMovieById(movieId);
        if (movie == null) {
            throw new IllegalArgumentException("Movie not found with ID: " + movieId);
        }

        // Find the show
        Show show = movie.findShowById(showId);
        if (show == null) {
            throw new IllegalArgumentException("Show not found with ID: " + showId);
        }

        // Check if all requested seats are available
        for (int seatNumber : seatNumbers) {
            if (!show.isSeatAvailable(seatNumber)) {
                throw new IllegalStateException("Seat " + seatNumber + " is not available");
            }
        }

        // Book all the seats
        for (int seatNumber : seatNumbers) {
            show.bookSeat(seatNumber);
        }

        // Create the booking
        double totalPrice = seatNumbers.size() * pricePerSeat;
        Booking booking = new Booking(nextBookingId++, movieId, movie.getTitle(),
                showId, show.getShowTime(), new ArrayList<>(seatNumbers),
                totalPrice, customerName);

        // Save the booking
        bookings.put(booking.getId(), booking);

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

    // Get total number of bookings
    public int getTotalBookings() {
        return bookings.size();
    }

    // Set price per seat
    public void setPricePerSeat(double price) {
        this.pricePerSeat = price;
    }

    // Get price per seat
    public double getPricePerSeat() {
        return pricePerSeat;
    }
}
