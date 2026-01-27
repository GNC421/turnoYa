package com.turnoya.user.application.ports.input;

import com.turnoya.user.application.dto.request.UpdateUserRequest;
import com.turnoya.user.application.dto.response.UserResponse;

import java.util.UUID;

public interface UpdateUserProfilePort {
    UserResponse updateProfile(UUID userId, UpdateUserRequest request);
}
