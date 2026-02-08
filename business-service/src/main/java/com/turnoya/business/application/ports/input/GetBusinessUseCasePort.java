package com.turnoya.business.application.ports.input;

import com.turnoya.business.application.dto.response.BusinessResponse;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GetBusinessUseCasePort {
    Optional<BusinessResponse> getById(UUID id);
    List<BusinessResponse> getByOwnerId(String ownerId);
    List<BusinessResponse> getAll();
}
