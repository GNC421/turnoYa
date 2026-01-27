package com.turnoya.auth.application.ports.output;

public interface TokenRepositoryPort {
    void saveRefreshToken(String userId, String token);
    boolean isValidRefreshToken(String userId, String token);
    void invalidateRefreshToken(String userId);
}