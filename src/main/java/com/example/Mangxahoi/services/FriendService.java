package com.example.Mangxahoi.services;

import com.example.Mangxahoi.constans.enums.FriendshipStatus;
import com.example.Mangxahoi.dto.request.FriendRequest;
import com.example.Mangxahoi.dto.response.FriendResponse;

import java.util.List;

public interface FriendService {
    FriendResponse addFriend(Long receiverId);
    FriendResponse responseFriend(Long senderId,  FriendRequest request);

    List<FriendResponse>getListFriend();

    List<FriendResponse>getListFriendPENDING();

    List<FriendResponse> rejected();


}
