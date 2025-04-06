package com.healthaiharbor.ai.userservice.controller;

import com.healthaiharbor.ai.userservice.dto.UserDTO;
import com.healthaiharbor.ai.userservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserDTO userDto) {
        logger.info("Creating user: {}", userDto);
        return ResponseEntity.ok(userService.createUser(userDto));
    }

    @PostMapping("/{username}/roles/{roleName}")
    public ResponseEntity<UserDTO> assignRoleToUser(@PathVariable String username, @PathVariable String roleName) {
        logger.info("Assigning role '{}' to user '{}'", roleName, username);
        return ResponseEntity.ok(userService.assignRoleToUser(username, roleName));
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserDTO> getUser(@PathVariable String username) {
        logger.info("Fetching user with username: {}", username);
        return ResponseEntity.ok(userService.getUserByUsername(username));
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        logger.info("Fetching all users");
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<Void> deleteUser(@PathVariable String username) {
        logger.info("Deleting user with username: {}", username);
        userService.deleteUser(username);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{username}")
    public ResponseEntity<UserDTO> updateUser(
            @PathVariable String username,
            @Valid @RequestBody UserDTO userDto
    ) {
        logger.info("Updating user '{}': {}", username, userDto);
        return ResponseEntity.ok(userService.updateUser(username, userDto));
    }
}
