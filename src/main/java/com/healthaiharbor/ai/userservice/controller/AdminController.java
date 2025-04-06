package com.healthaiharbor.ai.userservice.controller;

import com.healthaiharbor.ai.userservice.dto.RoleUpdateRequestDTO;
import com.healthaiharbor.ai.userservice.dto.UserDTO;
import com.healthaiharbor.ai.userservice.modal.User;
import com.healthaiharbor.ai.userservice.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for admin operations such as managing users.
 */
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);
    private final AdminService adminService;

    /**
     * Retrieves all users in the system.
     *
     * @param admin The authenticated admin user.
     * @return A list of UserDTOs.
     */
    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllUsers(@AuthenticationPrincipal User admin) {
        logger.info("Admin '{}' fetching all users", admin.getUsername());
        return ResponseEntity.ok(adminService.getAllUsers(admin));
    }

    /**
     * Retrieves a specific user by ID.
     *
     * @param id The ID of the user.
     * @param admin The authenticated admin user.
     * @return The UserDTO of the requested user.
     */
    @GetMapping("/users/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id, @AuthenticationPrincipal User admin) {
        logger.info("Admin '{}' fetching user with ID: {}", admin.getUsername(), id);
        return ResponseEntity.ok(adminService.getUserById(id, admin));
    }

    /**
     * Updates the roles of a specific user.
     *
     * @param id The ID of the user.
     * @param request The role update request DTO.
     * @param admin The authenticated admin user.
     * @return The updated UserDTO.
     */
    @PutMapping("/users/{id}/roles")
    public ResponseEntity<UserDTO> updateUserRoles(
            @PathVariable Long id,
            @RequestBody RoleUpdateRequestDTO request,
            @AuthenticationPrincipal User admin) {
        logger.info("Admin '{}' updating roles for user with ID: {}", admin.getUsername(), id);
        return ResponseEntity.ok(adminService.updateUserRoles(id, request, admin));
    }

    /**
     * Deletes a specific user by ID.
     *
     * @param id The ID of the user to be deleted.
     * @param admin The authenticated admin user.
     * @return A ResponseEntity with no content.
     */
    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id, @AuthenticationPrincipal User admin) {
        logger.info("Admin '{}' deleting user with ID: {}", admin.getUsername(), id);
        adminService.deleteUser(id, admin);
        return ResponseEntity.noContent().build();
    }
}
