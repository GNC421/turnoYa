package com.turnoya.business.application.usecases;

import com.turnoya.business.application.dto.request.RegisterBusinessRequest;
import com.turnoya.business.application.dto.response.BusinessResponse;
import com.turnoya.business.application.mappers.BusinessMapper;
import com.turnoya.business.domain.model.Business;
import com.turnoya.business.domain.ports.in.BusinessServicePort;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class RegisterBusinessUseCase {

    private final BusinessServicePort businessService;

    public RegisterBusinessUseCase(BusinessServicePort businessService) {
        this.businessService = businessService;
    }

    public BusinessResponse execute(String ownerId, RegisterBusinessRequest request) {
        // Convertir request a comando usando el ownerId
        var command = BusinessMapper.toCommand(ownerId, request);

        // Convertir comando a dominio
        Business business = BusinessMapper.toDomain(command);

        System.out.println("LOG-GNC toRegister: " + business);

        // Registrar negocio usando el puerto de entrada
        Business registeredBusiness = businessService.registerBusiness(business);

        System.out.println("LOG-GNC REGISTRADA: " + registeredBusiness);

        // Convertir a respuesta
        return BusinessMapper.toResponse(registeredBusiness);
    }
}