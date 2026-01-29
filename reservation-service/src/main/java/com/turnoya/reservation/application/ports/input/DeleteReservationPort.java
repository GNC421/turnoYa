package com.turnoya.reservation.application.ports.input;

import java.util.UUID;

public interface DeleteReservationPort {
    void deleteReservation(UUID id);
}
