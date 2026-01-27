package com.turnoya.reservation.application.dto.request;

import java.time.LocalDateTime;
import java.util.UUID;

public record CreateReservationRequest(
        UUID businessId,
        LocalDateTime startDateTime,
        LocalDateTime endDateTime
) {}