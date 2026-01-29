package com.turnoya.user.application.usecases;

import com.turnoya.user.application.dto.request.UpdateUserRequest;
import com.turnoya.user.application.dto.response.UserResponse;
import com.turnoya.user.application.ports.input.UpdateUserProfilePort;
import com.turnoya.user.application.ports.output.UserRepositoryPort;
import com.turnoya.user.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UpdateProfileUseCase implements UpdateUserProfilePort {
    private final UserRepositoryPort repository;

    @Override
    @Transactional
    public UserResponse updateProfile(UUID userId, UpdateUserRequest request) {
        User user = repository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        user.updateProfile(request.getName(), request.getPhone());
        User updated = repository.save(user);
        return UserResponse.builder()
                .id(updated.getId().toString())
                .email(updated.getEmail())
                .name(updated.getName())
                .phone(updated.getPhone())
                .createdAt(updated.getCreatedAt())
                .updatedAt(updated.getUpdatedAt())
                .enabled(updated.isActive())
                .build();
    }
}
