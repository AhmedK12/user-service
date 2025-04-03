package com.healthaiharbor.ai.userservice.service;

public interface PasswordEncoderService {
    String encodePassword(String rawPassword);
    boolean matches(String rawPassword, String encodedPassword);
}

