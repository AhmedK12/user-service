package com.healthaiharbor.ai.userservice.jwt;

import com.healthaiharbor.ai.userservice.modal.Permission;
import com.healthaiharbor.ai.userservice.modal.Role;
import com.healthaiharbor.ai.userservice.modal.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.List;

/**
 * Implementation of {@link JwtAlgorithm} using HMAC-based signing.
 * This class provides methods to generate, validate, and extract claims from JWTs.
 */
@Component
public class HmacJwtAlgorithm implements JwtAlgorithm {

    private static final Logger logger = LoggerFactory.getLogger(HmacJwtAlgorithm.class);

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.algorithm}")
    private String algorithm; // HS256, HS384, HS512

    /**
     * Retrieves the signing key used for generating and validating JWTs.
     *
     * @return The secret signing key.
     */
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Generates a JWT token for the given user, including roles and permissions as claims.
     *
     * @param user The user for whom the token is generated.
     * @return The generated JWT token.
     */
    @Override
    public String generateToken(User user) {
        logger.info("Generating JWT token for user: {}", user.getUsername());

        List<String> roles = user.getRoles()
                .stream()
                .map(Role::getName)
                .toList();

        List<String> permissions = user.getRoles()
                .stream()
                .flatMap(role -> role.getPermissions().stream())
                .map(Permission::getName)
                .distinct()
                .toList();

        String token = Jwts.builder()
                .setSubject(user.getEmail())
                .claim("roles", roles)
                .claim("permissions", permissions)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 24 hours
                .signWith(getSigningKey(), SignatureAlgorithm.valueOf(algorithm))
                .compact();

        logger.debug("Generated token for user '{}': {}", user.getUsername(), token);
        return token;
    }

    /**
     * Extracts the username (email) from the given JWT token.
     *
     * @param token The JWT token.
     * @return The username (email) extracted from the token.
     */
    @Override
    public String extractUsername(String token) {
        logger.info("Extracting username from token");

        String username = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();

        logger.debug("Extracted username: {}", username);
        return username;
    }

    /**
     * Validates the given JWT token by checking its signature and expiration.
     *
     * @param token The JWT token to validate.
     * @return {@code true} if the token is valid, {@code false} otherwise.
     */
    @Override
    public boolean validateToken(String token) {
        logger.info("Validating token");
        try {
            Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token);
            logger.info("Token is valid");
            return true;
        } catch (ExpiredJwtException e) {
            logger.warn("Token has expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.warn("Token is unsupported: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.warn("Token is malformed: {}", e.getMessage());
        } catch (SignatureException e) {
            logger.warn("Invalid token signature: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.warn("Token argument is illegal: {}", e.getMessage());
        }
        return false;
    }
}
