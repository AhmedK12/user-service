package com.healthaiharbor.ai.userservice.dto;

import jakarta.validation.constraints.NotBlank;

public record RefreshTokenDTO(
        @NotBlank(message = "Refresh token must not be blank")
        String refreshToken
) {
}
