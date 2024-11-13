package com.example.Mangxahoi.services;


import com.example.Mangxahoi.dto.request.CommentRequest;
import com.example.Mangxahoi.dto.response.CommentResponse;
import com.example.Mangxahoi.dto.response.PageResponse;

import java.util.List;

public interface CommentService {
    public CommentResponse addComment(Long postId,CommentRequest commentRequest);

    public CommentResponse updateComment(Long id,CommentRequest commentRequest);
    public String deleteComment(Long id);
    public PageResponse<CommentResponse> getAll(Long postId, int page, int size);
}
