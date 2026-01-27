package com.turnoya.business.domain.model.valueobjects;

public record ContactInfo(
        String email,
        String phone,
        String website
) {
    public ContactInfo {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("El email es requerido");
        }
        if (phone == null || phone.trim().isEmpty()) {
            throw new IllegalArgumentException("El teléfono es requerido");
        }

        // Validación básica de email
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("Email inválido");
        }

        // Validación básica de teléfono (al menos 8 dígitos)
        String digitsOnly = phone.replaceAll("\\D", "");
        if (digitsOnly.length() < 8) {
            throw new IllegalArgumentException("Teléfono inválido");
        }
    }
}
