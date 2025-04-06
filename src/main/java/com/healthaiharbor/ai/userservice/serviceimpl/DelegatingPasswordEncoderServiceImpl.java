package com.healthaiharbor.ai.userservice.serviceimpl;

import com.healthaiharbor.ai.userservice.auth.PasswordEncoderType;
import com.healthaiharbor.ai.userservice.service.PasswordEncoderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Service implementation for handling password encoding using multiple strategies.
 * Supports Bcrypt, Argon2, and Scrypt, with a delegating encoder based on configuration.
 */
@Service
public class DelegatingPasswordEncoderServiceImpl implements PasswordEncoderService {

    private static final Logger logger = LoggerFactory.getLogger(DelegatingPasswordEncoderServiceImpl.class);

    // Argon2 Parameters
    private static final int SALT_LENGTH = 16;
    private static final int HASH_LENGTH = 32;
    private static final int PARALLELISM = 1;
    private static final int MEMORY = 65536;
    private static final int ITERATIONS = 10;

    // Scrypt Parameters
    private static final int CPU_COST = 16384;
    private static final int SCRYPT_KEY_LENGTH = 32;
    private static final int MEMORY_COST = 8;
    private static final int PARALLELIZATION = 1;
    private static final int SCRYPT_SALT_LENGTH = 64;

    private final PasswordEncoder passwordEncoder;

    /**
     * Initializes the password encoder with the configured strategy.
     *
     * @param encoderKey The selected password encoder type from properties file.
     */
    public DelegatingPasswordEncoderServiceImpl(@Value("${security.password.encoder}") String encoderKey) {
        Map<String, PasswordEncoder> encoders = new HashMap<>();

        // Register password encoders
        encoders.put(PasswordEncoderType.BCRYPT.getKey(), new BCryptPasswordEncoder());
        encoders.put(PasswordEncoderType.ARGON2.getKey(), new Argon2PasswordEncoder(SALT_LENGTH, HASH_LENGTH, PARALLELISM, MEMORY, ITERATIONS));
        encoders.put(PasswordEncoderType.SCRYPT.getKey(), new SCryptPasswordEncoder(CPU_COST, MEMORY_COST, PARALLELIZATION, SCRYPT_KEY_LENGTH, SCRYPT_SALT_LENGTH));

        // Convert string key to enum safely
        PasswordEncoderType encoderType = PasswordEncoderType.fromKey(encoderKey);

        if (!encoders.containsKey(encoderType.getKey())) {
            logger.warn("Invalid password encoder key '{}'. Defaulting to BCRYPT.", encoderKey);
            encoderType = PasswordEncoderType.BCRYPT;
        }

        this.passwordEncoder = new DelegatingPasswordEncoder(encoderType.getKey(), encoders);
        logger.info("Password encoding strategy set to '{}'", encoderType.getKey());
    }

    /**
     * Encodes a raw password using the configured password encoder.
     *
     * @param rawPassword The raw password to encode.
     * @return The encoded password.
     */
    @Override
    public String encodePassword(String rawPassword) {
        String encoded = passwordEncoder.encode(rawPassword);
        logger.debug("Encoded password using '{}'", passwordEncoder.getClass().getSimpleName());
        return encoded;
    }

    /**
     * Verifies if the raw password matches the encoded password.
     *
     * @param rawPassword    The plain text password.
     * @param encodedPassword The hashed password.
     * @return True if the passwords match, false otherwise.
     */
    @Override
    public boolean matches(String rawPassword, String encodedPassword) {
        boolean matches = passwordEncoder.matches(rawPassword, encodedPassword);
        logger.debug("Password match result: {}", matches);
        return matches;
    }
}
