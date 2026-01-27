package com.turnoya.business.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
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
                                "/api/v1/businesses/**",  // Todo el registro de negocios
                                "/favicon.ico",            //favicon
                                "/api/test/**",           // Endpoints de prueba
                                "/h2-console/**",         // Consola H2
                                "/h2-console",
                                "/db-info",
                                "/actuator/health",       // Health check
                                "/swagger-ui/**",         // Swagger UI
                                "/swagger-ui.html",
                                "/swagger-resources/**",
                                "/api-docs/**",         // OpenAPI docs
                                "/api-docs"
                        ).permitAll()

                        // Cualquier otro request requiere autenticación
                        .anyRequest().authenticated()
                )

                // Configuración para H2 Console (solo desarrollo)
                .headers(headers -> headers
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::disable) // Permitir frames para H2
                );

                // Desactivar autenticación básica por ahora
                // En producción usarás JWT o OAuth2
                //.httpBasic(AbstractHttpConfigurer::disable);

        return http.build();
    }
}