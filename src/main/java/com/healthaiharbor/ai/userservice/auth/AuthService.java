package com.healthaiharbor.ai.userservice.auth;

import com.healthaiharbor.ai.userservice.dto.AuthResponseDTO;
import com.healthaiharbor.ai.userservice.dto.LoginRequestDTO;
import com.healthaiharbor.ai.userservice.dto.RegisterRequestDTO;
import com.healthaiharbor.ai.userservice.dto.UserDTO;

public interface AuthService {
    String generateJwtToken(String username);
    public UserDTO register(RegisterRequestDTO request);

    public AuthResponseDTO login(LoginRequestDTO request);

    AuthResponseDTO getAuthToken(String requestToken);
}
