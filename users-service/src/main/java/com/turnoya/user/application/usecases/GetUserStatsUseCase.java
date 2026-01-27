package com.turnoya.user.application.usecases;

import com.turnoya.user.application.dto.request.UpdateUserRequest;
import com.turnoya.user.application.dto.response.UserResponse;
import com.turnoya.user.application.ports.input.GetUserStatsPort;
import com.turnoya.user.application.ports.output.UserRepositoryPort;
import com.turnoya.user.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetUserStatsUseCase implements GetUserStatsPort {
    private final UserRepositoryPort repository;

    @Override
    @Transactional
    public UserResponse getStats(UUID userId) {
        User user = repository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        //user.updateProfile(request.getName(), request.getPhone());
        User updated = repository.save(user);
        return toResponse(updated);
    }
}
