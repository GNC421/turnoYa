package com.turnoya.reservation.domain.ports.out;

import com.turnoya.reservation.domain.model.Reservation;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReservationRepositoryPort {
    Reservation save(Reservation reservation);
    List<Reservation> findAll();
    List<Reservation> findByUser(UUID userId);
    List<Reservation> findByBusinessIdAndDateTimeRange(
            UUID businessId, LocalDateTime start, LocalDateTime end);
    Optional<Reservation> findById(UUID id);
}