package com.turnoya.business.application.usecases;

import com.turnoya.business.application.dto.request.RegisterBusinessRequest;
import com.turnoya.business.application.dto.response.BusinessResponse;
import com.turnoya.business.application.mappers.BusinessMapper;
import com.turnoya.business.application.ports.input.RegisterBusinessUseCasePort;
import com.turnoya.business.domain.model.Business;
import com.turnoya.business.domain.ports.out.BusinessRepositoryPort;
import com.turnoya.business.infrastructure.adapters.in.web.exception.BusinessException;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class RegisterBusinessUseCase implements RegisterBusinessUseCasePort {

    private final BusinessRepositoryPort businessRepository;

    public RegisterBusinessUseCase(BusinessRepositoryPort businessRepository) {
        this.businessRepository = businessRepository;
    }

    @Transactional
    public BusinessResponse execute(String ownerId, RegisterBusinessRequest request) {

        Business business = BusinessMapper.toDomain(ownerId, request);

        // Validar unicidad del nombre en la misma ciudad
        if (businessRepository.existsByNameAndCity(
                business.getName(), business.getAddress().city())) {
            throw new BusinessException(
                    "Ya existe un negocio con el nombre '" + business.getName() +
                            "' en la ciudad de " + business.getAddress().city(), HttpStatus.BAD_REQUEST);
        }

        // Validar que el email no esté registrado
        if (businessRepository.existsByEmail(business.getContactInfo().email())) {
            throw new BusinessException(
                    "El email " + business.getContactInfo().email() + " ya está registrado", HttpStatus.BAD_REQUEST);
        }

        // Convertir a respuesta
        return BusinessMapper.toResponse(businessRepository.save(business));
    }
}