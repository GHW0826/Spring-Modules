package com.finance.auth.login.dto;

public record SignUpResponse(
        Long id,
        String clientId,
        String clientName,
        String message // optional
) {
}