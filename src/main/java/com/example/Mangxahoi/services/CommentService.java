package com.example.Mangxahoi.services;


import com.example.Mangxahoi.dto.request.CommentRequest;
import com.example.Mangxahoi.dto.response.CommentResponse;

public interface CommentService {
    public CommentResponse addComment(Long postId,CommentRequest commentRequest);

    public CommentResponse updateComment(Long id,CommentRequest commentRequest);
    public String deleteComment(Long id);
}
