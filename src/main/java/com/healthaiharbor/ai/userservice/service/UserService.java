package com.healthaiharbor.ai.userservice.service;


import com.healthaiharbor.ai.userservice.dto.LoginRequestDTO;
import com.healthaiharbor.ai.userservice.dto.LoginResponseDTO;
import com.healthaiharbor.ai.userservice.dto.UserDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService {
    UserDTO createUser(UserDTO userDto);
    UserDTO assignRoleToUser(String username, String roleName);
    UserDTO getUserByUsername(String username);
    List<UserDTO> getAllUsers();
    void deleteUser(String username);
    LoginResponseDTO login(LoginRequestDTO loginRequestDTO);
}
