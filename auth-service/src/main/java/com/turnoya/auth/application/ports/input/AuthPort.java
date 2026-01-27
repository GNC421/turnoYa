package com.turnoya.auth.application.ports.input;

import com.turnoya.auth.application.dto.request.LoginRequest;
import com.turnoya.auth.application.dto.response.AuthResponse;

public interface AuthPort {
    AuthResponse login(LoginRequest request);
}