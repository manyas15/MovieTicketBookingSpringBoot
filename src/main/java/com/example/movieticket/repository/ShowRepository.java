package com.example.movieticket.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.example.movieticket.model.Show;

/**
 * Repository class for Show entity - In-Memory Implementation Provides data
 * access methods for Show operations Uses HashMap for in-memory storage (no
 * database required)
 */
@Repository
public class ShowRepository {

    private final Map<Integer, Show> shows = new HashMap<>();

    /**
     * Save a show to the repository
     *
     * @param show Show to save
     * @return Saved show
     */
    public Show save(Show show) {
        shows.put(show.getId(), show);
        return show;
    }

    /**
     * Find show by ID
     *
     * @param id Show ID
     * @return Optional containing the show if found
     */
    public Optional<Show> findById(Integer id) {
        return Optional.ofNullable(shows.get(id));
    }

    /**
     * Find all shows
     *
     * @return List of all shows
     */
    public List<Show> findAll() {
        return new ArrayList<>(shows.values());
    }

    /**
     * Delete show by ID
     *
     * @param id Show ID to delete
     */
    public void deleteById(Integer id) {
        shows.remove(id);
    }

    /**
     * Check if show exists by ID
     *
     * @param id Show ID
     * @return true if exists, false otherwise
     */
    public boolean existsById(Integer id) {
        return shows.containsKey(id);
    }

    /**
     * Count total shows
     *
     * @return Number of shows
     */
    public long count() {
        return shows.size();
    }

    /**
     * Find shows by show time
     *
     * @param showTime Show time to search for
     * @return List of shows with matching time
     */
    public List<Show> findByShowTime(String showTime) {
        return shows.values().stream()
                .filter(show -> show.getShowTime().equals(showTime))
                .collect(Collectors.toList());
    }

    /**
     * Find shows with available seats
     *
     * @return List of shows that have at least one available seat
     */
    public List<Show> findShowsWithAvailableSeats() {
        return shows.values().stream()
                .filter(show -> !show.getAvailableSeats().isEmpty())
                .collect(Collectors.toList());
    }

    /**
     * Find shows by minimum available seats
     *
     * @param minSeats Minimum number of available seats required
     * @return List of shows with at least the specified number of available
     * seats
     */
    public List<Show> findByMinimumAvailableSeats(int minSeats) {
        return shows.values().stream()
                .filter(show -> show.getAvailableSeats().size() >= minSeats)
                .collect(Collectors.toList());
    }

    /**
     * Find shows by total capacity range
     *
     * @param minCapacity Minimum total seats
     * @param maxCapacity Maximum total seats
     * @return List of shows within the capacity range
     */
    public List<Show> findByTotalSeatsBetween(int minCapacity, int maxCapacity) {
        return shows.values().stream()
                .filter(show -> show.getTotalSeats() >= minCapacity && show.getTotalSeats() <= maxCapacity)
                .collect(Collectors.toList());
    }

    /**
     * Find all shows ordered by show time
     *
     * @return List of all shows sorted by show time
     */
    public List<Show> findAllOrderedByShowTime() {
        return shows.values().stream()
                .sorted((s1, s2) -> s1.getShowTime().compareToIgnoreCase(s2.getShowTime()))
                .collect(Collectors.toList());
    }

    /**
     * Count shows by show time
     *
     * @param showTime Show time to count
     * @return Number of shows at the specified time
     */
    public long countByShowTime(String showTime) {
        return shows.values().stream()
                .filter(show -> show.getShowTime().equals(showTime))
                .count();
    }

    /**
     * Find fully booked shows (no available seats)
     *
     * @return List of shows that are fully booked
     */
    public List<Show> findFullyBookedShows() {
        return shows.values().stream()
                .filter(show -> show.getAvailableSeats().isEmpty())
                .collect(Collectors.toList());
    }

    /**
     * Get occupancy statistics for all shows
     *
     * @return Map with show ID as key and occupancy percentage as value
     */
    public Map<Integer, Double> getOccupancyStatistics() {
        Map<Integer, Double> stats = new HashMap<>();
        shows.values().forEach(show -> {
            int totalSeats = show.getTotalSeats();
            int availableSeats = show.getAvailableSeats().size();
            int bookedSeats = totalSeats - availableSeats;
            double occupancyRate = totalSeats > 0 ? (double) bookedSeats / totalSeats * 100 : 0;
            stats.put(show.getId(), occupancyRate);
        });
        return stats;
    }

    /**
     * Clear all shows (for testing)
     */
    public void deleteAll() {
        shows.clear();
    }
}
