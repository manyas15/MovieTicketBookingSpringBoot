package com.example.movieticket.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.example.movieticket.model.Booking;

/**
 * Repository class for Booking entity - In-Memory Implementation Provides data
 * access methods for Booking operations Uses HashMap for in-memory storage (no
 * database required)
 */
@Repository
public class BookingRepository {

    private final Map<Integer, Booking> bookings = new HashMap<>();

    /**
     * Save a booking to the repository
     *
     * @param booking Booking to save
     * @return Saved booking
     */
    public Booking save(Booking booking) {
        bookings.put(booking.getId(), booking);
        return booking;
    }

    /**
     * Find booking by ID
     *
     * @param id Booking ID
     * @return Optional containing the booking if found
     */
    public Optional<Booking> findById(Integer id) {
        return Optional.ofNullable(bookings.get(id));
    }

    /**
     * Find all bookings
     *
     * @return List of all bookings
     */
    public List<Booking> findAll() {
        return new ArrayList<>(bookings.values());
    }

    /**
     * Delete booking by ID
     *
     * @param id Booking ID to delete
     */
    public void deleteById(Integer id) {
        bookings.remove(id);
    }

    /**
     * Check if booking exists by ID
     *
     * @param id Booking ID
     * @return true if exists, false otherwise
     */
    public boolean existsById(Integer id) {
        return bookings.containsKey(id);
    }

    /**
     * Count total bookings
     *
     * @return Number of bookings
     */
    public long count() {
        return bookings.size();
    }

    /**
     * Find bookings by customer name (case-insensitive)
     *
     * @param customerName Customer name to search for
     * @return List of bookings for the customer
     */
    public List<Booking> findByCustomerName(String customerName) {
        return bookings.values().stream()
                .filter(booking -> booking.getCustomerName().equalsIgnoreCase(customerName))
                .collect(Collectors.toList());
    }

    /**
     * Find bookings by movie ID
     *
     * @param movieId Movie ID to search for
     * @return List of bookings for the movie
     */
    public List<Booking> findByMovieId(Integer movieId) {
        return bookings.values().stream()
                .filter(booking -> booking.getMovieId() == movieId)
                .collect(Collectors.toList());
    }

    /**
     * Find bookings by show ID
     *
     * @param showId Show ID to search for
     * @return List of bookings for the show
     */
    public List<Booking> findByShowId(Integer showId) {
        return bookings.values().stream()
                .filter(booking -> booking.getShowId() == showId)
                .collect(Collectors.toList());
    }

    /**
     * Find bookings by movie title (case-insensitive)
     *
     * @param movieTitle Movie title to search for
     * @return List of bookings for movies with matching titles
     */
    public List<Booking> findByMovieTitle(String movieTitle) {
        return bookings.values().stream()
                .filter(booking -> booking.getMovieTitle().toLowerCase().contains(movieTitle.toLowerCase()))
                .collect(Collectors.toList());
    }

    /**
     * Find bookings by price range
     *
     * @param minPrice Minimum total price
     * @param maxPrice Maximum total price
     * @return List of bookings within the price range
     */
    public List<Booking> findByTotalPriceBetween(double minPrice, double maxPrice) {
        return bookings.values().stream()
                .filter(booking -> booking.getTotalPrice() >= minPrice && booking.getTotalPrice() <= maxPrice)
                .collect(Collectors.toList());
    }

    /**
     * Find bookings with minimum number of seats
     *
     * @param minSeats Minimum number of seats
     * @return List of bookings with at least the specified number of seats
     */
    public List<Booking> findByMinimumSeats(int minSeats) {
        return bookings.values().stream()
                .filter(booking -> booking.getSeats().size() >= minSeats)
                .collect(Collectors.toList());
    }

    /**
     * Find all bookings ordered by total price (descending)
     *
     * @return List of bookings sorted by total price (highest first)
     */
    public List<Booking> findAllOrderedByTotalPriceDesc() {
        return bookings.values().stream()
                .sorted((b1, b2) -> Double.compare(b2.getTotalPrice(), b1.getTotalPrice()))
                .collect(Collectors.toList());
    }

    /**
     * Find all bookings ordered by customer name
     *
     * @return List of bookings sorted by customer name
     */
    public List<Booking> findAllOrderedByCustomerName() {
        return bookings.values().stream()
                .sorted((b1, b2) -> b1.getCustomerName().compareToIgnoreCase(b2.getCustomerName()))
                .collect(Collectors.toList());
    }

    /**
     * Count bookings by customer
     *
     * @param customerName Customer name
     * @return Number of bookings for the customer
     */
    public long countByCustomerName(String customerName) {
        return bookings.values().stream()
                .filter(booking -> booking.getCustomerName().equalsIgnoreCase(customerName))
                .count();
    }

    /**
     * Count bookings by movie
     *
     * @param movieId Movie ID
     * @return Number of bookings for the movie
     */
    public long countByMovieId(Integer movieId) {
        return bookings.values().stream()
                .filter(booking -> booking.getMovieId() == movieId)
                .count();
    }

    /**
     * Get total revenue from all bookings
     *
     * @return Sum of all booking prices
     */
    public double getTotalRevenue() {
        return bookings.values().stream()
                .mapToDouble(Booking::getTotalPrice)
                .sum();
    }

    /**
     * Get revenue by movie
     *
     * @return Map with movie ID as key and total revenue as value
     */
    public Map<Integer, Double> getRevenueByMovie() {
        return bookings.values().stream()
                .collect(Collectors.groupingBy(
                        Booking::getMovieId,
                        Collectors.summingDouble(Booking::getTotalPrice)
                ));
    }

    /**
     * Get total seats booked
     *
     * @return Total number of seats across all bookings
     */
    public int getTotalSeatsBooked() {
        return bookings.values().stream()
                .mapToInt(booking -> booking.getSeats().size())
                .sum();
    }

    /**
     * Clear all bookings (for testing)
     */
    public void deleteAll() {
        bookings.clear();
    }
}
