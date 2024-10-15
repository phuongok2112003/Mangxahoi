package com.example.Mangxahoi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {
    private int id;
    private String username;
    private String email;
    private String password;
    private String permission ;
    private String verifyToken;
    private boolean isActive;
    private String forgotToken;
    private Date forgotTokenExpire;
    private Date createdAt;
    private Date updatedAt;
    private List<PostResponseDto> posts;

}
