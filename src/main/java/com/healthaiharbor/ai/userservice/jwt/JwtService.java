package com.healthaiharbor.ai.userservice.jwt;

public interface JwtService {
    String generateToken(String email);
    String extractUsername(String token);
    boolean validateToken(String token);
}
