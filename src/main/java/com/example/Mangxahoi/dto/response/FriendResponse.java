package com.example.Mangxahoi.dto.response;

import com.example.Mangxahoi.constans.enums.FriendshipStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FriendResponse {
    UserResponseDto sender;
    UserResponseDto receiver;
    FriendshipStatus status;
    String createdAt;
}
