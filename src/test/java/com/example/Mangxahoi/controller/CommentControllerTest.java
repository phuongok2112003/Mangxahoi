package com.example.Mangxahoi.controller;

import com.example.Mangxahoi.dto.request.CommentRequest;
import com.example.Mangxahoi.dto.response.CommentResponse;
import com.example.Mangxahoi.services.CommentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.post;
import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.put;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommentController.class)
class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentService commentService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testAddComment_Success() throws Exception {
        Long postId = 1L;
        CommentRequest commentRequest = new CommentRequest("This is a test comment");
        CommentResponse commentResponse = new CommentResponse("long","Toi thich bn",1L,"3 tieng truoc");

        Mockito.when(commentService.addComment(eq(postId), any(CommentRequest.class)))
                .thenReturn(commentResponse);


        mockMvc.perform(MockMvcRequestBuilders.post("/comment/{postId}", postId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("data.code").value("1248"));
    }

//    @Test
//    void testUpdateComment_Success() throws Exception {
//        Long postId = 1L;
//        CommentRequest commentRequest = new CommentRequest("Updated comment");
//        CommentResponse commentResponse = new CommentResponse(postId, "Updated comment");
//
//        Mockito.when(commentService.updateComment(eq(postId), any(CommentRequest.class)))
//                .thenReturn(commentResponse);
//
//        mockMvc.perform(put("/comment/{postId}", postId)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(commentRequest)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.data.postId").value(postId))
//                .andExpect(jsonPath("$.data.comment").value("Updated comment"));
//    }
//
//    @Test
//    void testDeleteComment_Success() throws Exception {
//        Long postId = 1L;
//        String deleteMessage = "Comment deleted successfully";
//
//        Mockito.when(commentService.deleteComment(postId)).thenReturn(deleteMessage);
//
//        mockMvc.perform(delete("/comment/{postId}", postId))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.data").value(deleteMessage));
//    }
}