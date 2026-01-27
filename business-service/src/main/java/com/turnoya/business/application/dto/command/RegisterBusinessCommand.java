package com.turnoya.business.application.dto.command;

import com.turnoya.business.domain.model.BusinessCategory;
import com.turnoya.business.domain.model.valueobjects.Address;
import com.turnoya.business.domain.model.valueobjects.ContactInfo;

public record RegisterBusinessCommand(
        String ownerId,
        String name,
        String description,
        BusinessCategory category,
        Address address,
        ContactInfo contactInfo
) {
    public static RegisterBusinessCommand from(
            String ownerId,
            String name,
            String description,
            BusinessCategory category,
            String street,
            String number,
            String city,
            String state,
            String zipCode,
            String country,
            String email,
            String phone,
            String website
    ) {
        Address address = new Address(street, number, city, state, zipCode, country);
        ContactInfo contactInfo = new ContactInfo(email, phone, website);

        return new RegisterBusinessCommand(
                ownerId, name, description, category, address, contactInfo
        );
    }
}