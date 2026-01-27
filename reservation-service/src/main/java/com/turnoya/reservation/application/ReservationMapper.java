package com.turnoya.reservation.application;

import com.turnoya.reservation.application.dto.request.CreateReservationRequest;
import com.turnoya.reservation.application.dto.response.ReservationResponse;
import com.turnoya.reservation.domain.model.Reservation;

import java.util.UUID;

public class ReservationMapper {

    public static Reservation toDomain(String ownerId, CreateReservationRequest request) {
        return Reservation.book(
                request.businessId(),
                UUID.fromString(ownerId),
                request.startDateTime(),
                request.endDateTime()
        );
    }

    public static ReservationResponse toResponse(Reservation reservation) {
        return ReservationResponse.fromDomain(reservation);
    }
}
