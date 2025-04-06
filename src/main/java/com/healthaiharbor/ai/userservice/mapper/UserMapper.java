package com.healthaiharbor.ai.userservice.mapper;

import com.healthaiharbor.ai.userservice.dto.UserDTO;
import com.healthaiharbor.ai.userservice.modal.User;
import org.mapstruct.Mapper;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserDTO  toDTO(User user){
        return null;
    }
}
