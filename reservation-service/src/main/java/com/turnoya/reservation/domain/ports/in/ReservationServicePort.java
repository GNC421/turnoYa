package com.turnoya.reservation.domain.ports.in;

import com.turnoya.reservation.domain.model.Reservation;
import java.util.List;
import java.util.UUID;

public interface ReservationServicePort {
    Reservation createReservation(Reservation reservation);
    List<Reservation> getAllReservations();
    List<Reservation> getUserReservations(UUID userId);
    List<Reservation> getBusinessReservations(UUID businessId);
}