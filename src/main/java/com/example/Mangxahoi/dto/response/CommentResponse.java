package com.example.Mangxahoi.dto.response;

import com.example.Mangxahoi.dto.request.PostRequest;
import com.example.Mangxahoi.dto.request.UserRequest;
import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class CommentResponse {
    private String username;
    private String comment;
    private Long postId;
    private String createdAt;
}