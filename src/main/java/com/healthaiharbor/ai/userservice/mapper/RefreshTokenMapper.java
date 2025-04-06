package com.healthaiharbor.ai.userservice.mapper;

import com.healthaiharbor.ai.userservice.dto.RefreshTokenDTO;
import com.healthaiharbor.ai.userservice.dto.RefreshTokenRequestDTO;
import com.healthaiharbor.ai.userservice.modal.RefreshToken;
import org.mapstruct.Mapper;


public interface RefreshTokenMapper {
    public RefreshTokenDTO toDTO(RefreshToken refreshToken);
}

