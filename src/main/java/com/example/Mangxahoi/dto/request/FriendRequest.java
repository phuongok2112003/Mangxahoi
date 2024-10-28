package com.example.Mangxahoi.dto.request;


import com.example.Mangxahoi.constans.enums.FriendshipStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FriendRequest {
    FriendshipStatus status;
}
