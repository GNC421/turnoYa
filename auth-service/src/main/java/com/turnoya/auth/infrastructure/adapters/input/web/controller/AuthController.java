package com.turnoya.auth.infrastructure.adapters.input.web.controller;

import com.turnoya.auth.application.dto.request.LoginRequest;
import com.turnoya.auth.application.dto.request.RefreshTokenRequest;
import com.turnoya.auth.application.dto.response.AuthResponse;
import com.turnoya.auth.application.usecases.LoginUseCase;
import com.turnoya.auth.application.usecases.RefreshTokenUseCase;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final LoginUseCase loginUseCase;
    private final RefreshTokenUseCase refreshTokenUseCase;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = loginUseCase.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@Valid @RequestBody RefreshTokenRequest request) {
        AuthResponse response = refreshTokenUseCase.refreshToken(request);
        return ResponseEntity.ok(response);
    }

}