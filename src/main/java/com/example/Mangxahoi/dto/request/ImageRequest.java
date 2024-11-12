package com.example.Mangxahoi.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;
@Data
@AllArgsConstructor
@Builder
public class ImageRequest {
    private List<String> url;
}
