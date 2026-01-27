package com.turnoya.business.infrastructure.adapters.out.persistence.mapper;

import com.turnoya.business.domain.model.*;
import com.turnoya.business.domain.model.valueobjects.Address;
import com.turnoya.business.domain.model.valueobjects.ContactInfo;
import com.turnoya.business.infrastructure.adapters.out.persistence.entity.BusinessEntity;
import com.turnoya.business.infrastructure.adapters.out.persistence.entity.BusinessCategoryEntity;
import com.turnoya.business.infrastructure.adapters.out.persistence.entity.BusinessStatusEntity;
import org.springframework.stereotype.Component;

@Component
public class BusinessEntityMapper {

    public BusinessEntity toEntity(Business domain) {
        return BusinessEntity.builder()
                .id(domain.getId())
                .ownerId(domain.getOwnerId())
                .name(domain.getName())
                .description(domain.getDescription())
                .category(mapCategoryToEntity(domain.getCategory()))
                .street(domain.getAddress().street())
                .number(domain.getAddress().number())
                .city(domain.getAddress().city())
                .state(domain.getAddress().state())
                .zipCode(domain.getAddress().zipCode())
                .country(domain.getAddress().country())
                .email(domain.getContactInfo().email())
                .phone(domain.getContactInfo().phone())
                .website(domain.getContactInfo().website())
                .status(mapStatusToEntity(domain.getStatus()))
                .registrationDate(domain.getRegistrationDate())
                .lastUpdated(domain.getLastUpdated())
                .build();
    }

    public Business toDomain(BusinessEntity entity) {
        Address address = new Address(
                entity.getStreet(),
                entity.getNumber(),
                entity.getCity(),
                entity.getState(),
                entity.getZipCode(),
                entity.getCountry()
        );

        ContactInfo contactInfo = new ContactInfo(
                entity.getEmail(),
                entity.getPhone(),
                entity.getWebsite()
        );

        return Business.from(
                entity.getId(),
                entity.getOwnerId(),
                entity.getName(),
                entity.getDescription(),
                mapCategoryToDomain(entity.getCategory()),
                address,
                contactInfo,
                mapStatusToDomain(entity.getStatus()),
                entity.getRegistrationDate(),
                entity.getLastUpdated()
        );
    }

    private BusinessCategoryEntity mapCategoryToEntity(BusinessCategory domain) {
        return BusinessCategoryEntity.valueOf(domain.name());
    }

    private BusinessCategory mapCategoryToDomain(BusinessCategoryEntity entity) {
        return BusinessCategory.valueOf(entity.name());
    }

    private BusinessStatusEntity mapStatusToEntity(BusinessStatus domain) {
        return BusinessStatusEntity.valueOf(domain.name());
    }

    private BusinessStatus mapStatusToDomain(BusinessStatusEntity entity) {
        return BusinessStatus.valueOf(entity.name());
    }
}