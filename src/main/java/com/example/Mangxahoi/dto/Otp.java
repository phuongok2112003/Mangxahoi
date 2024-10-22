package com.example.Mangxahoi.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Otp {
    String code;
    String email;
}
