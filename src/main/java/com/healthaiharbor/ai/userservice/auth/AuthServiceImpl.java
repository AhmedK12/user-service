package com.healthaiharbor.ai.userservice.auth;

import com.healthaiharbor.ai.userservice.dto.*;
import com.healthaiharbor.ai.userservice.jwt.JwtService;
import com.healthaiharbor.ai.userservice.mapper.UserMapper;
import com.healthaiharbor.ai.userservice.modal.RefreshToken;
import com.healthaiharbor.ai.userservice.modal.Role;
import com.healthaiharbor.ai.userservice.modal.User;
import com.healthaiharbor.ai.userservice.repository.UserRepository;
import com.healthaiharbor.ai.userservice.service.PasswordEncoderService;
import com.healthaiharbor.ai.userservice.util.JwtUtil;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoderService passwordEncoder;
    private final JwtUtil jwtUtil;

    private final JwtService jwtService;

    private final RefreshTokenService refreshTokenService;

    private final UserMapper userMapper;

    public AuthServiceImpl(UserRepository userRepository, PasswordEncoderService passwordEncoderService, JwtUtil jwtUtil, JwtService jwtService, RefreshTokenService refreshTokenService, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoderService;
        this.jwtUtil = jwtUtil;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
        this.userMapper = userMapper;
    }


    public UserDTO register(RegisterRequestDTO request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new RuntimeException("Email already in use!");
        }

        User user = new User();
        user.setFirstname(request.Firstname());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encodePassword(request.password()));
        user.setRoles(Set.of(new Role()));

        return userMapper.toDTO(userRepository.save(user));
    }

    public AuthResponseDTO login(LoginRequestDTO request) {
        User user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        String authToken = jwtService.generateToken(request.username());
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(request.username());

        return new AuthResponseDTO(authToken,refreshToken.getToken());
    }

    @Override
    public AuthResponseDTO getAuthToken(String requestToken) {
        return refreshTokenService.findByToken(requestToken)
                .filter(token -> !token.isExpired())
                .map(token -> {
                     String newAccessToken = jwtService.generateToken(token
                            .getUser().getUsername());

                     return  new AuthResponseDTO(newAccessToken,requestToken);

                })
                .orElseThrow(() -> new RuntimeException("Invalid or expired refresh token"));
    }

    @Override
    public String generateJwtToken(String username) {
        return null;
    }
}

