package com.turnoya.reservation.application.usecases;

import com.turnoya.reservation.application.ports.input.GetReservationStatsPort;
import com.turnoya.reservation.domain.model.Reservation;
import com.turnoya.reservation.domain.model.ReservationStatus;
import com.turnoya.reservation.domain.ports.in.ReservationServicePort;
import com.turnoya.reservation.domain.ports.out.ReservationRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Transactional
public class GetReservationStatsUseCase implements GetReservationStatsPort {

    private final ReservationRepositoryPort repository;

    public GetReservationStatsUseCase(ReservationRepositoryPort reservationRepositoryPort) {
        this.repository = reservationRepositoryPort;
    }

    public Map<UUID, Double> calculateCancellationRates(List<UUID> userIds) {
        HashMap<UUID, Double> map = new HashMap<>();
        for(UUID userId: userIds) {
            List<Reservation> reservations = repository.findByUser(userId);
            int total = reservations.size();
            int cancelled = (int) reservations.stream().filter(r -> r.getStatus().equals(ReservationStatus.CANCELLED)).count();
            double rate = total == 0 ? 0.0 : (double) cancelled / total;
            map.put(userId, rate);
        }
        return map;
    }
}
