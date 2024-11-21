package com.example.Mangxahoi.controller;

import com.example.Mangxahoi.dto.request.PostRequest;
import com.example.Mangxahoi.dto.response.PageResponse;
import com.example.Mangxahoi.dto.response.PostResponse;
import com.example.Mangxahoi.services.PostService;
import com.example.Mangxahoi.utils.EOResponse;
import com.example.Mangxahoi.utils.ConvertUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
@Slf4j
public class PostController {
    private final PostService postService;
    @PostMapping(value = "/create-post")
    public EOResponse<PostResponse> createPost(@RequestBody PostRequest postDto) {


            return EOResponse.build(postService.createPost(postDto));
    }
    @GetMapping("/{id}")
    public  EOResponse<PostResponse> getPost( @PathVariable Long id) {
        return EOResponse.build(postService.getPost(id));
    }
    @PatchMapping(value = "/{id}")
    public  EOResponse<PostResponse> updatePost( @PathVariable Long id,@RequestBody PostRequest postDto) {

        return EOResponse.build(postService.updatePost(id,postDto));
    }

    @GetMapping("/post-of-friend")
    public  EOResponse<PageResponse<PostResponse>> getPostOfFriend(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        return EOResponse.build(postService.getPostOfFriend(page,size));
    }

    @GetMapping("/user/{userId}")
    public  EOResponse<PageResponse<PostResponse>> getPostByUserId(
            @PathVariable Long userId,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        return EOResponse.build(postService.getPost(userId,page,size));
    }

    @DeleteMapping("/{postId}")
    public EOResponse<String> deleteComment(@PathVariable Long postId) {
        return EOResponse.build(postService.deletePost( postId));
    }
}
