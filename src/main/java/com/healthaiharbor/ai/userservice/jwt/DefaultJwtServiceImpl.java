package com.healthaiharbor.ai.userservice.jwt;

import com.healthaiharbor.ai.userservice.modal.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class DefaultJwtServiceImpl implements JwtService {
    private static final Logger logger = LoggerFactory.getLogger(DefaultJwtServiceImpl.class);

    private final JwtAlgorithmFactory jwtAlgorithmFactory;

    public DefaultJwtServiceImpl(JwtAlgorithmFactory jwtAlgorithmFactory) {
        this.jwtAlgorithmFactory = jwtAlgorithmFactory;
    }

    @Override
    public String generateToken(User user) {
        logger.info("Generating JWT token for user: {}", user.getUsername());
        String token = jwtAlgorithmFactory.getJwtAlgorithm().generateToken(user);
        logger.debug("Generated token: {}", token);
        return token;
    }

    @Override
    public String extractUsername(String token) {
        logger.info("Extracting username from token");
        String username = jwtAlgorithmFactory.getJwtAlgorithm().extractUsername(token);
        logger.debug("Extracted username: {}", username);
        return username;
    }

    @Override
    public boolean validateToken(String token) {
        logger.info("Validating token");
        boolean isValid = jwtAlgorithmFactory.getJwtAlgorithm().validateToken(token);
        logger.debug("Token validation result: {}", isValid);
        return isValid;
    }
}
