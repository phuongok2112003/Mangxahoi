package com.example.Mangxahoi.dto;

import com.example.Mangxahoi.dto.response.UserResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TokenDto {
    private String accessToken;
    UserResponseDto user;

}
