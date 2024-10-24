package com.example.Mangxahoi.services.mapper;

import com.example.Mangxahoi.dto.response.UserResponseDto;
import com.example.Mangxahoi.entity.UserEntity;

public class UserMapper {

    public static UserResponseDto entityToDto(UserEntity entity) {
        if (entity == null) {
            return null;
        }

        UserResponseDto dto = new UserResponseDto();
        dto.setUsername(entity.getUsername());
        dto.setEmail(entity.getEmail());
        dto.setGender(entity.getGender());
        return dto;
    }

}
