package com.example.Mangxahoi.dto.request;

import com.example.Mangxahoi.constans.enums.UserRole;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {
    private String username;
    private String email;
    private String password;
    private UserRole role ;
    private Boolean gender ;
    private boolean isActive=true ;
    private LocalDate dateBirth;
    private String occupation;//nghe nghiep
    private String location;
    private String aboutMe;
}
