package com.turnoya.user.application.usecases;

import com.turnoya.user.application.dto.request.RegisterUserRequest;
import com.turnoya.user.application.dto.request.UpdateUserRequest;
import com.turnoya.user.application.dto.response.UserResponse;
import com.turnoya.user.application.ports.input.RegisterUserPort;
import com.turnoya.user.application.ports.output.UserRepositoryPort;
import com.turnoya.user.domain.model.User;
import com.turnoya.user.domain.service.PasswordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RegisterUserUseCase implements RegisterUserPort {
    private final UserRepositoryPort repository;
    private final PasswordService passwordService;

    @Override
    @Transactional
    public UserResponse register(RegisterUserRequest request) {
        String email = request.getEmail().toLowerCase().trim();

        if (repository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already registered");
        }

        String hashedPassword = passwordService.hashPassword(request.getPassword());
        User user = User.create(email, hashedPassword, request.getName(), request.getPhone());

        User saved = repository.save(user);
        System.out.println("LOG: FALLO DE SAVE");
        return toResponse(saved);
    }

    private UserResponse toResponse(User user) {
        return UserResponse.builder()
                .id(user.getId().toString())
                .email(user.getEmail())
                .name(user.getName())
                .phone(user.getPhone())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .enabled(user.isActive())
                .build();
    }
}