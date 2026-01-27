package com.turnoya.user.application.ports.input;

import com.turnoya.user.application.dto.request.ChangePasswordRequest;

import java.util.UUID;

public interface ChangeCredentialsPort {
    void changePassword(UUID userId, ChangePasswordRequest request);
}
