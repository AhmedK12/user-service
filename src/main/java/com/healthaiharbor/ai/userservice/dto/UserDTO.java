package com.healthaiharbor.ai.userservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserDTO(

        @NotBlank(message = "Username is required")
        @Size(min = 4, max = 20, message = "Username must be between 4 and 20 characters")
        String username,

        @NotBlank(message = "First name is required")
        String firstname,

        @NotBlank(message = "Last name is required")
        String lastname,

        @Email(message = "Invalid email format")
        @NotBlank(message = "Email is required")
        String email,

        @NotBlank(message = "Password is required")
        @Size(min = 6, max = 100, message = "Password must be at least 6 characters long")
        @Pattern(
                regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{6,}$",
                message = "Password must contain upper/lowercase letters and digits"
        )
        String password
) {}
