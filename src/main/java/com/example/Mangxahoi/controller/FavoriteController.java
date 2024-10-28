package com.example.Mangxahoi.controller;

import com.example.Mangxahoi.dto.request.CommentRequest;
import com.example.Mangxahoi.dto.response.CommentResponse;
import com.example.Mangxahoi.dto.response.FavoriteResponse;
import com.example.Mangxahoi.services.CommentService;
import com.example.Mangxahoi.services.LikeService;
import com.example.Mangxahoi.utils.EOResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/favorite")
@Slf4j
public class FavoriteController {
    private final LikeService likeService;

    @PostMapping("/{postId}")
    public EOResponse<FavoriteResponse> like(@PathVariable Long postId) {
        return EOResponse.build(likeService.like( postId));
    }

    @DeleteMapping("/{postId}")
    public EOResponse<String> unLike(@PathVariable Long postId) {
        return EOResponse.build(likeService.unLike( postId));
    }
}
