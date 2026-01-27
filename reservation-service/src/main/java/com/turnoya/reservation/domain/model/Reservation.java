package com.turnoya.reservation.domain.model;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class Reservation {
    private UUID id;
    private UUID businessId;
    private UUID userId;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private ReservationStatus status;
    private LocalDateTime createdAt;

    private Reservation() {}

    public static Reservation book(
            UUID businessId,
            UUID userId,
            LocalDateTime startDateTime,
            LocalDateTime endDateTime) {

        Reservation reservation = new Reservation();
        reservation.id = UUID.randomUUID();
        reservation.businessId = businessId;
        reservation.userId = userId;
        reservation.startDateTime = startDateTime;
        reservation.endDateTime = endDateTime;
        reservation.status = ReservationStatus.BOOKED;
        reservation.createdAt = LocalDateTime.now();

        reservation.validate();
        return reservation;
    }

    public static Reservation from(
            UUID id,
            UUID businessId,
            UUID userId,
            LocalDateTime startDateTime,
            LocalDateTime endDateTime,
            ReservationStatus status,
            LocalDateTime createdAt) {

        Reservation reservation = new Reservation();
        reservation.id = id;
        reservation.businessId = businessId;
        reservation.userId = userId;
        reservation.startDateTime = startDateTime;
        reservation.endDateTime = endDateTime;
        reservation.status = status;
        reservation.createdAt = createdAt;

        return reservation;
    }

    // Validación de dominio
    public void validate() {
        if (startDateTime.isAfter(endDateTime)) {
            throw new IllegalArgumentException("Start time must be before end time");
        }
        if (startDateTime.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Cannot book in the past");
        }
    }
}