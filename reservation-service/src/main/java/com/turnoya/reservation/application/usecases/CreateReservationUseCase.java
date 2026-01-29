package com.turnoya.reservation.application.usecases;

import com.turnoya.reservation.application.ReservationMapper;
import com.turnoya.reservation.application.dto.request.CreateReservationRequest;
import com.turnoya.reservation.application.dto.response.ReservationResponse;
import com.turnoya.reservation.application.ports.input.CreateReservationPort;
import com.turnoya.reservation.domain.exception.OverlappingReservationException;
import com.turnoya.reservation.domain.model.Reservation;
import com.turnoya.reservation.domain.ports.in.ReservationServicePort;
import com.turnoya.reservation.domain.ports.out.ReservationRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CreateReservationUseCase implements CreateReservationPort {

    private final ReservationRepositoryPort repository;

    public CreateReservationUseCase(ReservationRepositoryPort reservationRepositoryPort) {
        this.repository = reservationRepositoryPort;
    }

    @Override
    @Transactional
    public ReservationResponse createReservation(String ownerId, CreateReservationRequest request) {

        Reservation reservation = ReservationMapper.toDomain(ownerId, request);

        validateNoOverlappingReservations(reservation);
        Reservation registeredReservation = repository.save(reservation);

        return ReservationMapper.toResponse(registeredReservation);
    }

    private void validateNoOverlappingReservations(Reservation newReservation) {
        List<Reservation> existingReservations = repository
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
    
}
