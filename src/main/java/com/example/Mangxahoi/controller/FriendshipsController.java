package com.example.Mangxahoi.controller;

import com.example.Mangxahoi.constans.enums.FriendshipStatus;
import com.example.Mangxahoi.dto.request.CommentRequest;
import com.example.Mangxahoi.dto.request.FriendRequest;
import com.example.Mangxahoi.dto.response.CommentResponse;
import com.example.Mangxahoi.dto.response.FriendResponse;
import com.example.Mangxahoi.services.FriendService;
import com.example.Mangxahoi.utils.EOResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/friendships")
@Slf4j
public class FriendshipsController {
    private final FriendService friendService;
    @PostMapping("/{id}")
    public EOResponse<FriendResponse> addFriends(@PathVariable Long id) {
        return EOResponse.build(friendService.addFriend(id));
    }
    @PostMapping("/response/{id}")
    public EOResponse<FriendResponse> responseFriends(@PathVariable Long id, @RequestBody FriendRequest request) {
        return EOResponse.build(friendService.responseFriend(id,request));
    }

    @GetMapping("/accepted/{id}")
    public EOResponse<List<FriendResponse>> listFriendACCEPTED(@PathVariable Long id) {
        return EOResponse.build(friendService.getListFriend(id));
    }
    @GetMapping("/pending")
    public EOResponse<List<FriendResponse>> listFriendPENDING() {
        return EOResponse.build(friendService.getListFriendPENDING());
    }
    @GetMapping("/rejected")
    public EOResponse<List<FriendResponse>> friendREJECTED() {
        return EOResponse.build(friendService.rejected());
    }

}
