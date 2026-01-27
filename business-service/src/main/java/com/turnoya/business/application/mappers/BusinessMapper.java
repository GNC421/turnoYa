package com.turnoya.business.application.mappers;

import com.turnoya.business.domain.model.Business;
import com.turnoya.business.domain.model.valueobjects.Address;
import com.turnoya.business.domain.model.valueobjects.ContactInfo;
import com.turnoya.business.application.dto.command.RegisterBusinessCommand;
import com.turnoya.business.application.dto.request.RegisterBusinessRequest;
import com.turnoya.business.application.dto.request.UpdateBusinessRequest;
import com.turnoya.business.application.dto.response.BusinessResponse;

public class BusinessMapper {

    public static Business toDomain(RegisterBusinessCommand command) {
        return Business.register(
                command.ownerId(),
                command.name(),
                command.description(),
                command.category(),
                command.address(),
                command.contactInfo()
        );
    }

    public static BusinessResponse toResponse(Business business) {
        return new BusinessResponse(
                business.getId(),
                business.getOwnerId(),
                business.getName(),
                business.getDescription(),
                business.getCategory(),
                business.getAddress().street(),
                business.getAddress().number(),
                business.getAddress().city(),
                business.getAddress().state(),
                business.getAddress().zipCode(),
                business.getAddress().country(),
                business.getContactInfo().email(),
                business.getContactInfo().phone(),
                business.getContactInfo().website(),
                business.getStatus(),
                business.getRegistrationDate(),
                business.getLastUpdated()
        );
    }

    public static RegisterBusinessCommand toCommand(
            String ownerId,
            RegisterBusinessRequest request
    ) {
        return RegisterBusinessCommand.from(
                ownerId,
                request.name(),
                request.description(),
                request.category(),
                request.street(),
                request.number(),
                request.city(),
                request.state(),
                request.zipCode(),
                request.country(),
                request.email(),
                request.phone(),
                request.website()
        );
    }

    public static Business updateDomainFromRequest(
            Business business,
            UpdateBusinessRequest request
    ) {
        Address address = new Address(
                request.street(),
                request.number(),
                request.city(),
                request.state(),
                request.zipCode(),
                request.country()
        );

        ContactInfo contactInfo = new ContactInfo(
                request.email(),
                request.phone(),
                request.website()
        );

        business.updateInfo(
                request.name(),
                request.description(),
                request.category(),
                address,
                contactInfo
        );

        return business;
    }
}