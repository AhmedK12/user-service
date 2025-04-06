package com.healthaiharbor.ai.userservice.jwt;

import com.healthaiharbor.ai.userservice.modal.User;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;

/**
 * RSA-based JWT implementation for secure token generation and validation.
 * Uses asymmetric encryption with a private key for signing and a public key for verification.
 */
@Component
public class RsaJwtAlgorithm implements JwtAlgorithm {

    private static final Logger logger = LoggerFactory.getLogger(RsaJwtAlgorithm.class);

    @Value("${jwt.privateKey}")
    private String privateKeyStr;

    @Value("${jwt.publicKey}")
    private String publicKeyStr;

    @Value("${jwt.algorithm}")
    private String algorithm; // RS256, RS384, RS512

    /**
     * Retrieves and decodes the RSA private key.
     *
     * @return The PrivateKey instance.
     */
    private PrivateKey getPrivateKey() {
        try {
            byte[] keyBytes = Base64.getDecoder().decode(privateKeyStr);
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePrivate(spec);
        } catch (Exception e) {
            logger.error("Invalid private key", e);
            throw new RuntimeException("Invalid private key", e);
        }
    }

    /**
     * Retrieves and decodes the RSA public key.
     *
     * @return The PublicKey instance.
     */
    private PublicKey getPublicKey() {
        try {
            byte[] keyBytes = Base64.getDecoder().decode(publicKeyStr);
            X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePublic(spec);
        } catch (Exception e) {
            logger.error("Invalid public key", e);
            throw new RuntimeException("Invalid public key", e);
        }
    }

    /**
     * Generates a JWT token for a given user using RSA private key.
     *
     * @param user The user for whom the token is generated.
     * @return The generated JWT token.
     */
    @Override
    public String generateToken(User user) {
        try {
            String token = Jwts.builder()
                    .setSubject(user.getEmail())
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 24 hours expiry
                    .signWith(getPrivateKey(), SignatureAlgorithm.valueOf(algorithm))
                    .compact();
            logger.info("JWT token generated successfully for user: {}", user.getEmail());
            return token;
        } catch (Exception e) {
            logger.error("Error generating JWT token", e);
            throw new RuntimeException("JWT token generation failed", e);
        }
    }

    /**
     * Extracts the username (email) from the JWT token using the RSA public key.
     *
     * @param token The JWT token.
     * @return The extracted username.
     */
    @Override
    public String extractUsername(String token) {
        try {
            String username = Jwts.parserBuilder()
                    .setSigningKey(getPublicKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
            logger.debug("Username extracted from token: {}", username);
            return username;
        } catch (Exception e) {
            logger.error("Error extracting username from token", e);
            throw new RuntimeException("Failed to extract username", e);
        }
    }

    /**
     * Validates the JWT token using the RSA public key.
     *
     * @param token The JWT token to validate.
     * @return True if the token is valid, otherwise false.
     */
    @Override
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getPublicKey()).build().parseClaimsJws(token);
            logger.debug("JWT token is valid.");
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            logger.warn("Invalid or expired JWT token.", e);
            return false;
        }
    }
}
