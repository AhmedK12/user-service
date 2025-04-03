package com.healthaiharbor.ai.userservice.jwt;

import org.springframework.stereotype.Service;

@Service
public class DefaultJwtServiceImpl implements JwtService {
    private final JwtAlgorithmFactory jwtAlgorithmFactory;

    public DefaultJwtServiceImpl(JwtAlgorithmFactory jwtAlgorithmFactory) {
        this.jwtAlgorithmFactory = jwtAlgorithmFactory;
    }

    @Override
    public String generateToken(String email) {
        return jwtAlgorithmFactory.getJwtAlgorithm().generateToken(email);
    }

    @Override
    public String extractUsername(String token) {
        return jwtAlgorithmFactory.getJwtAlgorithm().extractUsername(token);
    }

    @Override
    public boolean validateToken(String token) {
        return jwtAlgorithmFactory.getJwtAlgorithm().validateToken(token);
    }
}
