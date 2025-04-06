package com.healthaiharbor.ai.userservice.jwt;

import io.jsonwebtoken.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * JWT filter responsible for authenticating requests based on the JWT token.
 * It extracts the token from the request header, validates it, and sets the
 * authentication in the security context.
 */
@Component
public class JwtFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);

    private final DefaultJwtServiceImpl jwtService;
    private final UserDetailsService userDetailsService;

    /**
     * Constructor to initialize dependencies.
     *
     * @param jwtService         Service for handling JWT operations.
     * @param userDetailsService Service to fetch user details.
     */
    public JwtFilter(DefaultJwtServiceImpl jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Filters incoming requests by validating the JWT token and setting the
     * authentication in the security context.
     *
     * @param request     The HTTP request.
     * @param response    The HTTP response.
     * @param filterChain The filter chain.
     * @throws ServletException if a servlet error occurs.
     * @throws IOException      if an I/O error occurs.
     * @throws java.io.IOException if an I/O error occurs.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException, java.io.IOException {
        String token = extractToken(request);

        if (token != null) {
            logger.info("JWT token extracted from request: {}", token);

            if (jwtService.validateToken(token)) {
                logger.debug("JWT token is valid.");
                String username = jwtService.extractUsername(token);
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(authentication);
                logger.info("User '{}' authenticated successfully", username);
            } else {
                logger.warn("Invalid or expired JWT token.");
            }
        } else {
            logger.debug("No JWT token found in request.");
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Extracts the JWT token from the request's Authorization header.
     *
     * @param request The HTTP request.
     * @return The extracted JWT token, or null if not found.
     */
    private String extractToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }
}
