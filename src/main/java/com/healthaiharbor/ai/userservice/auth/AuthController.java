package com.healthaiharbor.ai.userservice.auth;

import com.healthaiharbor.ai.userservice.dto.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for handling authentication-related operations.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Endpoint to refresh an authentication token using a refresh token.
     *
     * @param request DTO containing the refresh token.
     * @return A new authentication token.
     */
    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequestDTO request) {
        logger.info("Received refresh token request for token: {}", request.refreshToken());
        return ResponseEntity.ok(authService.getAuthToken(request.refreshToken()));
    }

    /**
     * Endpoint to authenticate a user and provide a JWT token.
     *
     * @param loginRequestDTO DTO containing login credentials.
     * @return Authentication response including access and refresh tokens.
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        logger.info("Login attempt for user: {}", loginRequestDTO.username());
        return ResponseEntity.ok(authService.login(loginRequestDTO));
    }

    /**
     * Endpoint to register a new user.
     *
     * @param registerRequestDTO DTO containing user registration details.
     * @return The registered user's details.
     */
    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@RequestBody RegisterRequestDTO registerRequestDTO) {
        logger.info("User registration request for: {}", registerRequestDTO.username());
        return ResponseEntity.ok(authService.register(registerRequestDTO));
    }
}
