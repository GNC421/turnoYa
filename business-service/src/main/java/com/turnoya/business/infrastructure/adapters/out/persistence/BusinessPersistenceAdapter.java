package com.turnoya.business.infrastructure.adapters.out.persistence;

import com.turnoya.business.domain.model.Business;
import com.turnoya.business.domain.ports.out.BusinessRepositoryPort;
import com.turnoya.business.infrastructure.adapters.out.persistence.entity.BusinessEntity;
import com.turnoya.business.infrastructure.adapters.out.persistence.mapper.BusinessEntityMapper;
import com.turnoya.business.infrastructure.adapters.out.persistence.repository.BusinessJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class BusinessPersistenceAdapter implements BusinessRepositoryPort {

    private final BusinessJpaRepository jpaRepository;
    private final BusinessEntityMapper mapper;

    @Override
    public Business save(Business business) {
        BusinessEntity entity = mapper.toEntity(business);
        BusinessEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Business> findById(UUID id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public List<Business> findByOwnerId(String ownerId) {

        List<BusinessEntity> entities = jpaRepository.findByOwnerId(ownerId);

        if (entities.isEmpty()) {
            return Collections.emptyList();
        }

        return entities.stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Business> findByCategory(String category) {
        List<BusinessEntity> entities = jpaRepository.findByCategory(category);

        if (entities.isEmpty()) {
            return Collections.emptyList();
        }

        return entities.stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Business> findByCity(String city) {
        return jpaRepository.findByCity(city)
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Business> findAll() {
        return jpaRepository.findAll()
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public boolean existsByNameAndCity(String name, String city) {
        return jpaRepository.existsByNameAndCity(name, city);
    }

    @Override
    public boolean existsByEmail(String email) {
        return jpaRepository.existsByEmail(email);
    }
}