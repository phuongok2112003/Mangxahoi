package com.example.Mangxahoi.services;


import com.example.Mangxahoi.dto.TokenDto;
import com.example.Mangxahoi.dto.request.PasswordResetRequest;
import com.example.Mangxahoi.dto.request.UserRequestDto;
import com.example.Mangxahoi.dto.response.UserResponseDto;
import com.example.Mangxahoi.entity.UserEntity;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import org.springframework.data.domain.Page;



public interface UserService {
    UserResponseDto register(UserRequestDto dto);
    UserResponseDto update(@NonNull Long id, UserRequestDto dto);
    String delete(Long id);
}
