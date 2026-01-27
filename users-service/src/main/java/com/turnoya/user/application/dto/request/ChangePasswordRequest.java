package com.turnoya.user.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ChangePasswordRequest {
    @NotBlank @Size(min = 8)
    private String currentPassword;
    @NotBlank @Size(min = 8)
    private String newPassword;
}