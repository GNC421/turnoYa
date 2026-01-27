package com.turnoya.business.domain.model;

import com.turnoya.business.domain.model.valueobjects.Address;
import com.turnoya.business.domain.model.valueobjects.ContactInfo;

import java.time.LocalDateTime;
import java.util.UUID;

public class Business {
    private UUID id;
    private String ownerId;           // ID del dueño (vendrá del Auth Service)
    private String name;
    private String description;
    private BusinessCategory category;
    private Address address;
    private ContactInfo contactInfo;
    private BusinessStatus status;
    private LocalDateTime registrationDate;
    private LocalDateTime lastUpdated;

    // Constructor privado para factory methods
    private Business() {}

    // Factory method para registrar nuevo negocio
    public static Business register(
        String ownerId,
        String name,
        String description,
        BusinessCategory category,
        Address address,
        ContactInfo contactInfo
    ) {
        Business business = new Business();
        business.ownerId = ownerId;
        business.name = name;
        business.description = description;
        business.category = category;
        business.address = address;
        business.contactInfo = contactInfo;
        business.status = BusinessStatus.PENDING; // Inicia como pendiente
        business.registrationDate = LocalDateTime.now();
        business.lastUpdated = LocalDateTime.now();

        business.validate();
        return business;
    }

    // Factory method para reconstruir desde persistencia
    public static Business from(
        UUID id,
        String ownerId,
        String name,
        String description,
        BusinessCategory category,
        Address address,
        ContactInfo contactInfo,
        BusinessStatus status,
        LocalDateTime registrationDate,
        LocalDateTime lastUpdated
    ) {
        Business business = new Business();
        business.id = id;
        business.ownerId = ownerId;
        business.name = name;
        business.description = description;
        business.category = category;
        business.address = address;
        business.contactInfo = contactInfo;
        business.status = status;
        business.registrationDate = registrationDate;
        business.lastUpdated = lastUpdated;

        business.validate();
        return business;
    }

    private void validate() {
        if (ownerId == null || ownerId.trim().isEmpty()) {
            throw new IllegalArgumentException("Owner ID es requerido");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Nombre del negocio es requerido");
        }
        if (name.length() > 100) {
            throw new IllegalArgumentException("Nombre no puede exceder 100 caracteres");
        }
        if (description != null && description.length() > 500) {
            throw new IllegalArgumentException("Descripción no puede exceder 500 caracteres");
        }
        if (category == null) {
            throw new IllegalArgumentException("Categoría es requerida");
        }
    }

    // Métodos del dominio
    public void activate() {
        if (this.status != BusinessStatus.PENDING) {
            throw new IllegalStateException("Solo se pueden activar negocios pendientes");
        }
        this.status = BusinessStatus.ACTIVE;
        this.lastUpdated = LocalDateTime.now();
    }

    public void suspend() {
        if (this.status != BusinessStatus.ACTIVE) {
            throw new IllegalStateException("Solo se pueden suspender negocios activos");
        }
        this.status = BusinessStatus.SUSPENDED;
        this.lastUpdated = LocalDateTime.now();
    }

    public void reactivate() {
        if (this.status != BusinessStatus.SUSPENDED) {
            throw new IllegalStateException("Solo se pueden reactivar negocios suspendidos");
        }
        this.status = BusinessStatus.ACTIVE;
        this.lastUpdated = LocalDateTime.now();
    }

    public void deactivate() {
        this.status = BusinessStatus.INACTIVE;
        this.lastUpdated = LocalDateTime.now();
    }

    public void updateInfo(
        String name,
        String description,
        BusinessCategory category,
        Address address,
        ContactInfo contactInfo
    ) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.address = address;
        this.contactInfo = contactInfo;
        this.lastUpdated = LocalDateTime.now();
        validate();
    }

    public boolean isActive() {
        return this.status == BusinessStatus.ACTIVE;
    }

    public boolean canAcceptAppointments() {
        return this.status == BusinessStatus.ACTIVE;
    }

    // Getters (sin setters públicos para mantener inmutabilidad)
    public UUID getId() { return id; }
    public String getOwnerId() { return ownerId; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public BusinessCategory getCategory() { return category; }
    public Address getAddress() { return address; }
    public ContactInfo getContactInfo() { return contactInfo; }
    public BusinessStatus getStatus() { return status; }
    public LocalDateTime getRegistrationDate() { return registrationDate; }
    public LocalDateTime getLastUpdated() { return lastUpdated; }

    @Override
    public String toString() {
        return "Business{" +
                "id=" + id +
                ", ownerId='" + ownerId + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", category=" + category +
                ", address=" + address +
                ", contactInfo=" + contactInfo +
                ", status=" + status +
                ", registrationDate=" + registrationDate +
                ", lastUpdated=" + lastUpdated +
                '}';
    }
}