package com.example.movieticket.runner;

import com.example.movieticket.model.Booking;
import com.example.movieticket.model.Movie;
import com.example.movieticket.model.Show;
import com.example.movieticket.service.BookingService;
import com.example.movieticket.service.MovieService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class AppRunner implements CommandLineRunner {

    private MovieService movieService;
    private BookingService bookingService;
    private Scanner scanner;

    public AppRunner(MovieService movieService, BookingService bookingService) {
        this.movieService = movieService;
        this.bookingService = bookingService;
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void run(String... args) {
        // Load sample data when the application starts
        movieService.loadSampleData();

        System.out.println("*********************************************");
        System.out.println("    WELCOME TO MOVIE TICKET BOOKING SYSTEM  ");
        System.out.println("*********************************************");

        boolean continueRunning = true;

        while (continueRunning) {
            displayMainMenu();

            try {
                int choice = getUserChoice();

                switch (choice) {
                    case 1:
                        displayAllMovies();
                        break;
                    case 2:
                        bookMovieTickets();
                        break;
                    case 3:
                        viewAllBookings();
                        break;
                    case 4:
                        cancelBooking();
                        break;
                    case 5:
                        continueRunning = false;
                        break;
                    default:
                        System.out.println(" Invalid choice! Please select a valid option (1-5).");
                }

                if (continueRunning) {
                    System.out.println("\nPress Enter to continue...");
                    scanner.nextLine();
                }

            } catch (Exception e) {
                System.out.println(" Error: " + e.getMessage());
                System.out.println("Please try again.");
                scanner.nextLine(); // Clear the input
            }
        }

        System.out.println("Thank you for using Movie Ticket Booking System!");
        System.out.println("Goodbye!");
    }

    private void displayMainMenu() {
        System.out.println("\n===========================================");
        System.out.println("           MAIN MENU");
        System.out.println("===========================================");
        System.out.println("1.  View All Movies");
        System.out.println("2.  Book Movie Tickets");
        System.out.println("3.  View My Bookings");
        System.out.println("4.  Cancel Booking");
        System.out.println("5.  Exit");
        System.out.println("===========================================");
        System.out.print("Please choose an option (1-5): ");
    }

    private int getUserChoice() {
        String input = scanner.nextLine().trim();
        return Integer.parseInt(input);
    }

    private void displayAllMovies() {
        System.out.println("\n===========================================");
        System.out.println("           AVAILABLE MOVIES");
        System.out.println("===========================================");

        List<Movie> movies = movieService.getAllMovies();

        if (movies.isEmpty()) {
            System.out.println("No movies available at the moment.");
            return;
        }

        for (Movie movie : movies) {
            System.out.println(" Movie ID: " + movie.getId());
            System.out.println("   Title: " + movie.getTitle());
            System.out.println("   Genre: " + movie.getGenre());
            System.out.println("   Duration: " + movie.getDuration() + " minutes");
            System.out.println("   Available Shows:");

            List<Show> shows = movie.getShows();
            if (shows.isEmpty()) {
                System.out.println("     No shows available");
            } else {
                for (Show show : shows) {
                    List<Integer> availableSeats = show.getAvailableSeats();
                    System.out.println("     📅 Show ID: " + show.getId()
                            + " | Time: " + show.getShowTime()
                            + " | Available Seats: " + availableSeats.size()
                            + "/" + show.getTotalSeats());
                }
            }
            System.out.println("-------------------------------------------");
        }
    }

    private void bookMovieTickets() {
        System.out.println("\n===========================================");
        System.out.println("           BOOK MOVIE TICKETS");
        System.out.println("===========================================");

        displayAllMovies();

        System.out.print("Enter your name: ");
        String customerName = scanner.nextLine().trim();

        if (customerName.isEmpty()) {
            System.out.println(" Customer name cannot be empty!");
            return;
        }

        System.out.print("Enter Movie ID: ");
        int movieId = Integer.parseInt(scanner.nextLine().trim());

        Movie movie = movieService.findMovieById(movieId);
        if (movie == null) {
            System.out.println(" Movie not found with ID: " + movieId);
            return;
        }

        System.out.print("Enter Show ID: ");
        int showId = Integer.parseInt(scanner.nextLine().trim());

        Show show = movie.findShowById(showId);
        if (show == null) {
            System.out.println(" Show not found with ID: " + showId);
            return;
        }

        // Display available seats
        List<Integer> availableSeats = show.getAvailableSeats();
        System.out.println("\nAvailable seats: " + availableSeats);

        if (availableSeats.isEmpty()) {
            System.out.println(" Sorry, this show is fully booked!");
            return;
        }

        System.out.print("Enter seat numbers (comma separated, e.g., 1,2,3): ");
        String seatInput = scanner.nextLine().trim();

        List<Integer> requestedSeats = new ArrayList<>();
        try {
            String[] seatStrings = seatInput.split(",");
            for (String seatStr : seatStrings) {
                int seatNumber = Integer.parseInt(seatStr.trim());
                requestedSeats.add(seatNumber);
            }
        } catch (NumberFormatException e) {
            System.out.println(" Invalid seat format! Please use numbers separated by commas.");
            return;
        }

        // Book the tickets
        try {
            Booking booking = bookingService.bookTickets(movieId, showId, requestedSeats, customerName);
            System.out.println("\n✅ BOOKING SUCCESSFUL!");
            booking.displayBookingDetails();
        } catch (Exception e) {
            System.out.println(" Booking failed: " + e.getMessage());
        }
    }

    private void viewAllBookings() {
        System.out.println("\n===========================================");
        System.out.println("           ALL BOOKINGS");
        System.out.println("===========================================");

        List<Booking> bookings = bookingService.getAllBookings();

        if (bookings.isEmpty()) {
            System.out.println("No bookings found.");
            return;
        }

        for (Booking booking : bookings) {
            booking.displayBookingDetails();
        }
    }

    private void cancelBooking() {
        System.out.println("\n===========================================");
        System.out.println("           CANCEL BOOKING");
        System.out.println("===========================================");

        viewAllBookings();

        System.out.print("Enter Booking ID to cancel: ");
        int bookingId = Integer.parseInt(scanner.nextLine().trim());

        boolean success = bookingService.cancelBooking(bookingId);

        if (success) {
            System.out.println(" Booking cancelled successfully!");
        } else {
            System.out.println(" Booking not found with ID: " + bookingId);
        }
    }
}
