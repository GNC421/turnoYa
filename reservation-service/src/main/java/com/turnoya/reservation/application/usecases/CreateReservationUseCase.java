package com.turnoya.reservation.application.usecases;

import com.turnoya.reservation.application.ReservationMapper;
import com.turnoya.reservation.application.dto.request.CreateReservationRequest;
import com.turnoya.reservation.application.dto.response.ReservationResponse;
import com.turnoya.reservation.domain.model.Reservation;
import com.turnoya.reservation.domain.ports.in.ReservationServicePort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CreateReservationUseCase {
    
    private final ReservationServicePort reservationService;

    public CreateReservationUseCase(ReservationServicePort reservationService) {
        this.reservationService = reservationService;
    }

    public ReservationResponse execute(String ownerId, CreateReservationRequest request) {

        Reservation Reservation = ReservationMapper.toDomain(ownerId, request);

        Reservation registeredReservation = reservationService.createReservation(Reservation);

        return ReservationMapper.toResponse(registeredReservation);
    }
    
}
