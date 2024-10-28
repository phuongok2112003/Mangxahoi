package com.example.Mangxahoi.services;

import com.example.Mangxahoi.dto.request.CommentRequest;
import com.example.Mangxahoi.dto.request.FavoriteRequest;
import com.example.Mangxahoi.dto.response.CommentResponse;
import com.example.Mangxahoi.dto.response.FavoriteResponse;

public interface LikeService {
    public FavoriteResponse like(Long postId);

    public String unLike(Long id);
}
