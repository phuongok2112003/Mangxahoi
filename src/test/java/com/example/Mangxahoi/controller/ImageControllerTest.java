package com.example.Mangxahoi.controller;

import com.example.Mangxahoi.dto.response.ImageResponse;
import com.example.Mangxahoi.services.CommentService;
import com.example.Mangxahoi.services.ImageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ImageControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ImageService imageService;

    @Test
    @WithMockUser
    void testUploadImage() throws Exception {
        MockMultipartFile file1 = new MockMultipartFile("files", "image1.jpg", MediaType.IMAGE_JPEG_VALUE, "test image 1".getBytes());
        MockMultipartFile file2 = new MockMultipartFile("files", "image2.jpg", MediaType.IMAGE_JPEG_VALUE, "test image 2".getBytes());

        ImageResponse imageResponse1 =ImageResponse.builder()
                .url("/post-image/image1.jpg")
                .build();
      ImageResponse imageResponse2 =ImageResponse.builder()
              .url("/post-image/image2.jpg")
              .build();
        List<ImageResponse> imageResponses = List.of(imageResponse1, imageResponse2);

        Mockito.when(imageService.uploadImage(any(MultipartFile[].class))).thenReturn(imageResponses);

        mockMvc.perform(MockMvcRequestBuilders.multipart("/image/upload-image")
                        .file(file1)
                        .file(file2)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].url").value("/post-image/image1.jpg"))
                .andExpect(jsonPath("$.data[1].url").value("/post-image/image2.jpg"));
    }

    // Test upload avatar endpoint
    @Test
    @WithMockUser
    void testUploadAvatar() throws Exception {
        MockMultipartFile file = new MockMultipartFile("files", "avatar.jpg", MediaType.IMAGE_JPEG_VALUE, "test avatar".getBytes());

        ImageResponse imageResponse =ImageResponse.builder()
                .url("/avatar-image/avatar.jpg")
                .build();

        Mockito.when(imageService.uploadAvatar(any(MultipartFile.class))).thenReturn(imageResponse);

        mockMvc.perform(MockMvcRequestBuilders.multipart("/image/upload-avatar")
                        .file(file)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.url").value("/avatar-image/avatar.jpg"));
    }

    // Test get avatar image endpoint
    @Test
    void testGetImageAvatar_Success() throws Exception {
        String filename = "avatar.jpg";
        byte[] imgBytes = "image data".getBytes();

        Mockito.when(imageService.getImage("/avatar-image/" + filename)).thenReturn(imgBytes);

        mockMvc.perform(MockMvcRequestBuilders.get("/image/avatar-image/{filename}", filename))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", MediaType.IMAGE_JPEG_VALUE))
                .andExpect(header().string("Content-Disposition", "inline; filename=\"avatar.jpg\""))
                .andExpect(content().bytes(imgBytes));
    }

    @Test
    void testGetImageAvatar_NotFound() throws Exception {
        String filename = "avatar.jpg";
        Mockito.when(imageService.getImage("/avatar-image/" + filename)).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.get("/image/avatar-image/{filename}", filename))
                .andExpect(status().isNotFound());
    }

    // Test get post image endpoint
    @Test
    void testGetImage_Success() throws Exception {
        String filename = "post.jpg";
        byte[] imgBytes = "image data".getBytes();

        Mockito.when(imageService.getImage("/post-image/" + filename)).thenReturn(imgBytes);

        mockMvc.perform(MockMvcRequestBuilders.get("/image/post-image/{filename}", filename))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", MediaType.IMAGE_JPEG_VALUE))
                .andExpect(header().string("Content-Disposition", "inline; filename=\"post.jpg\""))
                .andExpect(content().bytes(imgBytes));
    }

    @Test
    void testGetImage_NotFound() throws Exception {
        String filename = "post.jpg";
        Mockito.when(imageService.getImage("/post-image/" + filename)).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.get("/image/post-image/{filename}", filename))
                .andExpect(status().isNotFound());
    }
}
