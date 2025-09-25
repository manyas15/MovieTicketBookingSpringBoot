package com.example.movieticket.model;

import java.util.ArrayList;
import java.util.List;

public class Show {

    private int id;
    private String showTime;
    private int totalSeats;
    private List<Integer> bookedSeats;

    public Show(int id, String showTime, int totalSeats) {
        this.id = id;
        this.showTime = showTime;
        this.totalSeats = totalSeats;
        this.bookedSeats = new ArrayList<>();
    }

    // Check if a seat is available
    public boolean isSeatAvailable(int seatNumber) {
        return seatNumber >= 1 && seatNumber <= totalSeats && !bookedSeats.contains(seatNumber);
    }

    // Book a seat
    public void bookSeat(int seatNumber) {
        if (isSeatAvailable(seatNumber)) {
            bookedSeats.add(seatNumber);
        }
    }

    // Cancel a seat booking
    public void cancelSeat(int seatNumber) {
        bookedSeats.remove(Integer.valueOf(seatNumber));
    }

    // Get available seats
    public List<Integer> getAvailableSeats() {
        List<Integer> availableSeats = new ArrayList<>();
        for (int i = 1; i <= totalSeats; i++) {
            if (!bookedSeats.contains(i)) {
                availableSeats.add(i);
            }
        }
        return availableSeats;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getShowTime() {
        return showTime;
    }

    public void setShowTime(String showTime) {
        this.showTime = showTime;
    }

    public int getTotalSeats() {
        return totalSeats;
    }

    public void setTotalSeats(int totalSeats) {
        this.totalSeats = totalSeats;
    }

    public List<Integer> getBookedSeats() {
        return bookedSeats;
    }

    public void setBookedSeats(List<Integer> bookedSeats) {
        this.bookedSeats = bookedSeats;
    }
}
