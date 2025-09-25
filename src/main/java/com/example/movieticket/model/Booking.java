package com.example.movieticket.model;

import java.util.List;

public class Booking {

    private int id;
    private int movieId;
    private String movieTitle;
    private int showId;
    private String showTime;
    private List<Integer> seats;
    private double totalPrice;
    private String customerName;

    public Booking() {
        // Default constructor for easier object creation
    }

    public Booking(int id, int movieId, String movieTitle, int showId, String showTime,
            List<Integer> seats, double totalPrice, String customerName) {
        this.id = id;
        this.movieId = movieId;
        this.movieTitle = movieTitle;
        this.showId = showId;
        this.showTime = showTime;
        this.seats = seats;
        this.totalPrice = totalPrice;
        this.customerName = customerName;
    }

    // Calculate total price based on number of seats and price per seat
    public void calculateTotalPrice(double pricePerSeat) {
        this.totalPrice = seats.size() * pricePerSeat;
    }

    // Display booking details in a readable format
    public void displayBookingDetails() {
        System.out.println("=== Booking Details ===");
        System.out.println("Booking ID: " + id);
        System.out.println("Customer: " + customerName);
        System.out.println("Movie: " + movieTitle);
        System.out.println("Show Time: " + showTime);
        System.out.println("Seats: " + seats);
        System.out.println("Total Price: $" + totalPrice);
        System.out.println("=====================");
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public int getShowId() {
        return showId;
    }

    public void setShowId(int showId) {
        this.showId = showId;
    }

    public String getShowTime() {
        return showTime;
    }

    public void setShowTime(String showTime) {
        this.showTime = showTime;
    }

    public List<Integer> getSeats() {
        return seats;
    }

    public void setSeats(List<Integer> seats) {
        this.seats = seats;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
}
