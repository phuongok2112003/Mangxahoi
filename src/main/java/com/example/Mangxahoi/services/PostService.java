package com.example.Mangxahoi.services;


import com.example.Mangxahoi.dto.request.PostRequest;
import com.example.Mangxahoi.dto.response.PageResponse;
import com.example.Mangxahoi.dto.response.PostResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.security.core.parameters.P;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PostService {
  PostResponse createPost( PostRequest postRequest ) ;

  PostResponse updatePost( Long id,PostRequest postRequest);

  PostResponse getPost(Long id);

  PageResponse<PostResponse> getPostOfFriend(int page,int size);

  PageResponse<PostResponse> getPost(Long userId,int page,int size);
  String deletePost(Long id);
}
