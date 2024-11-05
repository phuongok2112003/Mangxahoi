package com.example.Mangxahoi.controller;

import com.example.Mangxahoi.constans.MessageCodes;
import com.example.Mangxahoi.dto.Otp;
import com.example.Mangxahoi.dto.TokenDto;
import com.example.Mangxahoi.dto.request.PasswordResetRequest;
import com.example.Mangxahoi.dto.request.UserRequest;
import com.example.Mangxahoi.dto.response.EmailResponse;
import com.example.Mangxahoi.dto.response.UserResponseDto;
import com.example.Mangxahoi.services.Impl.UserServiceImpl;
import com.example.Mangxahoi.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;
    @MockBean
    UserDetailsService userDetailsService;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
//    @WithMockUser
    void register_ShouldReturnUserResponse_WhenSuccessful() throws Exception {
        UserResponseDto mockResponse = UserResponseDto.builder()
                .id(1L)
                .username("user1")
                .email("user1@gmail.com")
                .build();
        UserRequest userRequest = UserRequest.builder()
                .username("user1")
                .email("user1@gmail.com")
                .password("1234")
                .build();
        Mockito.when(userService.register(any(UserRequest.class))).thenReturn(mockResponse);


        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andExpect(jsonPath("$.data.username").value("user1"))
                .andDo(print());
    }

    @Test
    @WithMockUser
    void delete_ShouldReturnSuccessMessage_WhenDeleted() throws Exception {
        Mockito.when(userService.delete(anyLong())).thenReturn(MessageCodes.PROCESSED_SUCCESSFULLY);

        mockMvc.perform(delete("/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(MessageCodes.PROCESSED_SUCCESSFULLY))
                .andDo(print());
    }

    @Test
    @WithMockUser
    void update_ShouldReturnUpdatedUser_WhenSuccessful() throws Exception {
        UserResponseDto mockResponse = UserResponseDto.builder()
                .id(1L)
                .username("user1")
                .email("user1@gmail.com")
                .build();
        UserRequest userRequest = UserRequest.builder()
                .username("user1")
                .email("user1@gmail.com")
                .password("1234")
                .build();
        Mockito.when(userService.update(anyLong(), any(UserRequest.class))).thenReturn(mockResponse);

        mockMvc.perform(put("/user/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.username").value("user1"))
                .andDo(print());
    }

    @Test
    void getToken_ShouldReturnTokenDto_WhenOtpIsValid() throws Exception {
        TokenDto tokenDto = new TokenDto();
        String acc="eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJwaHVvbmdAZ21haWwuY29tIiwicm9sZXMiOlsid" +
                "XNlciJdLCJpZCI6MSwiZXhwIjoxNzMwMzkwODYzLCJpYXQiOjE3MzAzODcyNjMsInVzZXJuYW1lIjoicGh1b25nIn0.U1uKEh013i8O7ukPUD0dcZt31EwDL7xOTPmrFtxvn5Q";
        String ref="eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJwaHVvbmciLCJleHAiOjE3MzI5NzkyNjN9.9hTPlFxLVTsFVUNZ-2S1lkhczaf05QxCTdufnO_ohvE";
        tokenDto.setAccessToken(acc);
        tokenDto.setRefreshToken(ref);
        Otp otp = new Otp("1248","phuong0961070156@gmail.com");
        Mockito.when(userService.getToken(any(Otp.class))).thenReturn(tokenDto);

        mockMvc.perform(post("/user/get-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(otp)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.accessToken").value(acc))
             .andExpect(jsonPath("$.data.refreshToken").value(ref)).andDo(print());
    }

    @Test
    void refreshToken_ShouldReturnNewToken_WhenValidTokenProvided() throws Exception {
        TokenDto tokenDto = new TokenDto();
        String acc="eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJwaHVvbmdAZ21haWwuY29tIiwicm9sZXMiOlsid" +
                "XNlciJdLCJpZCI6MSwiZXhwIjoxNzMwMzkwODYzLCJpYXQiOjE3MzAzODcyNjMsInVzZXJuYW1lIjoicGh1b25nIn0.U1uKEh013i8O7ukPUD0dcZt31EwDL7xOTPmrFtxvn5Q";
        String curref="eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJwaHVvbmdAZ21haWwuY29tIiwiZXhwIjoxNzMyOTgwNjU5fQ.P2ErGXXpXen9_ASzvYg9yCokJHqcUd5vIjY1VFXeWyk";
        String ref="eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJwaHVvbmdAZ21haWwuY29tIiwiZXhwIjoxNzMyOTgwNjQzfQ.XrNrz9mXNDVk6u_jkgS04qQtqyVANDgD0ZSg18ceAfc";

        tokenDto.setAccessToken(acc);
        tokenDto.setRefreshToken(ref);
        Mockito.when(userService.refreshToken(any(String.class))).thenReturn(tokenDto);

        mockMvc.perform(post("/user/refresh-token")
                        .param("token", curref))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.accessToken").value(acc))
                .andExpect(jsonPath("$.data.refreshToken").value(ref)).andDo(print());
    }

    @Test
    void forgotPassword_ShouldReturnEmailResponse_WhenSuccessful() throws Exception {
        EmailResponse emailResponse = new EmailResponse("5416410.264810.2684012.4848");
        Mockito.when(userService.sendPasswordResetCode(any(String.class))).thenReturn(emailResponse);

        mockMvc.perform(post("/user/forgot")
                        .param("email", "test@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.messageId").value("5416410.264810.2684012.4848"));
    }

    @Test
    void resetPassword_ShouldReturnSuccessMessage_WhenResetIsSuccessful() throws Exception {
        PasswordResetRequest resetRequest = new PasswordResetRequest();
        resetRequest.setPassword("123456");
        resetRequest.setPassword2("123456");
        String token="eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJwaHVvbmdAZ21haWwuY29tIiwiZXhwIjoxNzMyOTgwNjU5fQ.P2ErGXXpXen9_ASzvYg9yCokJHqcUd5vIjY1VFXeWyk";
        Mockito.when(userService.verifyPasswordResetCode(any(String.class), any(PasswordResetRequest.class))).thenReturn(MessageCodes.PROCESSED_SUCCESSFULLY);

        mockMvc.perform(post("/user/reset-password")
                        .param("token", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(resetRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(MessageCodes.PROCESSED_SUCCESSFULLY))
                .andDo(print());
    }

    @Test
    void getUser_ShouldReturnUser_WhenUserExists() throws Exception {
        UserResponseDto mockResponse = UserResponseDto.builder()
                .id(1L)
                .username("user1")
                .email("user1@gmail.com")
                .build();
        Mockito.when(userService.getUser(anyLong())).thenReturn(mockResponse);

        mockMvc.perform(get("/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andExpect(jsonPath("$.data.username").value("user1"))
                .andDo(print());
    }
}