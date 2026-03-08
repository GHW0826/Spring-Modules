package com.finance.auth.login.dto;

import jakarta.validation.constraints.NotBlank;

public record SignInRequest(
        @NotBlank String clientId,
        @NotBlank String password
) {
}