package com.turnoya.business.application.usecases;

import com.turnoya.business.application.dto.response.BusinessResponse;
import com.turnoya.business.application.mappers.BusinessMapper;
import com.turnoya.business.application.ports.input.GetBusinessUseCasePort;
import com.turnoya.business.domain.ports.out.BusinessRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetBusinessUseCase implements GetBusinessUseCasePort {

    private final BusinessRepositoryPort businessRepositoryPort;

    public Optional<BusinessResponse> getById(UUID id) {
        return businessRepositoryPort.findById(id)
                .map(BusinessMapper::toResponse);
    }

    public List<BusinessResponse> getByOwnerId(String ownerId) {
        return businessRepositoryPort.findByOwnerId(ownerId)
                .stream()
                .map(BusinessMapper::toResponse)
                .collect(Collectors.toList());
    }

    public List<BusinessResponse> getAll() {
        return businessRepositoryPort.findAll()
                .stream()
                .map(BusinessMapper::toResponse)
                .collect(Collectors.toList());
    }
}