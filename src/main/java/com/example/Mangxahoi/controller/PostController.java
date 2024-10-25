package com.example.Mangxahoi.controller;

import com.example.Mangxahoi.dto.request.PostRequest;
import com.example.Mangxahoi.dto.response.PostResponse;
import com.example.Mangxahoi.services.PostService;
import com.example.Mangxahoi.utils.EOResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
@Slf4j
public class PostController {
    private final PostService postService;
    @PostMapping(value = "/create-post",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public EOResponse<PostResponse> createPost( @RequestPart("postRequest") PostRequest postRequest,
                                                @RequestPart(value="files",required = false) MultipartFile[] files) throws JsonProcessingException {
            return EOResponse.build(postService.createPost(postRequest, files));
    }
    @GetMapping("/{id}")
    public  EOResponse<PostResponse> getPost( @PathVariable Long id) {
        return EOResponse.build(postService.getPost(id));
    }
}
