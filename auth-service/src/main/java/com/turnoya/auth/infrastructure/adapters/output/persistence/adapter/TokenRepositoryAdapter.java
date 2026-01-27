package com.turnoya.auth.infrastructure.adapters.output.persistence.adapter;

import com.turnoya.auth.application.ports.output.TokenRepositoryPort;
import com.turnoya.auth.infrastructure.adapters.output.persistence.entity.RefreshTokenEntity;
import com.turnoya.auth.infrastructure.adapters.output.persistence.repository.RefreshTokenRepository;
import com.turnoya.auth.infrastructure.config.JwtConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class TokenRepositoryAdapter implements TokenRepositoryPort {

    private final RefreshTokenRepository repository;
    private final JwtConfig jwtConfig;

    @Override
    public void saveRefreshToken(String userId, String token) {
        System.out.println("LOG: userId=" + userId);
        // Revocar tokens anteriores del usuario
        repository.revokeAllByUserId(userId);

        // Crear nuevo token
        RefreshTokenEntity entity = new RefreshTokenEntity();
        entity.setUserId(userId);
        entity.setToken(token);
        entity.setExpiresAt(LocalDateTime.now().plusSeconds(jwtConfig.getRefreshTokenExpiration()));

        repository.save(entity);
    }

    @Override
    public boolean isValidRefreshToken(String userId, String token) {
        return repository.findByTokenAndRevokedFalse(token)
                .map(entity ->
                        entity.getUserId().equals(userId) &&
                                !entity.isRevoked() &&
                                entity.getExpiresAt().isAfter(LocalDateTime.now())
                )
                .orElse(false);
    }

    @Override
    public void invalidateRefreshToken(String userId) {
        repository.revokeAllByUserId(userId);
    }
}