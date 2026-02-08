package com.turnoya.business.domain.ports.out;

import com.turnoya.business.domain.model.Business;
import com.turnoya.business.domain.model.BusinessSearchCriteria;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BusinessRepositoryPort {
    Business save(Business business);
    Optional<Business> findById(UUID id);
    List<Business> findByOwnerId(String ownerId);
    List<Business> findByCategory(String category);
    List<Business> findByCity(String city);
    List<Business> findAll();
    void deleteById(UUID id);
    boolean existsByNameAndCity(String name, String city);
    boolean existsByEmail(String email);
    List<Business> search(BusinessSearchCriteria criteria);
}