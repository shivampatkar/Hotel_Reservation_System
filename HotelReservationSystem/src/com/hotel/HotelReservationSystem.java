package com.hotel;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class HotelReservationSystem {
    private Hotel hotel;
    private PaymentProcessor paymentProcessor;
    private Scanner scanner;

    public HotelReservationSystem() {
        hotel = new Hotel();
        paymentProcessor = new PaymentProcessor();
        scanner = new Scanner(System.in);
    }

    public void run() {
        while (true) {
            System.out.println("\n1. Search for available rooms");
            System.out.println("2. Make a reservation");
            System.out.println("3. View reservation details");
            System.out.println("4. Process payment");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    searchRooms();
                    break;
                case 2:
                    makeReservation();
                    break;
                case 3:
                    viewReservation();
                    break;
                case 4:
                    processPayment();
                    break;
                case 5:
                    System.out.println("Thank you for using the Hotel Reservation System. Goodbye!");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void searchRooms() {
        System.out.print("Enter check-in date (YYYY-MM-DD): ");
        LocalDate checkIn = LocalDate.parse(scanner.nextLine());
        System.out.print("Enter check-out date (YYYY-MM-DD): ");
        LocalDate checkOut = LocalDate.parse(scanner.nextLine());
        System.out.print("Enter room category (Standard/Deluxe/Suite): ");
        String category = scanner.nextLine();

        List<Room> availableRooms = hotel.searchAvailableRooms(checkIn, checkOut, category);
        if (availableRooms.isEmpty()) {
            System.out.println("No available rooms found for the given criteria.");
        } else {
            System.out.println("Available rooms:");
            for (Room room : availableRooms) {
                System.out.printf("Room %d - %s - $%.2f per night%n", 
                    room.getRoomNumber(), room.getCategory(), room.getPricePerNight());
            }
        }
    }

    private void makeReservation() {
        System.out.print("Enter room number: ");
        int roomNumber = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        System.out.print("Enter guest name: ");
        String guestName = scanner.nextLine();
        System.out.print("Enter check-in date (YYYY-MM-DD): ");
        LocalDate checkIn = LocalDate.parse(scanner.nextLine());
        System.out.print("Enter check-out date (YYYY-MM-DD): ");
        LocalDate checkOut = LocalDate.parse(scanner.nextLine());

        Room room = hotel.getRoomByNumber(roomNumber);
        if (room == null) {
            System.out.println("Room not found.");
            return;
        }

        try {
            Reservation reservation = hotel.makeReservation(room, guestName, checkIn, checkOut);
            System.out.printf("Reservation created successfully. Reservation ID: %d%n", 
                reservation.getReservationId());
        } catch (IllegalStateException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void viewReservation() {
        System.out.print("Enter reservation ID: ");
        int reservationId = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        try {
            Reservation reservation = hotel.getReservation(reservationId);
            System.out.println("Reservation details:");
            System.out.printf("ID: %d%n", reservation.getReservationId());
            System.out.printf("Guest: %s%n", reservation.getGuestName());
            System.out.printf("Room: %d%n", reservation.getRoomNumber());
            System.out.printf("Check-in: %s%n", reservation.getCheckInDate());
            System.out.printf("Check-out: %s%n", reservation.getCheckOutDate());
            System.out.printf("Total price: $%.2f%n", reservation.getTotalPrice());
            System.out.printf("Paid: %s%n", reservation.isPaid() ? "Yes" : "No");
        } catch (NoSuchElementException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void processPayment() {
        System.out.print("Enter reservation ID: ");
        int reservationId = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        System.out.print("Enter payment method (Credit/Debit): ");
        String paymentMethod = scanner.nextLine();

        try {
            Reservation reservation = hotel.getReservation(reservationId);
            if (reservation.isPaid()) {
                System.out.println("This reservation has already been paid.");
                return;
            }

            boolean paymentSuccess = paymentProcessor.processPayment(reservation, paymentMethod);
            if (paymentSuccess) {
                System.out.println("Payment processed successfully.");
            } else {
                System.out.println("Payment processing failed. Please try again.");
            }
        } catch (NoSuchElementException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        HotelReservationSystem system = new HotelReservationSystem();
        // Add some sample rooms
        system.hotel.addRoom(new Room(100, "Standard", 1000.0, true));
        system.hotel.addRoom(new Room(101, "Deluxe", 2000.0, true));
        system.hotel.addRoom(new Room(102, "Suite", 5000.0, true));
        system.run();
    }
}