package com.healthaiharbor.ai.userservice.service;


import com.healthaiharbor.ai.userservice.dto.LoginRequestDTO;
import com.healthaiharbor.ai.userservice.dto.LoginResponseDTO;
import com.healthaiharbor.ai.userservice.dto.UserDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService {
    UserDTO getUserByUsername(String username);
    void deleteUser(String username);
    UserDTO updateUser(String username, UserDTO userDto);
}
