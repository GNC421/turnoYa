package com.turnoya.user.infrastructure.adapters.output.client;

import com.turnoya.user.application.ports.output.ReservationServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ReservationServiceClient implements ReservationServicePort {

    private final RestTemplate restTemplate;
    private final String userServiceUrl = "http://localhost:8080/reservations-api";

    @Override
    public Map<UUID, Double> getPercentageBooksFailed(List<UUID> userIds) {
        String url = userServiceUrl + "/cancellation-rates";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        for (UUID user: userIds) {
            url += (url.contains("?") ? "&" : "?") + "userIds=" + user.toString();
        }

        try {
            ResponseEntity<Map> response = restTemplate.getForEntity(
                    url,
                    Map.class
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return response.getBody();
            }

            throw new RuntimeException("Invalid credentials");

        } catch (HttpClientErrorException.Unauthorized e) {
            throw new IllegalArgumentException("Invalid credentials");
        } catch (Exception e) {
            throw new RuntimeException("User service unavailable: " + e.getMessage());
        }
    }
}
