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

    UserResponseDto getUserDtoByUsername(String username);

    UserEntity getUserByUsername(String username);

    UserResponseDto getInfo();
    Boolean logout(HttpServletRequest request);
    UserResponseDto save(UserRequestDto dto);
    UserResponseDto update(@NonNull int id, UserRequestDto dto);
    Boolean delete(int id);
    boolean permanentLock(String username);


   String sendPasswordResetCode(String email);
   Boolean verifyPasswordResetCode(PasswordResetRequest passwordResetRequest);
    TokenDto refreshToken(String refreshToken);
}
