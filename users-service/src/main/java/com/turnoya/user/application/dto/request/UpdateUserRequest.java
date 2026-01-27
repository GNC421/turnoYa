package com.turnoya.user.application.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateUserRequest {
    @Size(min = 2, max = 100)
    private String name;
    @Size(min = 9, max = 20)
    private String phone;
}