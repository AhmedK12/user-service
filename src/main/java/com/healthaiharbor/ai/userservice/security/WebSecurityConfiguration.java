package com.healthaiharbor.ai.userservice.security;

import com.healthaiharbor.ai.userservice.jwt.JwtFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configures the security settings for the application, including JWT authentication.
 */
@Configuration
public class WebSecurityConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(WebSecurityConfiguration.class);

    private final JwtFilter jwtFilter;

    public WebSecurityConfiguration(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    /**
     * Configures security settings, including authorization rules and JWT filter.
     *
     * @param http HttpSecurity object for configuring security policies.
     * @return SecurityFilterChain defining the application's security settings.
     * @throws Exception if any configuration error occurs.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        logger.info("Initializing Security Configuration...");

        http
                .csrf(AbstractHttpConfigurer::disable)  // Disabling CSRF for stateless authentication
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/admin/**").hasAuthority("ROLE_ADMIN")  // Restrict admin routes
                        .requestMatchers("/user/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN") // User and Admin access
                        .requestMatchers("/data/**").hasAuthority("READ_USER")  // Permission-based access
                        .anyRequest().authenticated()  // Any other request requires authentication
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        logger.info("Security configuration initialized successfully.");
        return http.build();
    }

    /**
     * Exposes the AuthenticationManager as a Spring Bean.
     *
     * @param authConfig Authentication configuration for Spring Security.
     * @return AuthenticationManager for managing authentication processes.
     * @throws Exception if any configuration error occurs.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        logger.info("Initializing Authentication Manager...");
        return authConfig.getAuthenticationManager();
    }
}
