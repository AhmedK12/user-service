package com.healthaiharbor.ai.userservice.auth;

import com.healthaiharbor.ai.userservice.modal.RefreshToken;
import com.healthaiharbor.ai.userservice.repository.RefreshTokenRepository;
import com.healthaiharbor.ai.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Service for managing refresh tokens used for authentication.
 */
@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private static final Logger logger = LoggerFactory.getLogger(RefreshTokenService.class);

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    @Value("${security.jwt.refresh.expiry}")
    private long refreshTokenDurationMs;

    /**
     * Creates or updates a refresh token for a given user.
     * @param username The username for which the refresh token is created.
     * @return The newly generated or updated refresh token.
     */
    public RefreshToken createRefreshToken(String username) {
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        RefreshToken refreshToken = refreshTokenRepository.findByUser(user)
                .orElse(new RefreshToken());

        refreshToken.setUser(user);
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));

        logger.info("Refresh token created for user: {}", username);
        return refreshTokenRepository.save(refreshToken);
    }

    /**
     * Deletes a refresh token from the repository.
     * @param token The token to be deleted.
     */
    public void deleteRefreshToken(String token) {
        refreshTokenRepository.deleteByToken(token);
        logger.info("Refresh token deleted: {}", token);
    }

    /**
     * Finds a valid refresh token in the repository.
     * @param token The token to be validated.
     * @return An optional containing the refresh token if found.
     */
    public Optional<RefreshToken> findValidToken(String token) {
        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByToken(token);
        if (refreshToken.isPresent()) {
            logger.info("Valid refresh token found: {}", token);
        } else {
            logger.warn("Refresh token not found or expired: {}", token);
        }
        return refreshToken;
    }
}
