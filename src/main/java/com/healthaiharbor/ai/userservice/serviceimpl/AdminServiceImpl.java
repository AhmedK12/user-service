package com.healthaiharbor.ai.userservice.serviceimpl;

import com.healthaiharbor.ai.userservice.dto.RoleUpdateRequestDTO;
import com.healthaiharbor.ai.userservice.dto.UserDTO;
import com.healthaiharbor.ai.userservice.kafka.AdminActivityLogger;
import com.healthaiharbor.ai.userservice.mapper.UserMapper;
import com.healthaiharbor.ai.userservice.modal.Role;
import com.healthaiharbor.ai.userservice.modal.User;
import com.healthaiharbor.ai.userservice.repository.RoleRepository;
import com.healthaiharbor.ai.userservice.repository.UserRepository;
import com.healthaiharbor.ai.userservice.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Implementation of AdminService, providing administrative functions
 * such as retrieving, updating, and deleting users.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class AdminServiceImpl implements AdminService {

    private static final Logger logger = LoggerFactory.getLogger(AdminServiceImpl.class);

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final AdminActivityLogger adminActivityLogger;

    /**
     * Retrieves all users in the system.
     *
     * @param admin The admin performing the action.
     * @return A list of UserDTO objects representing all users.
     */
    @Override
    public List<UserDTO> getAllUsers(User admin) {
        logger.info("Admin {} is fetching all users.", admin.getUsername());
        List<User> users = userRepository.findAll();
        adminActivityLogger.usersFetched(admin, null);
        return users.stream()
                .map(userMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a user by their ID.
     *
     * @param id    The ID of the user to retrieve.
     * @param admin The admin performing the action.
     * @return The UserDTO representation of the user.
     */
    @Override
    public UserDTO getUserById(Long id, User admin) {
        logger.info("Admin {} is fetching user with ID {}", admin.getUsername(), id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("User with ID {} not found.", id);
                    return new RuntimeException("User not found");
                });

        adminActivityLogger.userFetched(admin, user);
        return userMapper.toDTO(user);
    }

    /**
     * Updates a user's roles.
     *
     * @param userId  The ID of the user whose roles are being updated.
     * @param request The request containing the new roles.
     * @param admin   The admin performing the action.
     * @return The updated UserDTO.
     */
    @Override
    public UserDTO updateUserRoles(Long userId, RoleUpdateRequestDTO request, User admin) {
        logger.info("Admin {} is updating roles for user ID {}", admin.getUsername(), userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    logger.error("User with ID {} not found.", userId);
                    return new RuntimeException("User not found");
                });

        Set<Role> roles = request.roles().stream()
                .map(roleName -> roleRepository.findByName(roleName)
                        .orElseThrow(() -> {
                            logger.error("Role '{}' not found.", roleName);
                            return new RuntimeException("Role not found: " + roleName);
                        }))
                .collect(Collectors.toSet());

        user.setRoles(roles.stream().toList());
        User updated = userRepository.save(user);

        adminActivityLogger.roleUpdated(admin, user);
        logger.info("Roles updated successfully for user ID {}", userId);
        return userMapper.toDTO(updated);
    }

    /**
     * Deletes a user from the system.
     *
     * @param userId The ID of the user to delete.
     * @param admin  The admin performing the action.
     */
    @Override
    public void deleteUser(Long userId, User admin) {
        logger.info("Admin {} is deleting user ID {}", admin.getUsername(), userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    logger.error("User with ID {} not found.", userId);
                    return new RuntimeException("User not found");
                });

        userRepository.delete(user);
        adminActivityLogger.deleted(admin, user);
        logger.info("User ID {} deleted successfully.", userId);
    }
}
