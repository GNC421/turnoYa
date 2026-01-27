package com.turnoya.user.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private UUID id;
    private String email;
    private String passwordHash; // Hasheada con BCrypt
    private String name;
    private String phone;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean enabled;

    // Métodos de dominio
    public boolean isActive() {
        return enabled;
    }

    public static User create(String email, String passwordHash, String name, String phone) {
        return User.builder()
                .email(email.toLowerCase().trim())
                .passwordHash(passwordHash)
                .name(name)
                .phone(phone)
                .createdAt(LocalDateTime.now())
                .enabled(true)
                .build();
    }
}