package com.example.Mangxahoi.controller;

import com.example.Mangxahoi.dto.request.CommentRequest;
import com.example.Mangxahoi.dto.response.CommentResponse;
import com.example.Mangxahoi.dto.response.ImageResponse;
import com.example.Mangxahoi.dto.response.PageResponse;
import com.example.Mangxahoi.services.CommentService;
import com.example.Mangxahoi.utils.EOResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comment")
@Slf4j
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/{postId}")
    public EOResponse<CommentResponse> addComment(@PathVariable Long postId,@RequestBody CommentRequest commentRequest) {
        return EOResponse.build(commentService.addComment( postId,commentRequest));
    }
    @PutMapping("/{postId}")
    public EOResponse<CommentResponse> updateComment(@PathVariable Long postId,@RequestBody CommentRequest commentRequest) {
        return EOResponse.build(commentService.updateComment( postId,commentRequest));
    }
    @GetMapping("/{postId}")
    public EOResponse<PageResponse<CommentResponse>> getCommentsByPostId(
            @PathVariable Long postId,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        return EOResponse.build(commentService.getAll(postId, page, size));
    }
    @DeleteMapping("/{postId}")
    public EOResponse<String> deleteComment(@PathVariable Long postId) {
        return EOResponse.build(commentService.deleteComment( postId));
    }
}
