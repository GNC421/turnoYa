package com.turnoya.business.infrastructure.adapters.out.persistence;

import com.turnoya.business.domain.model.Business;
import com.turnoya.business.domain.model.BusinessSearchCriteria;
import com.turnoya.business.domain.ports.out.BusinessRepositoryPort;
import com.turnoya.business.infrastructure.adapters.out.persistence.entity.BusinessCategoryEntity;
import com.turnoya.business.infrastructure.adapters.out.persistence.entity.BusinessEntity;
import com.turnoya.business.infrastructure.adapters.out.persistence.entity.BusinessStatusEntity;
import com.turnoya.business.infrastructure.adapters.out.persistence.mapper.BusinessEntityMapper;
import com.turnoya.business.infrastructure.adapters.out.persistence.repository.BusinessJpaRepository;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.*;
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

    @Override
    public List<Business> search(BusinessSearchCriteria criteria) {
        Specification<BusinessEntity> spec = buildSpecification(criteria);

        List<BusinessEntity> entityList = jpaRepository.findAll(spec, criteria.getPageable()).getContent();
        return entityList.stream().map(mapper::toDomain).toList();
    }

    private Specification<BusinessEntity> buildSpecification(BusinessSearchCriteria criteria) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (criteria.hasName()) {
                predicates.add(cb.like(
                        cb.lower(root.get("name")),
                        "%" + criteria.getName().toLowerCase() + "%"
                ));
            }

            if (criteria.hasCategory()) {
                predicates.add(cb.equal(
                        root.get("category"),
                        BusinessCategoryEntity.valueOf(criteria.getCategory().toUpperCase())
                ));
            }

            if (criteria.hasCity()) {
                predicates.add(cb.equal(
                        cb.lower(root.get("city")),
                        criteria.getCity().toLowerCase()
                ));
            }

            if (criteria.hasStatus()) {
                predicates.add(cb.equal(
                        root.get("status"),
                        BusinessStatusEntity.valueOf(criteria.getStatus().toUpperCase())
                ));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}