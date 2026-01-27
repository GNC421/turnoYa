package com.turnoya.reservation.infrastructure.adapters.in.web.controllers;

import com.turnoya.reservation.application.dto.request.CreateReservationRequest;
import com.turnoya.reservation.application.dto.response.ReservationResponse;
import com.turnoya.reservation.application.usecases.CreateReservationUseCase;
import com.turnoya.reservation.application.usecases.GetReservationUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final CreateReservationUseCase createReservationUseCase;
    private final GetReservationUseCase getReservationUseCase;

    @PostMapping
    public ResponseEntity<ReservationResponse> createReservation(
            @RequestBody CreateReservationRequest request,
            @RequestHeader("X-User-Id") String ownerId) {

        ReservationResponse reservation = createReservationUseCase.execute(ownerId, request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(reservation);
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponse>> getAllReservations() {
        List<ReservationResponse> reservations = getReservationUseCase.getAll();
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/me")
    public ResponseEntity<List<ReservationResponse>> getMyReservations(
            @RequestHeader("X-User-Id") String userId) {
        List<ReservationResponse> reservations = getReservationUseCase.getByUser(userId);
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ReservationResponse>> findReservations(
            @RequestParam(name = "businessId", required = false) String businessId) {
        List<ReservationResponse> reservations = getReservationUseCase.getByBusiness(businessId);
        return ResponseEntity.ok(reservations);
    }
}
