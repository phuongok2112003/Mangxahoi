package com.example.Mangxahoi.services.mapper;

import com.example.Mangxahoi.dto.request.UserRequestDto;
import com.example.Mangxahoi.dto.response.UserResponseDto;
import com.example.Mangxahoi.entity.UserEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class UserMapper {

    public static UserResponseDto entityToDto(UserEntity entity) {
        if (entity == null) {
            return null;
        }

        UserResponseDto dto = new UserResponseDto();
        dto.setUsername(entity.getUsername());
        dto.setEmail(entity.getEmail());
        dto.setRole(entity.getRole());
        dto.setGender(entity.getGender());
        dto.setActive(entity.isActive());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setDateBirth(entity.getDateBirth());
        dto.setUpdatedAt(entity.getUpdatedAt());

        dto.setPosts(entity.getPosts());
        dto.setComments(entity.getComments());
        dto.setLikes(entity.getLikes());
        dto.setFriends(entity.getFriends());
        dto.setImages(entity.getImages());
        return dto;
    }

}
