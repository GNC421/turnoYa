package com.turnoya.user.application.usecases;

import com.turnoya.user.application.dto.request.ValidateCredentialsRequest;
import com.turnoya.user.application.dto.response.ValidateCredentialsResponse;
import com.turnoya.user.application.ports.input.ValidateCredentialsPort;
import com.turnoya.user.application.ports.output.UserRepositoryPort;
import com.turnoya.user.domain.model.User;
import com.turnoya.user.domain.service.PasswordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ValidateCredentialsUseCase implements ValidateCredentialsPort {

    private final UserRepositoryPort userRepository;
    private final PasswordService passwordService;

    @Override
    public ValidateCredentialsResponse execute(ValidateCredentialsRequest request) {
        System.out.println("LOG: REQ BODY = " + request.getEmail());
        System.out.println("LOG: REQ BODY = " + request.getPassword());
        // 1. Buscar usuario por email
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));

        // 2. Validar password
        if (!passwordService.matches(request.getPassword(), user.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        // 3. Validar que el usuario esté activo
        if (!user.isActive()) {
            throw new IllegalStateException("Account is disabled");
        }

        // 4. Retornar respuesta
        return ValidateCredentialsResponse.builder()
                .id(user.getId().toString())
                .email(user.getEmail())
                .name(user.getName())
                .phone(user.getPhone())
                .build();
    }
}