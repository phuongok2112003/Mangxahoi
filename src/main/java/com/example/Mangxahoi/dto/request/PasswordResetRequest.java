package com.example.Mangxahoi.dto.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PasswordResetRequest {
    @NotBlank(message = "Password cannot be empty")
    @Size(min = 4, message = "Password must be at least 4 characters long")
    private String password;

    @NotBlank(message = "Confirm password cannot be empty")
    private String password2;

}
