package com.healthaiharbor.ai.userservice.serviceimpl;

import com.healthaiharbor.ai.userservice.dto.LoginRequestDTO;
import com.healthaiharbor.ai.userservice.dto.LoginResponseDTO;
import com.healthaiharbor.ai.userservice.dto.UserDTO;
import com.healthaiharbor.ai.userservice.kafka.AdminActivityLogger;
import com.healthaiharbor.ai.userservice.kafka.UserActivityLogger;
import com.healthaiharbor.ai.userservice.mapper.UserMapper;
import com.healthaiharbor.ai.userservice.repository.UserRepository;
import com.healthaiharbor.ai.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final BloomFilterServiceImpl bloomFilterService;
    private final AdminActivityLogger adminActivityLogger;
    private final UserActivityLogger userActivityLogger;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserServiceImpl(BloomFilterServiceImpl bloomFilterService, AdminActivityLogger adminActivityLogger, UserActivityLogger userActivityLogger, UserRepository userRepository, UserMapper userMapper) {
        this.bloomFilterService = bloomFilterService;
        this.adminActivityLogger = adminActivityLogger;
        this.userActivityLogger = userActivityLogger;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }



    @Override
    public UserDTO getUserByUsername(String username) {
        return userMapper.toDTO(userRepository.findByUsername(username).orElseThrow());
    }



    @Override
    public void deleteUser(String username) {
       userRepository.deleteByUsername(username);
    }



    @Override
    public UserDTO updateUser(String username, UserDTO userDto) {  // user should be same
        return null;
    }
}
