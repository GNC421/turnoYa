package com.turnoya.reservation.infrastructure.adapters.in.web.controllers;

import com.turnoya.reservation.application.dto.request.CreateReservationRequest;
import com.turnoya.reservation.application.dto.response.ReservationResponse;
import com.turnoya.reservation.application.ports.input.CreateReservationPort;
import com.turnoya.reservation.application.ports.input.DeleteReservationPort;
import com.turnoya.reservation.application.ports.input.GetReservationPort;
import com.turnoya.reservation.application.ports.input.GetReservationStatsPort;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final CreateReservationPort createReservationPort;
    private final GetReservationPort getReservationPort;
    private final GetReservationStatsPort getReservationStatsPort;
    private final DeleteReservationPort deleteReservationPort;

    @PostMapping
    public ResponseEntity<ReservationResponse> createReservation(
            @RequestBody CreateReservationRequest request,
            @RequestHeader("X-User-Id") String ownerId) {

        ReservationResponse reservation = createReservationPort.createReservation(ownerId, request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(reservation);
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponse>> getAllReservations() {
        List<ReservationResponse> reservations = getReservationPort.getAll();
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/me")
    public ResponseEntity<List<ReservationResponse>> getMyReservations(
            @RequestHeader("X-User-Id") String userId) {
        List<ReservationResponse> reservations = getReservationPort.getByUser(userId);
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ReservationResponse>> findReservations(
            @RequestParam(name = "businessId", required = false) String businessId) {
        List<ReservationResponse> reservations = getReservationPort.getByBusiness(businessId);
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/cancellation-rates")
    public ResponseEntity<Map<UUID, Double>> getReservationCancellationRates(@RequestParam List<UUID> userIds) {
        if (userIds == null || userIds.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        Map<UUID, Double> cancellationRates = getReservationStatsPort.calculateCancellationRates(userIds);
        return ResponseEntity.ok(cancellationRates);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable UUID id) {
        deleteReservationPort.deleteReservation(id);
        return ResponseEntity.noContent().build();
    }
}
