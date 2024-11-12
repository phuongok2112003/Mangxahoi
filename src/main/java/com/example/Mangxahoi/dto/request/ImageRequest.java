package com.example.Mangxahoi.dto.request;

import lombok.Builder;
import lombok.Data;

import java.util.List;
@Data
@Builder
public class ImageRequest {
    private List<String> url;
}
