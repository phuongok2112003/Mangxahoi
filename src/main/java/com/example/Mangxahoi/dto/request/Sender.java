package com.example.Mangxahoi.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Sender {
    String name;
    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Email should be valid")
    String email;
}
