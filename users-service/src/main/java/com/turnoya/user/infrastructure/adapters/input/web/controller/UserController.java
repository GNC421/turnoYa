package com.turnoya.user.infrastructure.adapters.input.web.controller;

import com.turnoya.user.application.dto.request.*;
import com.turnoya.user.application.dto.response.UserResponse;
import com.turnoya.user.application.dto.response.UserStatsResponse;
import com.turnoya.user.application.dto.response.ValidateCredentialsResponse;
import com.turnoya.user.application.ports.input.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final RegisterUserPort registerUserPort;
    private final GetUserProfilePort userProfilePort;
    private final GetUserStatsPort userStatsPort;
    private final UpdateUserProfilePort updateUserProfilePort;
    private final ChangeCredentialsPort changeCredentialsPort;
    private final ValidateCredentialsPort validateCredentialsPort;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterUserRequest request) {
        UserResponse response = registerUserPort.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getProfile(@PathVariable UUID userId) {
        UserResponse response = userProfilePort.getProfile(userId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserResponse> updateProfile(
            @PathVariable UUID userId,
            @Valid @RequestBody UpdateUserRequest request) {
        UserResponse response = updateUserProfilePort.updateProfile(userId, request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{userId}/password")
    public ResponseEntity<Void> changePassword(
            @PathVariable UUID userId,
            @Valid @RequestBody ChangePasswordRequest request) {
        changeCredentialsPort.changePassword(userId, request);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/validate")
    public ResponseEntity<ValidateCredentialsResponse> validateCredentials(
            @RequestBody ValidateCredentialsRequest request) {
        ValidateCredentialsResponse response = validateCredentialsPort.execute(request);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/stats/users")
    public ResponseEntity<List<UserStatsResponse>> getUserStats(@RequestParam List<UUID> userIds) {
        if (userIds == null || userIds.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        List<UserStatsResponse> stats = userStatsPort.getStats(userIds);
        return ResponseEntity.ok(stats);
    }
}