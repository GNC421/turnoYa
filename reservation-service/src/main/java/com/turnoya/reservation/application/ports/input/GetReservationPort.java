package com.turnoya.reservation.application.ports.input;

import com.turnoya.reservation.application.dto.response.ReservationResponse;

import java.util.List;

public interface GetReservationPort {
    List<ReservationResponse> getAll();
    List<ReservationResponse> getByUser(String ownerId);
    List<ReservationResponse> getByBusiness(String businessId);
}
