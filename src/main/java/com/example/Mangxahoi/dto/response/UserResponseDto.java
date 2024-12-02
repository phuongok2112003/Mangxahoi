package com.example.Mangxahoi.dto.response;

import com.example.Mangxahoi.constans.enums.UserRole;
import com.example.Mangxahoi.repository.LikeRepository;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponseDto {
    private Long id;
    private String fullName;
    private String email;
    private Boolean gender ;
    private String occupation;//nghe nghiep
    private String location;
    private String aboutMe;
}
