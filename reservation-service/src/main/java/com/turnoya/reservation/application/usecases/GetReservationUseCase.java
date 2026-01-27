package com.turnoya.reservation.application.usecases;

import com.turnoya.reservation.application.ReservationMapper;
import com.turnoya.reservation.application.dto.request.CreateReservationRequest;
import com.turnoya.reservation.application.dto.response.ReservationResponse;
import com.turnoya.reservation.domain.model.Reservation;
import com.turnoya.reservation.domain.ports.in.ReservationServicePort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class GetReservationUseCase {

    private final ReservationServicePort reservationService;

    public GetReservationUseCase(ReservationServicePort reservationService) {
        this.reservationService = reservationService;
    }

    public List<ReservationResponse> getAll() {
        return reservationService.getAllReservations().stream()
                .map(ReservationMapper::toResponse).toList();
    }

    public List<ReservationResponse> getByUser(String ownerId) {
        return reservationService.getUserReservations(UUID.fromString(ownerId)).stream()
                .map(ReservationMapper::toResponse).toList();
    }

    public List<ReservationResponse> getByBusiness(String businessId) {
        return reservationService.getBusinessReservations(UUID.fromString(businessId)).stream()
                .map(ReservationMapper::toResponse).toList();
    }
}
