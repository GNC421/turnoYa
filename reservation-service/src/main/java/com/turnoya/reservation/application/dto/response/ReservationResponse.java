package com.turnoya.reservation.application.dto.response;

import com.turnoya.reservation.domain.model.Reservation;
import java.time.LocalDateTime;
import java.util.UUID;

public record ReservationResponse(
        UUID id,
        UUID businessId,
        UUID userId,
        LocalDateTime startDateTime,
        LocalDateTime endDateTime,
        String status,
        LocalDateTime createdAt
) {
    public static ReservationResponse fromDomain(Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getBusinessId(),
                reservation.getUserId(),
                reservation.getStartDateTime(),
                reservation.getEndDateTime(),
                reservation.getStatus().name(),
                reservation.getCreatedAt()
        );
    }
}