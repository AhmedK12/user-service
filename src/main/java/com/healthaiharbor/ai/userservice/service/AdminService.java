package com.healthaiharbor.ai.userservice.service;


import com.healthaiharbor.ai.userservice.dto.RoleUpdateRequestDTO;
import com.healthaiharbor.ai.userservice.dto.UserDTO;
import com.healthaiharbor.ai.userservice.modal.User;

import java.util.List;

public interface AdminService {
    List<UserDTO> getAllUsers(User admin);
    UserDTO getUserById(Long id,User admin);
    UserDTO updateUserRoles(Long userId, RoleUpdateRequestDTO request, User admin);
    void deleteUser(Long userId, User admin);
}
