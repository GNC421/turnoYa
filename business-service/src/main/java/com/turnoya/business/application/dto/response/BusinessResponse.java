package com.turnoya.business.application.dto.response;

import com.turnoya.business.domain.model.BusinessCategory;
import com.turnoya.business.domain.model.BusinessStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record BusinessResponse(
        UUID id,
        String ownerId,
        String name,
        String description,
        BusinessCategory category,
        String street,
        String number,
        String city,
        String state,
        String zipCode,
        String country,
        String email,
        String phone,
        String website,
        BusinessStatus status,
        LocalDateTime registrationDate,
        LocalDateTime lastUpdated
) {}