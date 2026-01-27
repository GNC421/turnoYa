package com.turnoya.user.application.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ValidateCredentialsResponse {
    private String id;
    private String email;
    private String name;
    private String phone;
}