package com.turnoya.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouteConfig {

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                // Business API
                .route("business-api", r -> r
                        .path("/business-api/**")
                        .filters(f -> f.rewritePath("/business-api/(?<segment>.*)", "/api/v1/businesses/${segment}"))
                        .uri("http://localhost:8081"))

                // User API
                .route("user-api", r -> r
                        .path("/user-api/**")
                        .filters(f -> f.rewritePath("/user-api/(?<segment>.*)", "/api/v1/users/${segment}"))
                        .uri("http://localhost:8082"))

                // Auth API
                .route("auth-api", r -> r
                        .path("/auth-api/**")
                        .filters(f -> f.rewritePath("/auth-api/(?<segment>.*)", "/api/v1/auth/${segment}"))
                        .uri("http://localhost:8083"))

                // Reservation API
                .route("reservations-api", r -> r
                        .path("/reservations-api/**")
                        .filters(f -> f.rewritePath("/reservations-api/(?<segment>.*)", "/api/v1/reservations/${segment}"))
                        .uri("http://localhost:8085"))

                .build();
    }
}