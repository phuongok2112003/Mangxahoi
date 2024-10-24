package com.example.Mangxahoi.services;

import com.example.Mangxahoi.dto.request.ImageRequest;
import com.example.Mangxahoi.dto.response.ImageResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageService {
    List<ImageResponse> uploadImage(MultipartFile[] files);
    byte[] getImage( String filename);
    String deleteImage(String filename);
    List<ImageResponse> updateImage(ImageRequest imageCurr, MultipartFile[]  image);
    ImageResponse uploadAvatar(MultipartFile files);
}
