package com.turnoya.business.domain.ports.in;

import com.turnoya.business.domain.model.Business;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BusinessServicePort {
    Business registerBusiness(Business business);
    Optional<Business> findById(UUID id);
    List<Business> findByOwnerId(String ownerId);
    List<Business> findByCategory(String category);
    List<Business> findByCity(String city);
    List<Business> findAll();
    Business updateBusiness(UUID id, Business updatedBusiness);
    void deleteBusiness(UUID id);
    Business activateBusiness(UUID id);
    Business suspendBusiness(UUID id);
    Business deactivateBusiness(UUID id);
    boolean existsByNameAndCity(String name, String city);
}