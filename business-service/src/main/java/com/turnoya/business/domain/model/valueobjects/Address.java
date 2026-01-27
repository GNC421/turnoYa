package com.turnoya.business.domain.model.valueobjects;

public record Address(
        String street,
        String number,
        String city,
        String state,
        String zipCode,
        String country
) {
    public Address {
        if (street == null || street.trim().isEmpty()) {
            throw new IllegalArgumentException("La calle es requerida");
        }
        if (city == null || city.trim().isEmpty()) {
            throw new IllegalArgumentException("La ciudad es requerida");
        }
        if (country == null || country.trim().isEmpty()) {
            throw new IllegalArgumentException("El país es requerido");
        }
    }

    public String getFullAddress() {
        return String.format("%s %s, %s, %s %s",
                street, number, city, state, zipCode);
    }
}
