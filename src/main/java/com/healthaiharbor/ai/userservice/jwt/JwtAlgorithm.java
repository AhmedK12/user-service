package com.healthaiharbor.ai.userservice.jwt;

public interface JwtAlgorithm {
    String generateToken(String email);
    String extractUsername(String token);
    boolean validateToken(String token);
}
