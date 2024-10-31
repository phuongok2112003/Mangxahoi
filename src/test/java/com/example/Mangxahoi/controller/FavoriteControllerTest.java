package com.example.Mangxahoi.controller;

import com.example.Mangxahoi.dto.response.FavoriteResponse;
import com.example.Mangxahoi.services.CommentService;
import com.example.Mangxahoi.services.LikeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class FavoriteControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LikeService likeService;

    @Autowired
    private ObjectMapper objectMapper;
     @Test
     @WithMockUser
    void testLike() throws Exception {
         Long postId = 1L;
         FavoriteResponse favoriteResponse=FavoriteResponse.builder()
                 .postId(postId)
                 .build();
         Mockito.when(likeService.like(postId)).thenReturn(favoriteResponse);
         mockMvc.perform(MockMvcRequestBuilders.post("/favorite/{postId}",postId))
                 .andExpect(status().isOk())
                 .andExpect(MockMvcResultMatchers.jsonPath("$.data.postId").value(postId)).andDo(print());
    }
    @Test
    @WithMockUser
    void testUnLike() throws Exception {
        Long postId = 1L;
        String deleteMessage = "Successfully";
        FavoriteResponse favoriteResponse=FavoriteResponse.builder()
                .postId(postId)
                .build();
        Mockito.when(likeService.unLike(postId)).thenReturn(deleteMessage);
        mockMvc.perform(MockMvcRequestBuilders.delete("/favorite/{postId}",postId))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").value(deleteMessage))
                .andDo(print());

    }
}
