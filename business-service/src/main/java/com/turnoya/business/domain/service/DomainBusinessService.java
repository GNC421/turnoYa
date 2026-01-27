package com.turnoya.business.domain.service;

import com.turnoya.business.domain.model.Business;
import com.turnoya.business.domain.ports.in.BusinessServicePort;
import com.turnoya.business.domain.ports.out.BusinessRepositoryPort;
import com.turnoya.business.infrastructure.adapters.in.web.exception.BusinessException;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class DomainBusinessService implements BusinessServicePort {

    private final BusinessRepositoryPort businessRepository;

    public DomainBusinessService(BusinessRepositoryPort businessRepository) {
        this.businessRepository = businessRepository;
    }

    @Override
    @Transactional
    public Business registerBusiness(Business business) {
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

        System.out.println("VALIDADA");

        return businessRepository.save(business);
    }

    @Override
    public Optional<Business> findById(UUID id) {
        return businessRepository.findById(id);
    }

    @Override
    public List<Business> findByOwnerId(String ownerId) {
        System.out.println("LOG-GNC BY OWNER: " + ownerId);
        return businessRepository.findByOwnerId(ownerId);
    }

    @Override
    public List<Business> findByCategory(String category) {
        return businessRepository.findByCategory(category);
    }

    @Override
    public List<Business> findByCity(String city) {
        return businessRepository.findByCity(city);
    }

    @Override
    public List<Business> findAll() {
        return businessRepository.findAll();
    }

    @Override
    @Transactional
    public Business updateBusiness(UUID id, Business updatedBusiness) {
        Business existing = businessRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Negocio no encontrado"));

        // Validar unicidad si cambió el nombre
        if (!existing.getName().equals(updatedBusiness.getName())) {
            if (businessRepository.existsByNameAndCity(
                    updatedBusiness.getName(), existing.getAddress().city())) {
                throw new IllegalArgumentException(
                        "Ya existe un negocio con el nombre: " + updatedBusiness.getName() +
                                " en la ciudad de " + existing.getAddress().city());
            }
        }

        existing.updateInfo(
                updatedBusiness.getName(),
                updatedBusiness.getDescription(),
                updatedBusiness.getCategory(),
                updatedBusiness.getAddress(),
                updatedBusiness.getContactInfo()
        );

        return businessRepository.save(existing);
    }

    @Override
    @Transactional
    public void deleteBusiness(UUID id) {
        businessRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Business activateBusiness(UUID id) {
        Business business = businessRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Negocio no encontrado"));

        business.activate();
        return businessRepository.save(business);
    }

    @Override
    @Transactional
    public Business suspendBusiness(UUID id) {
        Business business = businessRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Negocio no encontrado"));

        business.suspend();
        return businessRepository.save(business);
    }

    @Override
    @Transactional
    public Business deactivateBusiness(UUID id) {
        Business business = businessRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Negocio no encontrado"));

        business.deactivate();
        return businessRepository.save(business);
    }

    @Override
    public boolean existsByNameAndCity(String name, String city) {
        return businessRepository.existsByNameAndCity(name, city);
    }
}