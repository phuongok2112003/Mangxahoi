package com.example.Mangxahoi.dto.request;


import lombok.Data;

@Data
public class PasswordResetRequest {
    private String password;
    private String password2;
}
