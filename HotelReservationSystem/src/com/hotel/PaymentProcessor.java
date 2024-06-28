package com.hotel;

public class PaymentProcessor {
    public boolean processPayment(Reservation reservation, String paymentMethod) {
        // Simulate payment processing
        // In a real system, this would integrate with a payment gateway
        System.out.println("Processing payment for reservation " + reservation.getReservationId());
        reservation.setPaid(true);
        return true;
    }
}