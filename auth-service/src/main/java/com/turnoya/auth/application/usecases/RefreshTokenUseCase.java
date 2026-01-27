package com.turnoya.auth.application.usecases;

import com.turnoya.auth.application.dto.request.RefreshTokenRequest;
import com.turnoya.auth.application.dto.response.AuthResponse;
import com.turnoya.auth.application.ports.input.AuthPort;
import com.turnoya.auth.application.ports.output.TokenRepositoryPort;
import com.turnoya.auth.domain.service.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RefreshTokenUseCase implements AuthPort {

    private final JwtService jwtService;
    private final TokenRepositoryPort tokenRepository;

    @Transactional
    public AuthResponse refreshToken(RefreshTokenRequest request) {

        try {
            // 1. Validar que el token sea un refresh token
            if (!jwtService.validateToken(request.getRefreshToken())) {
                throw new IllegalArgumentException("Invalid refresh token");
            }

            // 2. Verificar que sea un refresh token (no access token)
            if (!jwtService.isRefreshToken(request.getRefreshToken())) {
                throw new IllegalArgumentException("Token is not a refresh token");
            }

            // 3. Extraer información del token
            String userId = jwtService.extractUserId(request.getRefreshToken());
            String email = jwtService.extractEmail(request.getRefreshToken());
            String name = jwtService.extractName(request.getRefreshToken());


            // 4. Verificar que el refresh token no esté expirado
            if (jwtService.isTokenExpired(request.getRefreshToken())) {
                throw new IllegalArgumentException("Refresh token expired");
            }

            // 5. Verificar que el refresh token esté en la base de datos (si usas almacenamiento)
            if (!tokenRepository.isValidRefreshToken(userId, request.getRefreshToken())) {
                throw new IllegalArgumentException("Refresh token revoked");
            }

            // 6. Generar nuevos tokens
            String newAccessToken = jwtService.generateAccessToken(userId, email);
            String newRefreshToken = jwtService.generateRefreshToken(userId);

            // 7. Guardar nuevo refresh token
            tokenRepository.saveRefreshToken(userId, newRefreshToken);

            // 8. Retornar nueva respuesta
            return buildAuthResponse(newAccessToken, newRefreshToken, AuthResponse.UserInfo.builder()
                    .id(userId)
                    .email(email)
                    .name(name)
                    .build());

        } catch (IllegalArgumentException e) {
            throw e; // Re-lanzar para manejo específico
        } catch (Exception e) {
            throw new RuntimeException("Token refresh failed");
        }
    }

    // ============ HELPER METHODS ============

    private AuthResponse buildAuthResponse(String accessToken, String refreshToken, AuthResponse.UserInfo userInfo) {
        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .user(userInfo)
                .tokenType("Bearer")
                .expiresIn(900L) // 15 minutos en segundos
                .build();
    }

    // ============ SOBRECARGA PARA LoginRequest (no usada aquí) ============

    @Override
    public AuthResponse login(com.turnoya.auth.application.dto.request.LoginRequest request) {
        throw new UnsupportedOperationException(
                "Use LoginUseCase for login operations"
        );
    }

    // ============ MÉTODOS ADICIONALES ============

    @Transactional
    public void invalidateRefreshToken(String userId) {
        if (tokenRepository != null) {
            tokenRepository.invalidateRefreshToken(userId);
        }
    }

    public boolean validateRefreshToken(String token) {
        return jwtService.validateToken(token) &&
                jwtService.isRefreshToken(token) &&
                !jwtService.isTokenExpired(token);
    }

    public String extractUserIdFromRefreshToken(String token) {
        if (!validateRefreshToken(token)) {
            throw new IllegalArgumentException("Invalid refresh token");
        }
        return jwtService.extractUserId(token);
    }
}