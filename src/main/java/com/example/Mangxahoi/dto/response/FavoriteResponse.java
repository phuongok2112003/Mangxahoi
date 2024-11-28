package com.example.Mangxahoi.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FavoriteResponse {
    Long id;
    String username;
    Long postId;
    String createAt;
}
