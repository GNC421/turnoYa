package com.turnoya.reservation.application.usecases;

import com.turnoya.reservation.application.ReservationMapper;
import com.turnoya.reservation.application.dto.response.ReservationResponse;
import com.turnoya.reservation.application.ports.input.GetReservationPort;
import com.turnoya.reservation.domain.model.Reservation;
import com.turnoya.reservation.domain.ports.in.ReservationServicePort;
import com.turnoya.reservation.domain.ports.out.ReservationRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class GetReservationUseCase implements GetReservationPort {

    private final ReservationRepositoryPort repository;

    public GetReservationUseCase(ReservationRepositoryPort reservationRepositoryPort) {
        this.repository = reservationRepositoryPort;
    }

    public List<ReservationResponse> getAll() {
        return repository.findAll().stream()
                .map(ReservationMapper::toResponse).toList();
    }

    public List<ReservationResponse> getByUser(String ownerId) {
        return repository.findByUser(UUID.fromString(ownerId)).stream()
                .map(ReservationMapper::toResponse).toList();
    }

    public List<ReservationResponse> getByBusiness(String businessId) {
        return repository.findByBusinessIdAndDateTimeRange(UUID.fromString(businessId), LocalDateTime.now(),
                        LocalDateTime.now().plusYears(5)).stream()
                .map(ReservationMapper::toResponse).toList();
    }
}
