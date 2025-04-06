package com.healthaiharbor.ai.userservice.auth;

import com.healthaiharbor.ai.userservice.modal.User;
import com.healthaiharbor.ai.userservice.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Custom implementation of UserDetailsService for loading user details from the database.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

    @Autowired
    private UserRepository userRepository;

    /**
     * Loads user details by username.
     *
     * @param username The username of the user.
     * @return UserDetails containing the user's credentials and authorities.
     * @throws UsernameNotFoundException If the user is not found in the database.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("Attempting to load user: {}", username);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    logger.error("User not found: {}", username);
                    return new UsernameNotFoundException("User not found");
                });

        Set<SimpleGrantedAuthority> authorities = new HashSet<>();

        // Add roles (e.g., "ROLE_ADMIN")
        authorities.addAll(user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toSet()));

        // Add permissions (e.g., "READ_USER", "WRITE_USER")
        authorities.addAll(user.getRoles().stream()
                .flatMap(role -> role.getPermissions().stream())
                .map(permission -> new SimpleGrantedAuthority(permission.getName()))
                .collect(Collectors.toSet()));

        logger.info("User loaded successfully: {} with roles: {}", username, authorities);
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                authorities
        );
    }
}
