package com.healthaiharbor.ai.userservice.jwt;

import com.healthaiharbor.ai.userservice.modal.User;

public interface JwtService {
    String generateToken(User user);
    String extractUsername(String token);
    boolean validateToken(String token);
}
