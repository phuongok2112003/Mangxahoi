package com.example.Mangxahoi.controller;

import com.example.Mangxahoi.dto.Otp;
import com.example.Mangxahoi.dto.request.LoginRequest;
import com.example.Mangxahoi.services.AuthService;
import com.example.Mangxahoi.utils.EOResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthController {
    private final AuthService authService;
    @PostMapping("/login")
    public EOResponse<Otp> login(@Valid @RequestBody LoginRequest loginRequest)  {
        return EOResponse.build(authService.login(loginRequest));
    }
}
