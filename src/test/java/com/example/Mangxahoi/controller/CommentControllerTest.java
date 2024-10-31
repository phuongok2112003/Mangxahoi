package com.example.Mangxahoi.controller;
import com.example.Mangxahoi.dto.request.CommentRequest;
import com.example.Mangxahoi.dto.response.CommentResponse;
import com.example.Mangxahoi.services.CommentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@SpringBootTest
@AutoConfigureMockMvc
class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentService commentService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    void testAddComment_Success() throws Exception {
        Long postId = 1L;
        CommentRequest commentRequest = new CommentRequest("This is a test comment");
        CommentResponse commentResponse = CommentResponse.builder()
                .comment("This is a test comment")
                .build();
        Mockito.when(commentService.addComment(eq(postId), any(CommentRequest.class)))
                .thenReturn(commentResponse);

        mockMvc.perform(MockMvcRequestBuilders.post("/comment/{postId}", postId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("data.comment").value("This is a test comment"))
                .andDo(print());

    }

    @Test
    @WithMockUser
    void testUpdateComment_Success() throws Exception {
        Long postId = 1L;
        CommentRequest commentRequest = new CommentRequest("Updated comment");
        CommentResponse commentResponse = CommentResponse.builder()
                .username("phuong@gmail.com")
                .comment("Updated comment")
                .postId(1L)
                .build();

        Mockito.when(commentService.updateComment(eq(postId), any(CommentRequest.class)))
                .thenReturn(commentResponse);

        mockMvc.perform(MockMvcRequestBuilders.put("/comment/{postId}", postId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("data.postId").value(postId))
                .andExpect(MockMvcResultMatchers.jsonPath("data.comment").value("Updated comment"))
                .andDo(print());
    }

    @Test
    @WithMockUser
    void testDeleteComment_Success() throws Exception {
        Long postId = 1L;
        String deleteMessage = "Successfully";

        Mockito.when(commentService.deleteComment(postId)).thenReturn(deleteMessage);

        mockMvc.perform(delete("/comment/{postId}", postId))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").value(deleteMessage))
                .andDo(print());
    }

}