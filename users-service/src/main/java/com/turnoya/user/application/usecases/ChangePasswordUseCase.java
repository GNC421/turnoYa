package com.turnoya.user.application.usecases;

import com.turnoya.user.application.dto.request.ChangePasswordRequest;
import com.turnoya.user.application.ports.input.ChangeCredentialsPort;
import com.turnoya.user.application.ports.output.UserRepositoryPort;
import com.turnoya.user.domain.model.User;
import com.turnoya.user.domain.service.PasswordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChangePasswordUseCase implements ChangeCredentialsPort {
    private final UserRepositoryPort repository;
    private final PasswordService passwordService;

    @Override
    @Transactional
    public void changePassword(UUID userId, ChangePasswordRequest request) {
        User user = repository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (!passwordService.matches(request.getCurrentPassword(), user.getPasswordHash())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }

        String newHashedPassword = passwordService.hashPassword(request.getNewPassword());
        user.setPasswordHash(newHashedPassword);
        repository.save(user);
    }
}
