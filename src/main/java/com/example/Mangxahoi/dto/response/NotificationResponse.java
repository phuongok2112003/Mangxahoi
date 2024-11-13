package com.example.Mangxahoi.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class NotificationResponse {
    private Long id;

    private String content;

    private String createdAt;
}
