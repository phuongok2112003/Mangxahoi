package com.example.Mangxahoi.dto.request;

import com.example.Mangxahoi.dto.response.ImageResponse;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostRequest {
    private Long id;
    private String content;
    private ImageRequest imageRequest;
    
}
