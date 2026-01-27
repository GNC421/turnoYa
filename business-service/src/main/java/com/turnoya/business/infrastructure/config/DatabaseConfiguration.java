package com.turnoya.business.infrastructure.config;

import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EntityScan(basePackages = "com.turnoya.business.infrastructure.adapters.out.persistence.entity")
@EnableJpaRepositories(basePackages = "com.turnoya.business.infrastructure.adapters.out.persistence.repository")
public class DatabaseConfiguration {
    // Configuración automática de Spring Data JPA
}