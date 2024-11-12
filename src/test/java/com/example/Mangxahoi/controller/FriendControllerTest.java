package com.example.Mangxahoi.controller;
import com.example.Mangxahoi.constans.enums.FriendshipStatus;
import com.example.Mangxahoi.dto.request.FriendRequest;
import com.example.Mangxahoi.dto.response.FriendResponse;
import com.example.Mangxahoi.dto.response.UserResponseDto;
import com.example.Mangxahoi.services.FriendService;
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

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
class FriendControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FriendService friendService; // MockBean cho FriendService

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    void testAddFriends() throws Exception {
        Long id = 1L;
        FriendResponse friendResponse =FriendResponse.builder()
                .sender(UserResponseDto.builder()
                        .id(2L)
                        .username("user1")
                        .email("user1@gmail.com")
                        .build())
                .receiver(UserResponseDto.builder()
                        .id(id)
                        .username("user2")
                        .email("user2@gmail.com")
                        .build())
                .build();

        Mockito.when(friendService.addFriend(id)).thenReturn(friendResponse);

        mockMvc.perform(MockMvcRequestBuilders.post("/friendships/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("data.receiver.username").value("user2"))
                .andDo(print()) ;
    }

    @Test
    @WithMockUser
    void testResponseFriends() throws Exception {
        Long id = 1L;
        FriendRequest friendRequest = new FriendRequest(FriendshipStatus.ACCEPTED);
        FriendResponse friendResponse  =FriendResponse.builder()
                .sender(UserResponseDto.builder()
                        .id(2L)
                        .username("user1")
                        .email("user1@gmail.com")
                        .build())
                .receiver(UserResponseDto.builder()
                        .id(id)
                        .username("user2")
                        .email("user2@gmail.com")
                        .build())
                .status(FriendshipStatus.ACCEPTED)
                .build();

        Mockito.when(friendService.responseFriend(eq(id), any(FriendRequest.class))).thenReturn(friendResponse);

        mockMvc.perform(MockMvcRequestBuilders.post("/friendships/response/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(friendRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("data.status").value("ACCEPTED"));
    }

    @Test
    @WithMockUser
    void testListFriendACCEPTED() throws Exception {
        Long id = 1L;
        List<FriendResponse> friends = Arrays.asList(
                FriendResponse.builder()
                        .sender(UserResponseDto.builder()
                                .id(2L)
                                .username("user2")
                                .email("user2@gmail.com")
                                .build())
                        .receiver(UserResponseDto.builder()
                                .id(id) // 'id' cần phải được định nghĩa hoặc truyền vào từ trước
                                .username("user1")
                                .email("user1@gmail.com")
                                .build())
                        .status(FriendshipStatus.ACCEPTED)
                        .build(),
                FriendResponse.builder()
                        .sender(UserResponseDto.builder()
                                .id(3L)
                                .username("user3")
                                .email("user3@gmail.com")
                                .build())
                        .receiver(UserResponseDto.builder()
                                .id(id)
                                .username("user1")
                                .email("user1@gmail.com")
                                .build())
                        .status(FriendshipStatus.ACCEPTED)
                        .build()
        );

        Mockito.when(friendService.getListFriend()).thenReturn(friends);

        mockMvc.perform(MockMvcRequestBuilders.get("/friendships/accepted/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("data[0].status").value("ACCEPTED"))
                .andExpect(jsonPath("data[1].status").value("ACCEPTED")).andDo(print());
    }

    @Test
    @WithMockUser
    void testListFriendPENDING() throws Exception {
        List<FriendResponse> friends = Arrays.asList(
                FriendResponse.builder()
                        .sender(UserResponseDto.builder()
                                .id(2L)
                                .username("user2")
                                .email("user2@gmail.com")
                                .build())
                        .receiver(UserResponseDto.builder()
                                .id(1L) // 'id' cần phải được định nghĩa hoặc truyền vào từ trước
                                .username("user1")
                                .email("user1@gmail.com")
                                .build())
                        .status(FriendshipStatus.PENDING)
                        .build(),
                FriendResponse.builder()
                        .sender(UserResponseDto.builder()
                                .id(3L)
                                .username("user3")
                                .email("user3@gmail.com")
                                .build())
                        .receiver(UserResponseDto.builder()
                                .id(1L)
                                .username("user1")
                                .email("user1@gmail.com")
                                .build())
                        .status(FriendshipStatus.PENDING)
                        .build()
        );


        Mockito.when(friendService.getListFriendPENDING()).thenReturn(friends);

        mockMvc.perform(MockMvcRequestBuilders.get("/friendships/pending")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("data[0].status").value("PENDING"))
                .andExpect(jsonPath("data[1].status").value("PENDING")).andDo(print());
    }

    @Test
    @WithMockUser
    void testFriendREJECTED() throws Exception {
        List<FriendResponse> friends = Arrays.asList(
                FriendResponse.builder()
                        .sender(UserResponseDto.builder()
                                .id(2L)
                                .username("user2")
                                .email("user2@gmail.com")
                                .build())
                        .receiver(UserResponseDto.builder()
                                .id(1L) // 'id' cần phải được định nghĩa hoặc truyền vào từ trước
                                .username("user1")
                                .email("user1@gmail.com")
                                .build())
                        .status(FriendshipStatus.REJECTED)
                        .build(),
                FriendResponse.builder()
                        .sender(UserResponseDto.builder()
                                .id(3L)
                                .username("user3")
                                .email("user3@gmail.com")
                                .build())
                        .receiver(UserResponseDto.builder()
                                .id(1L)
                                .username("user1")
                                .email("user1@gmail.com")
                                .build())
                        .status(FriendshipStatus.REJECTED)
                        .build()
        );

        Mockito.when(friendService.rejected()).thenReturn(friends);

        mockMvc.perform(MockMvcRequestBuilders.get("/friendships/rejected")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("data[0].status").value("REJECTED"))
                .andExpect(jsonPath("data[1].status").value("REJECTED")).andDo(print());
    }
}