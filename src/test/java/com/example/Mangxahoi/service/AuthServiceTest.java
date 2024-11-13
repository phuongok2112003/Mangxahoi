package com.example.Mangxahoi.service;

import com.example.Mangxahoi.dto.Otp;
import com.example.Mangxahoi.dto.request.LoginRequest;
import com.example.Mangxahoi.entity.UserEntity;
import com.example.Mangxahoi.error.CommonStatus;
import com.example.Mangxahoi.exceptions.EOException;
import com.example.Mangxahoi.repository.UserRepository;
import com.example.Mangxahoi.services.AuthService;
import com.example.Mangxahoi.services.Impl.AuthServiceImpl;
import com.example.Mangxahoi.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
public class AuthServiceTest {
    @Mock
    private AuthenticationManager authenticationManager;
    @InjectMocks
    private AuthServiceImpl authService;
    private LoginRequest loginRequest;
    @Mock
    private Authentication authentication;
    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private UserEntity userEntity;

    @Mock
    UserRepository userRepository;
    @BeforeEach
    void setUp() {
        loginRequest=LoginRequest.builder()
                .password("1234")
                .username("phuong@gmail.com")
                .build();
        userEntity=UserEntity.builder()
                .id(1L)
                .username("phuong")
                .email("phuong@gmail.com")
                .password(bCryptPasswordEncoder.encode("1234"))
                .build();

    }

    @Test
    void login_ShouldReturnOtp_WhenAuthenticationIsSuccessful() {
        // Given

        String username = loginRequest.getUsername();


        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userEntity);

        when(userRepository.save(userEntity)).thenReturn(userEntity);

        // When
        Otp result = authService.login(loginRequest);

        // Then
        assertNotNull(result);
        assertEquals(username, result.getEmail());
        assertNotNull(result.getCode());

    }

    @Test
    void login_ShouldReturnNull_WhenAuthenticationFails() {
        // Given
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(ProviderNotFoundException.class);

        // When
        EOException exception = assertThrows(EOException.class, () -> {
            authService.login(loginRequest);
        });


        assertEquals(CommonStatus.ACCOUNT_NOT_FOUND.getMessage(), exception.getMessage());
    }

}
