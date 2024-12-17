package com.example.Mangxahoi.services;


import com.example.Mangxahoi.dto.Otp;
import com.example.Mangxahoi.dto.TokenDto;
import com.example.Mangxahoi.dto.request.PasswordResetRequest;
import com.example.Mangxahoi.dto.request.UserRequest;
import com.example.Mangxahoi.dto.response.EmailResponse;
import com.example.Mangxahoi.dto.response.UserResponseDto;
import lombok.NonNull;

import java.io.IOException;


public interface UserService {
    UserResponseDto register(UserRequest dto);
    UserResponseDto update(@NonNull Long id, UserRequest dto);
    String delete(Long id);
    String turnOnOffSatus(Long id);
    TokenDto getToken(Otp otp);
    UserResponseDto getUser();
    EmailResponse sendPasswordResetCode(String email);
    String verifyPasswordResetCode(String token,PasswordResetRequest passwordResetRequest);
    UserResponseDto getUser(Long id);

}
