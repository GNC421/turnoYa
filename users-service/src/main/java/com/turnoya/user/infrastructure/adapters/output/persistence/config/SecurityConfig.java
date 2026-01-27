package com.turnoya.user.infrastructure.adapters.output.persistence.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Desactivar CSRF para desarrollo (en producción debes configurarlo)
                .csrf(AbstractHttpConfigurer::disable)

                // Configurar autorización de endpoints
                .authorizeHttpRequests(auth -> auth
                        // Endpoints públicos (sin autenticación)
                        .requestMatchers(
                                "/api/v1/users/**",  // Todo el registro de negocios
                                "/favicon.ico",            //favicon
                                "/api/test/**",           // Endpoints de prueba
                                "/actuator/health",       // Health check
                                "/swagger-ui/**",         // Swagger UI
                                "/swagger-ui.html",
                                "/swagger-resources/**",
                                "/api-docs/**",         // OpenAPI docs
                                "/api-docs"
                        ).permitAll()

                        // Cualquier otro request requiere autenticación
                        .anyRequest().authenticated()
                );

        // Desactivar autenticación básica por ahora
        // En producción usarás JWT o OAuth2
        //.httpBasic(AbstractHttpConfigurer::disable);

        return http.build();
    }
}