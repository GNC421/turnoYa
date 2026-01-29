package com.turnoya.reservation.application.ports.input;

import com.turnoya.reservation.application.dto.request.CreateReservationRequest;
import com.turnoya.reservation.application.dto.response.ReservationResponse;

public interface CreateReservationPort {
    ReservationResponse createReservation(String ownerId, CreateReservationRequest request);
}
