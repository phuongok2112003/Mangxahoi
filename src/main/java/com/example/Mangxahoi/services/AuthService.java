package com.example.Mangxahoi.services;

import com.example.Mangxahoi.dto.Otp;
import com.example.Mangxahoi.dto.request.LoginRequest;

import java.io.IOException;

public interface AuthService {
    Otp login(LoginRequest loginRequest);
}
