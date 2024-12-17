package com.example.Mangxahoi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Otp implements Serializable {
    @NotBlank(message = "otp cannot be empty")
    String otp;
    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Email should be valid")
    String email;
}
