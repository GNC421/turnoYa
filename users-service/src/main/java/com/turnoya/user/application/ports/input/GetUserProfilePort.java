package com.turnoya.user.application.ports.input;

import com.turnoya.user.application.dto.request.UpdateUserRequest;
import com.turnoya.user.application.dto.response.UserResponse;

import java.util.UUID;

public interface GetUserProfilePort {
    UserResponse getProfile(UUID userId);
}
