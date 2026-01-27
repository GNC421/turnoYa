package com.turnoya.auth.infrastructure.adapters.output.client;

import com.turnoya.auth.application.dto.request.LoginRequest;
import com.turnoya.auth.application.ports.output.UserServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class UserServiceClient implements UserServicePort {

    private final RestTemplate restTemplate;

    private final String userServiceUrl = "http://localhost:8080/user-api";

    @Override
    public Map<String, Object> validateCredentials(String email, String password) {
        String url = userServiceUrl + "/validate";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        LoginRequest requestBody = new LoginRequest(email, password);
        HttpEntity<LoginRequest> request = new HttpEntity<>(requestBody, headers);
        System.out.println("LOG: HTTP REQ BODY = " + request.getBody());

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(
                    url,
                    request,
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