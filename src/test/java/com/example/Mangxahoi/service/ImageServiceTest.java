package com.example.Mangxahoi.service;

import com.example.Mangxahoi.constans.MessageCodes;
import com.example.Mangxahoi.constans.enums.UserRole;
import com.example.Mangxahoi.dto.request.ImageRequest;
import com.example.Mangxahoi.dto.response.ImageResponse;
import com.example.Mangxahoi.entity.UserEntity;
import com.example.Mangxahoi.error.CommonStatus;
import com.example.Mangxahoi.exceptions.EOException;
import com.example.Mangxahoi.repository.UserRepository;
import com.example.Mangxahoi.services.Impl.ImageServiceImpl;
import com.example.Mangxahoi.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;

import static org.aspectj.weaver.tools.cache.SimpleCacheFactory.path;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.util.AssertionErrors.assertNotNull;

@SpringBootTest
@AutoConfigureMockMvc
@RequiredArgsConstructor
@Slf4j
public class ImageServiceTest {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private ImageServiceImpl imageService;
    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private UserEntity userEntity;

    @BeforeEach
    void setUp() throws IOException {
        userEntity = UserEntity.builder()
                .email("admin@gmail.com")
                .fullName("admin")
                .dateBirth(LocalDate.parse("2003-11-02"))
                .id(1L)
                .gender(true)
                .password(bCryptPasswordEncoder.encode("1234"))
                .build();
        userEntity.setRole(UserRole.USER);
        userEntity.setAvatarUrl("/avatar-image/old-avatar.png");
        Path directoryPath = Paths.get("temp");
        if (Files.notExists(directoryPath)) {
            Files.createDirectories(directoryPath);
        }

    }

    @Test
    void testUploadImage_Success() throws IOException {
        MockMultipartFile mockFile = new MockMultipartFile("file", "image.jpg", MediaType.IMAGE_JPEG_VALUE, "test image content".getBytes());

        List<ImageResponse> response = imageService.uploadImage(new MockMultipartFile[]{mockFile});

        Assertions.assertNotNull(response);
        log.info(response.get(0).getUrl());
        assertEquals(1, response.size());
        assertTrue(response.get(0).getUrl().contains("/post-image/"));
    }

    @Test
    void testUploadImage_EmptyFile() {
        MockMultipartFile mockFile = new MockMultipartFile("file", "", MediaType.IMAGE_JPEG_VALUE, new byte[0]);

        EOException exception = assertThrows(EOException.class, () -> imageService.uploadImage(new MockMultipartFile[]{mockFile}));
        assertEquals(MessageCodes.NOT_NULL, exception.getMessage());
    }

    @Test
    void testUploadImage_InvalidFormat() {
        MockMultipartFile mockFile = new MockMultipartFile("file", "file.txt", MediaType.TEXT_PLAIN_VALUE, "text content".getBytes());

        EOException exception = assertThrows(EOException.class, () -> imageService.uploadImage(new MockMultipartFile[]{mockFile}));
        assertEquals(MessageCodes.FILE_UPLOAD_NOT_FORMAT, exception.getMessage());
    }

    @Test
    void testGetImage_Success() throws IOException {
        // Định nghĩa thư mục thử nghiệm và tên tệp tin
        String testDir = "uploads/test/";
        Files.createDirectories(Paths.get(testDir));
        Path testFilePath = Paths.get(testDir + "test-image.jpg");

        // Tạo tệp tin hình ảnh thử nghiệm trong thư mục mong muốn
        Files.write(testFilePath, "test image content".getBytes());

        // Đặt IMAGE_UPLOAD_DIR cho trường hợp kiểm thử này
        ReflectionTestUtils.setField(imageService, "IMAGE_UPLOAD_DIR", testDir);

        // Gọi phương thức getImage và kiểm tra kết quả đầu ra
        byte[] imageBytes = imageService.getImage("test-image.jpg");
        Assertions.assertNotNull(imageBytes);

        // Dọn dẹp
      Files.deleteIfExists(testFilePath);
    }



    @Test
    void testGetImage_FileNotFound() {
        byte[] imageBytes = imageService.getImage("nonexistent.jpg");
        assertNull(imageBytes);
    }

    @Test
    void testDeleteImage_Success() throws IOException {
        Path path = Paths.get("test-delete-image.jpg");
        Files.write(path, "test content".getBytes());

        String result = imageService.deleteImage("test-delete-image.jpg");
        assertEquals(MessageCodes.PROCESSED_SUCCESSFULLY, result);

        Files.deleteIfExists(path);
   }

    @Test
    void testUpdateImage_Success() {
        ImageRequest imageRequest = new ImageRequest(List.of("/post-image/test.jpg"));
        MockMultipartFile mockFile = new MockMultipartFile("file", "image.jpg", MediaType.IMAGE_JPEG_VALUE, "new image content".getBytes());

        List<ImageResponse> response = imageService.updateImage(imageRequest, new MockMultipartFile[]{mockFile});
        Assertions.assertNotNull(response);
        assertEquals(1, response.size());
    }

    @Test
    void testUploadAvatar_Success() {
        try (MockedStatic<SecurityUtils> mockedUtils = mockStatic(SecurityUtils.class)) {
        MockMultipartFile mockFile = new MockMultipartFile("file", "avatar.jpg", MediaType.IMAGE_JPEG_VALUE, "avatar content".getBytes());


        userEntity.setAvatarUrl("old-avatar.jpg");
        mockedUtils.when(SecurityUtils::getEmail).thenReturn(userEntity.getEmail());
        when(userRepository.findByEmail(anyString())).thenReturn(userEntity);

        ImageResponse response = imageService.uploadAvatar(mockFile);
        Assertions.assertNotNull(response);
        assertTrue(response.getUrl().contains("/avatar-image/"));

        verify(userRepository, times(1)).save(userEntity);
    }}

    @Test
    void testUploadAvatar_UserNotFound() {
        try (MockedStatic<SecurityUtils> mockedUtils = mockStatic(SecurityUtils.class)) {
        MockMultipartFile mockFile = new MockMultipartFile("file", "avatar.jpg", MediaType.IMAGE_JPEG_VALUE, "avatar content".getBytes());
         mockedUtils.when(SecurityUtils::getEmail).thenReturn(userEntity.getEmail());
        when(userRepository.findByEmail(anyString())).thenReturn(null);

        EOException exception = assertThrows(EOException.class, () -> imageService.uploadAvatar(mockFile));
        assertEquals(CommonStatus.ACCOUNT_NOT_FOUND.getMessage(), exception.getMessage());
    }}
}