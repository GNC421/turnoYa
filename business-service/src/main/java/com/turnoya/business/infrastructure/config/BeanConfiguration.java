package com.turnoya.business.infrastructure.config;

import com.turnoya.business.domain.ports.in.BusinessServicePort;
import com.turnoya.business.domain.ports.out.BusinessRepositoryPort;
import com.turnoya.business.domain.service.DomainBusinessService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    @Bean
    public BusinessServicePort businessService(BusinessRepositoryPort businessRepository) {
        return new DomainBusinessService(businessRepository);
    }
}