package com.example.Mangxahoi.services.mapper;

import com.example.Mangxahoi.dto.response.ImageResponse;
import com.example.Mangxahoi.entity.ImageEntity;

public class ImageMapper {
    public static ImageResponse entityToResponse(ImageEntity imageEntity){
        return  ImageResponse.builder()
                .url(imageEntity.getUrl())
                .build();
    }
}
