package com.turnoya.user.application.ports.output;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ReservationServicePort {
    Map<UUID, Double> getPercentageBooksFailed(List<UUID> userIds);
}
