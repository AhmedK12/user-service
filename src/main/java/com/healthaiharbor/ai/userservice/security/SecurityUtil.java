package com.healthaiharbor.ai.userservice.security;


import com.healthaiharbor.ai.userservice.modal.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtil {

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("No authenticated user found");
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof User user) {
            return user;
        }

        throw new RuntimeException("Unexpected principal type");
    }
}
