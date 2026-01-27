package com.turnoya.reservation.domain.service;

import com.turnoya.reservation.domain.model.Reservation;
import com.turnoya.reservation.domain.ports.in.ReservationServicePort;
import com.turnoya.reservation.domain.ports.out.ReservationRepositoryPort;
import com.turnoya.reservation.domain.exception.OverlappingReservationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReservationService implements ReservationServicePort {

    private final ReservationRepositoryPort reservationRepository;

    @Override
    @Transactional
    public Reservation createReservation(Reservation reservation) {

        // Validar solapamientos
        validateNoOverlappingReservations(reservation);

        // Guardar
        return reservationRepository.save(reservation);
    }

    private void validateNoOverlappingReservations(Reservation newReservation) {
        List<Reservation> existingReservations = reservationRepository
                .findByBusinessIdAndDateTimeRange(
                        newReservation.getBusinessId(),
                        newReservation.getStartDateTime(),
                        newReservation.getEndDateTime()
                );

        boolean hasOverlap = existingReservations.stream()
                .anyMatch(existing ->
                        !existing.getId().equals(newReservation.getId()) &&
                                isOverlapping(existing, newReservation));

        if (hasOverlap) {
            throw new OverlappingReservationException(
                    "Reservation overlaps with existing reservation");
        }
    }

    private boolean isOverlapping(Reservation r1, Reservation r2) {
        return r1.getStartDateTime().isBefore(r2.getEndDateTime()) &&
                r2.getStartDateTime().isBefore(r1.getEndDateTime());
    }

    @Override
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    @Override
    public List<Reservation> getUserReservations(UUID userId) {
        return reservationRepository.findByUser(userId);
    }

    @Override
    public List<Reservation> getBusinessReservations(UUID businessId) {
        return reservationRepository.findByBusinessIdAndDateTimeRange(businessId, LocalDateTime.now(), LocalDateTime.now().plusYears(5));
    }
}
