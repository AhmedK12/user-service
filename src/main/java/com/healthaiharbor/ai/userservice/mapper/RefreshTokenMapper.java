package com.healthaiharbor.ai.userservice.mapper;

import com.healthaiharbor.ai.userservice.dto.RefreshTokenDTO;
import com.healthaiharbor.ai.userservice.dto.RefreshTokenRequestDTO;
import com.healthaiharbor.ai.userservice.modal.RefreshToken;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RefreshTokenMapper {
    RefreshTokenDTO toDTO(RefreshToken refreshToken);
}

