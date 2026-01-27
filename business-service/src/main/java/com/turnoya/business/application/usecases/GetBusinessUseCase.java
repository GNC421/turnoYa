package com.turnoya.business.application.usecases;

import com.turnoya.business.application.dto.response.BusinessResponse;
import com.turnoya.business.application.mappers.BusinessMapper;
import com.turnoya.business.domain.ports.in.BusinessServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetBusinessUseCase {

    private final BusinessServicePort businessService;

    public Optional<BusinessResponse> getById(UUID id) {
        return businessService.findById(id)
                .map(BusinessMapper::toResponse);
    }

    public List<BusinessResponse> getByOwnerId(String ownerId) {
        return businessService.findByOwnerId(ownerId)
                .stream()
                .map(BusinessMapper::toResponse)
                .collect(Collectors.toList());
    }

    public List<BusinessResponse> getByCategory(String category) {
        return businessService.findByCategory(category)
                .stream()
                .map(BusinessMapper::toResponse)
                .collect(Collectors.toList());
    }

    public List<BusinessResponse> getByCity(String city) {
        return businessService.findByCity(city)
                .stream()
                .map(BusinessMapper::toResponse)
                .collect(Collectors.toList());
    }

    public List<BusinessResponse> getAll() {
        return businessService.findAll() // Modificar según implementación
                .stream()
                .map(BusinessMapper::toResponse)
                .collect(Collectors.toList());
    }
}