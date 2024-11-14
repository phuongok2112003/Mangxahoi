package com.example.Mangxahoi.services;

import com.example.Mangxahoi.constans.enums.FriendshipStatus;
import com.example.Mangxahoi.dto.request.FriendRequest;
import com.example.Mangxahoi.dto.response.FriendResponse;
import com.example.Mangxahoi.dto.response.PageResponse;

import java.util.List;

public interface FriendService {
    FriendResponse addFriend(Long receiverId);
    FriendResponse responseFriend(Long senderId,  FriendRequest request);

    PageResponse<FriendResponse> getListFriend(int page, int size);

    List<FriendResponse>getListFriendPENDING();


}
