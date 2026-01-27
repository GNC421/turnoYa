package com.turnoya.auth.application.ports.output;

import java.util.Map;

public interface UserServicePort {
    Map<String, Object> validateCredentials(String email, String password);
}