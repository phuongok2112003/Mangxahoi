package com.example.Mangxahoi.dto.request;

import com.example.Mangxahoi.constans.enums.UserRole;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequest {
    @NotBlank(message = "Full name cannot be empty")
    @Pattern(regexp = "^[a-zA-ZÀÁÂÃÄÅÆÇÈÉÊËÌÍÎÏÐÑÒÓÔÕÖØÙÚÛÜÝÞßàáâãäåæçèéêëìíîïðñòóôõöøùúûüýþÿ' ]+$",
            message = "Full name can only contain letters, spaces, and apostrophes")
    private String fullName;


    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Password cannot be empty")
    @Size(min = 4, message = "Password must be at least 4 characters long")
    private String password;
    private UserRole role ;
    @NotBlank(message = "Gender cannot be empty")
    private Boolean gender ;
    private boolean isActive=true ;
    private LocalDate dateBirth;
    private String occupation;//nghe nghiep
    private String location;
    private String aboutMe;
}
