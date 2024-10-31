package com.example.Mangxahoi.controller;


import com.example.Mangxahoi.dto.Otp;
import com.example.Mangxahoi.dto.request.LoginRequest;
import com.example.Mangxahoi.services.AuthService;
import com.example.Mangxahoi.utils.EOResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testLogin_Success() throws Exception {
        // Mock dữ liệu đầu vào và đầu ra
        LoginRequest loginRequest = new LoginRequest("phuong0961070156@gmail.com", "1234");
        Otp otp = new Otp("1248","phuong0961070156@gmail.com");
        EOResponse<Otp> response = EOResponse.build(otp);

        // Cấu hình hành vi của AuthService
        Mockito.when(authService.login(ArgumentMatchers.any())).thenReturn(otp);

        // Gửi request và kiểm tra response
        mockMvc.perform(MockMvcRequestBuilders.post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))

                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("data.code").value("1248"))
                .andDo(print());

    }

    @Test
    void testLogin_InvalidRequest() throws Exception {
        // Tạo một LoginRequest không hợp lệ
        LoginRequest invalidRequest = new LoginRequest("", "password");

        // Gửi request và kiểm tra lỗi validation
        mockMvc.perform(MockMvcRequestBuilders.post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(print());;
    }
}
