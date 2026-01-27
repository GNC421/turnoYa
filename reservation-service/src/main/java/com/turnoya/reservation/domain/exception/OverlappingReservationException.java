package com.turnoya.reservation.domain.exception;

public class OverlappingReservationException extends ReservationException {
    public OverlappingReservationException(String message) {
        super(message);
    }
}
