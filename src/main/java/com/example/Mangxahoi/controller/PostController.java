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
    @PostMapping(value = "/create-post",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public EOResponse<PostResponse> createPost( @RequestPart("PostRequest") String postDto,
                                                @RequestPart(value="files",required = false) MultipartFile[] files) throws JsonProcessingException {

        PostRequest postRequest=  ConvertUtils.toObject(postDto,PostRequest.class);
            return EOResponse.build(postService.createPost(postRequest, files));
    }
    @GetMapping("/{id}")
    public  EOResponse<PostResponse> getPost( @PathVariable Long id) {
        return EOResponse.build(postService.getPost(id));
    }
    @PatchMapping(value = "/{id}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public  EOResponse<PostResponse> updatePost( @PathVariable Long id,@RequestPart("PostRequest") String postDto,
                                                @RequestPart(value="files",required = false) MultipartFile[] files) throws JsonProcessingException {
        PostRequest postRequest=  ConvertUtils.toObject(postDto,PostRequest.class);
        return EOResponse.build(postService.updatePost(id,postRequest,files));
    }

    @GetMapping("/post-of-friend")
    public  EOResponse<PageResponse<PostResponse>> getPostOfFriend(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        return EOResponse.build(postService.getPostOfFriend(page,size));
    }
}
