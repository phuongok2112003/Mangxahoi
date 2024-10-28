package com.example.Mangxahoi.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FavoriteResponse {
    Long id;
    String username;
    Long postId;
    String createAt;
}
