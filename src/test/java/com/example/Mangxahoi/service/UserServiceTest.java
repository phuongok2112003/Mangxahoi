package com.example.Mangxahoi.service;

import com.example.Mangxahoi.constans.enums.UserRole;
import com.example.Mangxahoi.dto.Otp;
import com.example.Mangxahoi.dto.request.UserRequest;
import com.example.Mangxahoi.dto.response.UserResponseDto;
import com.example.Mangxahoi.entity.UserEntity;
import com.example.Mangxahoi.error.UserStatus;
import com.example.Mangxahoi.exceptions.EOException;
import com.example.Mangxahoi.repository.UserRepository;
import com.example.Mangxahoi.services.EmailService;
import com.example.Mangxahoi.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.assertEquals;

@SpringBootTest
@Slf4j
@TestPropertySource("/test.properties")
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private EmailService emailService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserService userService;
//    @Autowired
//    UserDetailsService userDetailsService;

    private UserResponseDto userResponse;
    private UserRequest userRequest;
    private UserEntity user;
    @BeforeEach
    void setUp() {
       userRequest=UserRequest.builder()
               .password("1234")
               .email("admin@gmail.com")
               .username("admin")
               .role(UserRole.USER)
               .gender(true)
               .dateBirth(LocalDate.parse("2003-11-02"))
               .build();
       userResponse=UserResponseDto.builder()
               .id(1L)
               .username("admin")
               .email("admin@gmail.com")
               .build();

       user=UserEntity.builder()
               .email("admin@gmail.com")
               .username("admin")

               .dateBirth(LocalDate.parse("2003-11-02"))
               .id(1L)
               .gender(true)
               .password(bCryptPasswordEncoder.encode(userRequest.getPassword()))
               .build();
       user.setRole(UserRole.USER);

    }

    @Test
    void testRegister_UserAlreadyExists_ShouldThrowException() {
        when(userRepository.findByEmail(userRequest.getEmail())).thenReturn(user);  // Cấu hình findByEmail trả về user

        // Act & Assert: Kiểm tra xem ngoại lệ EOException có được ném ra khi đăng ký với email đã tồn tại
        EOException exception = assertThrows(EOException.class, () -> userService.register(userRequest));

        // Kiểm tra thông tin lỗi
        log.info("Lỗi: {}", exception.getMessage());
        Assertions.assertEquals(UserStatus.EMAIL_IS_EXIST.getMessage(), exception.getMessage());
    }

    @Test
    void testRegister_NewUser_ShouldReturnUserResponseDto() {
        // Arrange

        when(userRepository.findByEmail(userRequest.getEmail())).thenReturn(null);

        when(userRepository.save(any(UserEntity.class))).thenReturn(user);

        // Act
        UserResponseDto result = userService.register(userRequest);

        // Assert
        assertNotNull(result);
        Assertions.assertEquals(userRequest.getEmail(), result.getEmail());

    }

    @Test
    void testGetToken_InvalidOtp_ShouldThrowException() {
        // Arrange
        Otp otp = new Otp();
        otp.setEmail("admin@gmail.com");
        otp.setCode("1234");


        user.setOtp("123");
        when(userRepository.findByEmail(otp.getEmail())).thenReturn(user);

        // Act & Assert
        EOException exception = assertThrows(EOException.class, () -> userService.getToken(otp));
        Assertions.assertEquals(UserStatus.WRONG_OTP, exception.getValue());
    }
//
//    @Test
//    void testGetToken_ValidOtp_ShouldReturnTokenDto() {
//        // Arrange
//        Otp otp = new Otp();
//        otp.setEmail("user@example.com");
//        otp.setCode("validCode");
//
//        UserEntity user = new UserEntity();
//        user.setOtp("validCode");
//        when(userRepository.findByEmail(otp.getEmail())).thenReturn(user);
//
//        String accessToken = "accessToken";
//        String refreshToken = "refreshToken";
//        when(TokenUtils.createAccessToken(user)).thenReturn(accessToken);
//        when(TokenUtils.createRefreshToken(user.getEmail())).thenReturn(refreshToken);
//
//        // Act
//        TokenDto result = userService.getToken(otp);
//
//        // Assert
//        assertNotNull(result);
//        assertEquals(accessToken, result.getAccessToken());
//        assertEquals(refreshToken, result.getRefreshToken());
//    }
//
//    @Test
//    void testDelete_UserNotFound_ShouldThrowException() {
//        // Arrange
//        Long userId = 1L;
//        when(userRepository.findById(userId)).thenReturn(Optional.empty());
//
//        // Act & Assert
//        EOException exception = assertThrows(EOException.class, () -> userService.delete(userId));
//        assertEquals(MessageCodes.ENTITY_NOT_FOUND, exception.getErrorCode());
//    }
//
//    @Test
//    void testDelete_UserExists_ShouldReturnSuccessMessage() {
//        // Arrange
//        Long userId = 1L;
//        UserEntity user = new UserEntity();
//        user.setId(userId);
//        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
//
//        // Act
//        String result = userService.delete(userId);
//
//        // Assert
//        assertEquals(MessageCodes.PROCESSED_SUCCESSFULLY, result);
//        verify(userRepository, times(1)).save(user);
//    }
//
//    @Test
//    void testVerifyPasswordResetCode_InvalidToken_ShouldThrowTokenExpiredException() {
//        // Arrange
//        String token = "invalidToken";
//        PasswordResetRequest resetRequest = new PasswordResetRequest();
//        resetRequest.setPassword("password1");
//        resetRequest.setPassword2("password1");
//
//        when(TokenUtils.verifyToken(token)).thenThrow(TokenExpiredException.class);
//
//        // Act & Assert
//        EOException exception = assertThrows(EOException.class, () -> userService.verifyPasswordResetCode(token, resetRequest));
//        assertEquals(CommonStatus.TokenExpired, exception.getErrorCode());
//    }
}
