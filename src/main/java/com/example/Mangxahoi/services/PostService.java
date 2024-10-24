package com.example.Mangxahoi.services;


import com.example.Mangxahoi.dto.request.PostRequest;
import com.example.Mangxahoi.dto.response.PostResponse;
import org.springframework.web.multipart.MultipartFile;

public interface PostService {
  PostResponse createPost(PostRequest postRequest, MultipartFile[] files);
}
