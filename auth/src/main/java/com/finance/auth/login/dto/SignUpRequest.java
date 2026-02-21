package com.finance.auth.login.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignUpRequest(
        @NotBlank String clientId,
        @NotBlank String clientName,
        @NotBlank @Size(min = 8) String password
) {
}