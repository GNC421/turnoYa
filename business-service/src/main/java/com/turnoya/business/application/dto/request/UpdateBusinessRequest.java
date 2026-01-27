package com.turnoya.business.application.dto.request;

import com.turnoya.business.domain.model.BusinessCategory;
import jakarta.validation.constraints.*;

public record UpdateBusinessRequest(
        @NotBlank(message = "El nombre del negocio es requerido")
        @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
        String name,

        @Size(max = 500, message = "La descripción no puede exceder 500 caracteres")
        String description,

        @NotNull(message = "La categoría es requerida")
        BusinessCategory category,

        // Dirección
        @NotBlank(message = "La calle es requerida")
        String street,

        @NotBlank(message = "El número es requerido")
        String number,

        @NotBlank(message = "La ciudad es requerida")
        String city,

        String state,

        @NotBlank(message = "El código postal es requerido")
        @Pattern(regexp = "^[0-9]{4,10}$", message = "Código postal inválido")
        String zipCode,

        @NotBlank(message = "El país es requerido")
        String country,

        // Información de contacto
        @NotBlank(message = "El email es requerido")
        @Email(message = "Email inválido")
        String email,

        @NotBlank(message = "El teléfono es requerido")
        @Pattern(regexp = "^[+]*[(]{0,1}[0-9]{1,4}[)]{0,1}[-\\s\\./0-9]*$",
                message = "Teléfono inválido")
        String phone,

        @Pattern(regexp = "^(https?:\\/\\/)?([\\da-z\\.-]+)\\.([a-z\\.]{2,6})([\\/\\w \\.-]*)*\\/?$",
                message = "URL de sitio web inválida")
        String website
) {}