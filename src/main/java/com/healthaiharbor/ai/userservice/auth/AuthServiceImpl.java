package com.healthaiharbor.ai.userservice.auth;

import com.healthaiharbor.ai.userservice.dto.*;
import com.healthaiharbor.ai.userservice.jwt.JwtService;
import com.healthaiharbor.ai.userservice.kafka.AdminActivityLogger;
import com.healthaiharbor.ai.userservice.kafka.UserActivityLogger;
import com.healthaiharbor.ai.userservice.mapper.UserMapper;
import com.healthaiharbor.ai.userservice.modal.RefreshToken;
import com.healthaiharbor.ai.userservice.modal.Role;
import com.healthaiharbor.ai.userservice.modal.User;
import com.healthaiharbor.ai.userservice.repository.RoleRepository;
import com.healthaiharbor.ai.userservice.repository.UserRepository;
import com.healthaiharbor.ai.userservice.service.PasswordEncoderService;
import com.healthaiharbor.ai.userservice.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service implementation for authentication and user management.
 * Handles user registration, login, and token management.
 */
@Service
public class AuthServiceImpl implements AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoderService passwordEncoder;
    private final UserActivityLogger userActivityLogger;
    private final AdminActivityLogger adminActivityLogger;
    private final JwtUtil jwtUtil;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    @Lazy
    private final UserMapper userMapper;

    public AuthServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoderService passwordEncoderService,
                           UserActivityLogger userActivityLogger, AdminActivityLogger adminActivityLogger, JwtUtil jwtUtil,
                           JwtService jwtService, RefreshTokenService refreshTokenService, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoderService;
        this.userActivityLogger = userActivityLogger;
        this.adminActivityLogger = adminActivityLogger;
        this.jwtUtil = jwtUtil;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
        this.userMapper = userMapper;
    }

    /**
     * Registers a new user.
     * @param request RegisterRequestDTO containing user details.
     * @return UserDTO with user information.
     */
    public UserDTO register(RegisterRequestDTO request) {
        logger.info("Attempting to register user: {}", request.username());

        if (userRepository.existsByUsername(request.username())) {
            logger.warn("Username {} is already in use!", request.username());
            throw new RuntimeException("Username already in use!");
        }

        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Default role not found"));

        User user = new User();
        user.setUsername(request.username());
        user.setFirstname(request.Firstname());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encodePassword(request.password()));
        user.setRoles(List.of(userRole));

        User savedUser = userRepository.save(user);
        userActivityLogger.register(user.getUsername());

        logger.info("User {} registered successfully", user.getUsername());
        return userMapper.toDTO(savedUser);
    }

    /**
     * Authenticates a user and generates JWT tokens.
     * @param request LoginRequestDTO containing login credentials.
     * @return AuthResponseDTO with access and refresh tokens.
     */
    public AuthResponseDTO login(LoginRequestDTO request) {
        logger.info("User {} attempting to login", request.username());

        User user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            logger.warn("Invalid login attempt for user {}", request.username());
            throw new RuntimeException("Invalid credentials");
        }

        String authToken = jwtService.generateToken(user);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(request.username());
        userActivityLogger.logLogin(user.getUsername());

        logger.info("User {} logged in successfully", user.getUsername());
        return new AuthResponseDTO(authToken, refreshToken.getToken());
    }

    /**
     * Generates a new JWT access token using a refresh token.
     * @param requestToken Refresh token string.
     * @return AuthResponseDTO with a new access token.
     */
    public AuthResponseDTO getAuthToken(String requestToken) {
        logger.info("Refreshing access token using refresh token");

        Optional<RefreshToken> optionalToken = refreshTokenService.findValidToken(requestToken);

        if (optionalToken.isEmpty()) {
            logger.warn("Invalid or expired refresh token");
            throw new RuntimeException("Invalid or expired refresh token");
        }

        RefreshToken token = optionalToken.get();
        User user = userRepository.findByUsername(token.getUser().getUsername())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        String newAccessToken = jwtService.generateToken(user);
        userActivityLogger.refreshToken(user.getUsername());

        logger.info("New access token generated for user {}", user.getUsername());
        return new AuthResponseDTO(newAccessToken, requestToken);
    }

    @Override
    public String generateJwtToken(String username) {
        return null;
    }
}
