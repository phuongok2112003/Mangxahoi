package com.example.Mangxahoi.dto.response;

import com.example.Mangxahoi.constans.enums.PostStatus;
import com.example.Mangxahoi.entity.UserEntity;

import com.example.Mangxahoi.repository.UserRepository;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import java.time.Instant;

import java.util.List;
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostResponse {
    private Long id;

    private String content;

    private UserResponseDto user;

    private List<CommentResponse> comments;

    private List<FavoriteResponse> likes;

    private List<ImageResponse> images;

    private String createdAt;

    private PostStatus status;
}
