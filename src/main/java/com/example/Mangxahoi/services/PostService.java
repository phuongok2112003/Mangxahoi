package com.example.Mangxahoi.services;


import com.example.Mangxahoi.dto.request.PostRequest;
import com.example.Mangxahoi.dto.response.PostResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.security.core.parameters.P;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PostService {
  PostResponse createPost( PostRequest postRequest , MultipartFile[] files) throws JsonProcessingException;

  PostResponse updatePost( Long id,PostRequest postRequest, MultipartFile[] files);

  PostResponse getPost(Long id);

  List<PostResponse> getPostOfFriend();
}
