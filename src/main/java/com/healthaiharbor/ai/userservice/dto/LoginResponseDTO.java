package com.healthaiharbor.ai.userservice.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginResponseDTO(
        @NotBlank(message = "Auth token must not be blank")
        String authToken,

        @NotBlank(message = "Refresh token must not be blank")
        String refreshToken
) {
}

