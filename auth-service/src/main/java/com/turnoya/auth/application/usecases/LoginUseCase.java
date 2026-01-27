package com.turnoya.auth.application.usecases;

import com.turnoya.auth.application.dto.request.LoginRequest;
import com.turnoya.auth.application.dto.response.AuthResponse;
import com.turnoya.auth.application.ports.input.AuthPort;
import com.turnoya.auth.application.ports.output.TokenRepositoryPort;
import com.turnoya.auth.application.ports.output.UserServicePort;
import com.turnoya.auth.domain.service.JwtService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class LoginUseCase implements AuthPort {

    private final UserServicePort userService;
    private final TokenRepositoryPort tokenRepositoryPort;
    private final JwtService jwtService;

    @Override
    @Transactional
    public AuthResponse login(LoginRequest request) {
        Map<String, Object> userData = userService.validateCredentials(
                request.getEmail(),
                request.getPassword()
        );

        String userId = userData.get("id").toString();
        String email = userData.get("email").toString();
        String name = userData.get("name").toString();

        String accessToken = jwtService.generateAccessToken(userId, email);
        String refreshToken = jwtService.generateRefreshToken(userId);
        if(tokenRepositoryPort == null) {
            System.err.println("ERROR: Token repository");
        }
        tokenRepositoryPort.saveRefreshToken(userId, refreshToken);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(900L) // 15 minutos en segundos
                .user(AuthResponse.UserInfo.builder()
                        .id(userId)
                        .email(email)
                        .name(name)
                        .build())
                .build();
    }
}