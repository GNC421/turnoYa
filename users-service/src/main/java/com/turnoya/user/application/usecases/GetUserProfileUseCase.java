package com.turnoya.user.application.usecases;

import com.turnoya.user.application.dto.response.UserResponse;
import com.turnoya.user.application.ports.input.GetUserProfilePort;
import com.turnoya.user.application.ports.output.UserRepositoryPort;
import com.turnoya.user.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetUserProfileUseCase implements GetUserProfilePort {
    private final UserRepositoryPort repository;

    @Override
    public UserResponse getProfile(UUID userId) {
        User user = repository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
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
