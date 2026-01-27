package com.turnoya.user.application.ports.input;

import com.turnoya.user.application.dto.request.*;
import com.turnoya.user.application.dto.response.UserResponse;
import com.turnoya.user.application.dto.response.UserStatsResponse;

import java.util.UUID;

public interface RegisterUserPort {
    UserResponse register(RegisterUserRequest request);
}