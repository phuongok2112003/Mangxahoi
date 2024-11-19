package com.example.Mangxahoi.dto.response;

import com.example.Mangxahoi.dto.request.PostRequest;
import com.example.Mangxahoi.dto.request.UserRequest;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentResponse {
    private Long id;
    private String username;
    private String comment;
    private Long postId;
    private String createdAt;
}