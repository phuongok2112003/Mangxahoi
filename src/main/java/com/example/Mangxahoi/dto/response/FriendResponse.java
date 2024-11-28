package com.example.Mangxahoi.dto.response;

import com.example.Mangxahoi.constans.enums.FriendshipStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FriendResponse {
    Long id;
    UserResponseDto sender;
    UserResponseDto receiver;
    FriendshipStatus status;
    String createdAt;
}
