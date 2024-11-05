package com.example.Mangxahoi.service;

import com.example.Mangxahoi.dto.Otp;
import com.example.Mangxahoi.dto.request.LoginRequest;
import com.example.Mangxahoi.entity.UserEntity;
import com.example.Mangxahoi.repository.UserRepository;
import com.example.Mangxahoi.services.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
@TestPropertySource("/test.properties")
public class AuthServiceTest {
    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserRepository userRepository;

    @Autowired
    private AuthService authService;


    private LoginRequest loginRequest;

    @MockBean
    private Authentication authentication;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private UserEntity userEntity;

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
        String password = loginRequest.getPassword();


        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userEntity);


        // When
        Otp result = authService.login(loginRequest);

        // Then
        assertNotNull(result);
        assertEquals(username, result.getEmail());
        assertNotNull(result.getCode());
//        verify(userEntity).setOtp(result.getCode());
//        verify(userRepository).save(userEntity);
    }

    @Test
    void login_ShouldReturnNull_WhenAuthenticationFails() {
        // Given
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new UsernameNotFoundException("User not found"));

        // When
        Otp result = authService.login(loginRequest);

        // Then
        assertNull(result);
    }

//    @Test
//    public void testLoginWithInvalidCredentials() {
//        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
//                .thenThrow(new org.springframework.security.authentication.BadCredentialsException("Invalid credentials"));
//
//        EOException exception = assertThrows(EOException.class, () -> authService.login(loginRequest));
//        assertEquals(CommonStatus.ACCOUNT_NOT_FOUND, exception.getStatus());
//    }

//    @Test
//    public void testLoginWhenAccountLocked() {
//        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
//                .thenThrow(new org.springframework.security.authentication.LockedException("Account is locked"));
//
//        EOException exception = assertThrows(EOException.class, () -> authService.login(loginRequest));
//        assertEquals(CommonStatus.ACCOUNT_HAS_BEEN_LOCKED, exception.getStatus());
//    }
//
//    @Test
//    public void testLoginWhenAccountDisabled() {
//        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
//                .thenThrow(new org.springframework.security.authentication.DisabledException("Account is disabled"));
//
//        EOException exception = assertThrows(EOException.class, () -> authService.login(loginRequest));
//        assertEquals(CommonStatus.ACCOUNT_IS_NOT_ACTIVATED, exception.getStatus());
//    }
}
