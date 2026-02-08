package com.turnoya.business.application.dto.request;

import jakarta.validation.constraints.Size;

public record BusinessSearchRequest(
    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
    String name,
    String category,
    String city,
    String status,
    int page,
    int size
) {}