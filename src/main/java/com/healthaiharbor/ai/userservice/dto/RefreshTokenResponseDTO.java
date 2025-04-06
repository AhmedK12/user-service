package com.healthaiharbor.ai.userservice.dto;

import jakarta.validation.constraints.NotBlank;

public record RefreshTokenResponseDTO(
        @NotBlank(message = "Access token must not be blank")
        String authToken,

        @NotBlank(message = "Refresh token must not be blank")
        String refreshToken) {
}
