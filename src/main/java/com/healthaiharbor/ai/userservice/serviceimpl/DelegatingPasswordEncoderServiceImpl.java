package com.healthaiharbor.ai.userservice.serviceimpl;

import com.healthaiharbor.ai.userservice.auth.PasswordEncoderType;
import com.healthaiharbor.ai.userservice.service.PasswordEncoderService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class DelegatingPasswordEncoderServiceImpl implements PasswordEncoderService {

    //It should come from properties file
    public static final int SALT_LENGTH = 16;
    public static final int HASH_LENGTH = 32;
    public static final int PARALLELISM = 1;
    public static final int MEMORY = 65536;
    public static final int ITERATIONS = 10;
    public static final int CPU_COST = 16384;
    public static final int SCRYPT_KEY_LENGTH = 32;
    public static final int MEMORY_COST = 8;
    public static final int PARALLELIZATION = 1;
    public static final int SCRYPT_SALT_LENGTH = 64;
    private final PasswordEncoder passwordEncoder;

    public DelegatingPasswordEncoderServiceImpl(@Value("${security.password.encoder}") String encoderKey) {
        Map<String, PasswordEncoder> encoders = new HashMap<>();

        encoders.put(PasswordEncoderType.BCRYPT.getKey(), new BCryptPasswordEncoder());
        encoders.put(PasswordEncoderType.ARGON2.getKey(), new Argon2PasswordEncoder(SALT_LENGTH, HASH_LENGTH, PARALLELISM, MEMORY, ITERATIONS));
        encoders.put(PasswordEncoderType.SCRYPT.getKey(), new SCryptPasswordEncoder(CPU_COST, MEMORY_COST, PARALLELIZATION, SCRYPT_KEY_LENGTH, SCRYPT_SALT_LENGTH));
        // Convert string key to enum
        PasswordEncoderType encoderType = PasswordEncoderType.fromKey(encoderKey);

        this.passwordEncoder = new DelegatingPasswordEncoder(encoderType.getKey(), encoders);
    }

    @Override
    public String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword); // New passwords get Argon2
    }

    @Override
    public boolean matches(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
