package com.hotel;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public class Hotel {
    private List<Room> rooms;
    private List<Reservation> reservations;

    public Hotel() {
        rooms = new ArrayList<>();
        reservations = new ArrayList<>();
    }

    public void addRoom(Room room) {
        rooms.add(room);
    }

    public List<Room> searchAvailableRooms(LocalDate checkIn, LocalDate checkOut, String category) {
        return rooms.stream()
                .filter(room -> room.isAvailable() && room.getCategory().equals(category))
                .filter(room -> isRoomAvailable(room, checkIn, checkOut))
                .collect(Collectors.toList());
    }

    private boolean isRoomAvailable(Room room, LocalDate checkIn, LocalDate checkOut) {
        return reservations.stream()
                .filter(res -> res.getRoomNumber() == room.getRoomNumber())
                .noneMatch(res -> res.getCheckInDate().isBefore(checkOut) && res.getCheckOutDate().isAfter(checkIn));
    }

    public Reservation makeReservation(Room room, String guestName, LocalDate checkIn, LocalDate checkOut) {
        if (!isRoomAvailable(room, checkIn, checkOut)) {
            throw new IllegalStateException("Room is not available for the selected dates.");
        }

        int nights = (int) checkIn.until(checkOut).getDays();
        double totalPrice = room.getPricePerNight() * nights;

        Reservation reservation = new Reservation(
                reservations.size() + 1,
                room.getRoomNumber(),
                guestName,
                checkIn,
                checkOut,
                totalPrice,
                false
        );

        reservations.add(reservation);
        return reservation;
    }

    public Reservation getReservation(int reservationId) {
        return reservations.stream()
                .filter(res -> res.getReservationId() == reservationId)
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Reservation not found."));
    }

    public Room getRoomByNumber(int roomNumber) {
        return rooms.stream()
                .filter(room -> room.getRoomNumber() == roomNumber)
                .findFirst()
                .orElse(null);
    }
}