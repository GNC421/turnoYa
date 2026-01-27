package com.turnoya.business.domain.model;

public enum BusinessStatus {
    PENDING("Pendiente"),      // Registrado pero no verificado
    ACTIVE("Activo"),          // Verificado y operativo
    SUSPENDED("Suspendido"),   // Temporalmente inactivo
    INACTIVE("Inactivo");    // Cerrado permanentemente

    private final String displayName;

    BusinessStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}