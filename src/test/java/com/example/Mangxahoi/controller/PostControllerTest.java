package com.example.Mangxahoi.controller;

import com.example.Mangxahoi.dto.request.PostRequest;
import com.example.Mangxahoi.dto.response.ImageResponse;
import com.example.Mangxahoi.dto.response.PostResponse;
import com.example.Mangxahoi.services.PostService;
import com.example.Mangxahoi.utils.ConvertUtils;
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

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PostControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PostService postService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    void testCreatePost() throws Exception {
        PostRequest postRequest = new PostRequest();


        PostResponse postResponse = PostResponse.builder()
                .images(Arrays.asList(ImageResponse.builder().url("/post-image/image1.jpg").build()))
                .content("Toi yeu ban")
                .build();


        MockMultipartFile postRequestPart = new MockMultipartFile("PostRequest", "",
                "application/json", objectMapper.writeValueAsString(postRequest).getBytes());
        MockMultipartFile file1 = new MockMultipartFile("files", "image1.jpg",
                MediaType.IMAGE_JPEG_VALUE, "image data".getBytes());
        Mockito.when(postService.createPost(any(PostRequest.class), any(MultipartFile[].class))).thenReturn(postResponse);


        mockMvc.perform(MockMvcRequestBuilders.multipart("/post/create-post")
                        .file(postRequestPart)
                        .file(file1)
                        .content("{\n" +
                                "  \"content\": \"Toi yeu ban\",\n" +
                                "  \"imageRequest\": {\n" +
                                "    \"url\": [\n" +
                                "     \n" +
                                "    ]\n" +
                                "  }\n" +
                                "}")
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andExpect(jsonPath("data.images[0].url").value("/post-image/image1.jpg"))
                .andExpect(jsonPath("data.content").value("Toi yeu ban")).andDo(print());
    }

    // Test get post by id
    @Test
    @WithMockUser
    void testGetPost() throws Exception {
        Long postId = 1L;

        PostResponse postResponse = PostResponse.builder()
                .id(1L)
                .images(Arrays.asList(ImageResponse.builder().url("/post-image/image1.jpg").build()))
                .content("Toi yeu ban")
                .build();

        Mockito.when(postService.getPost(postId)).thenReturn(postResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/post/{id}", postId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isNotEmpty()).andExpect(jsonPath("data.images[0].url")
                        .value("/post-image/image1.jpg"))
                .andExpect(jsonPath("data.content").value("Toi yeu ban")).andDo(print());
    }

    // Test update post
    @Test
    @WithMockUser
    void testUpdatePost() throws Exception {
        Long postId = 1L;

        PostResponse postResponse = PostResponse.builder()
                .id(1L)
                .images(Arrays.asList(ImageResponse.builder().url("/post-image/image1.jpg").build()))
                .content("Toi yeu ban")
                .build();
        PostRequest postRequest = new PostRequest();

        MockMultipartFile postRequestPart = new MockMultipartFile("PostRequest", "", "application/json", objectMapper.writeValueAsString(postRequest).getBytes());
        MockMultipartFile file1 = new MockMultipartFile("files", "image1.jpg", MediaType.IMAGE_JPEG_VALUE, "image data".getBytes());

        Mockito.when(postService.updatePost(eq(postId), any(PostRequest.class), any(MultipartFile[].class))).thenReturn(postResponse);


        mockMvc.perform(MockMvcRequestBuilders.multipart("/post/{id}", postId)
                        .file(postRequestPart)
                        .file(file1)
                        .with(request -> { request.setMethod("PATCH"); return request; })
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isNotEmpty()).andExpect(jsonPath("data.images[0].url")
                        .value("/post-image/image1.jpg"))
                .andExpect(jsonPath("data.content").value("Toi yeu ban")).andDo(print());
    }

    // Test get posts of friends
//    @Test
//    @WithMockUser
//    void testGetPostOfFriend() throws Exception {
//        PostResponse postResponse1 = new PostResponse();
//        PostResponse postResponse2 = new PostResponse();
//        List<PostResponse> postResponses = List.of(postResponse1, postResponse2);
//
//        Mockito.when(postService.getPostOfFriend()).thenReturn(postResponses);
//
//        mockMvc.perform(MockMvcRequestBuilders.get("/post/post-of-friend"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.data").isArray())
//                .andExpect(jsonPath("$.data.length()").value(2));
//    }
}
