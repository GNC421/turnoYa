package com.turnoya.reservation.application.ports.input;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface GetReservationStatsPort {
    Map<UUID, Double> calculateCancellationRates(List<UUID> userIds);
}
