package com.turnoya.reservation.application.usecases;

import com.turnoya.reservation.application.ports.input.DeleteReservationPort;
import com.turnoya.reservation.domain.ports.out.ReservationRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class DeleteReservationUseCase implements DeleteReservationPort {

    private final ReservationRepositoryPort repository;

    public DeleteReservationUseCase(ReservationRepositoryPort reservationRepositoryPort) {
        this.repository = reservationRepositoryPort;
    }

    @Override
    @Transactional
    public void deleteReservation(UUID id) {
        repository.delete(id);
    }
}
