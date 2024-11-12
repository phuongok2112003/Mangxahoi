package com.example.Mangxahoi.service;

import com.example.Mangxahoi.constans.enums.UserRole;
import com.example.Mangxahoi.dto.response.ImageResponse;
import com.example.Mangxahoi.entity.UserEntity;
import com.example.Mangxahoi.repository.UserRepository;
import com.example.Mangxahoi.services.Impl.ImageServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;

import static org.aspectj.weaver.tools.cache.SimpleCacheFactory.path;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
    @Mock
    private MultipartFile multipartFile;

    @Value("${file.upload-image-dir}")
    private String IMAGE_UPLOAD_DIR = "test-directory/";
    private final String path = "temp/test-image.jpg";
    private UserEntity userEntity;

    @BeforeEach
    void setUp() throws IOException {
        userEntity = UserEntity.builder()
                .email("admin@gmail.com")
                .username("admin")
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
        // Thiết lập các giả định cho multipartFile
        when(multipartFile.isEmpty()).thenReturn(false);
        when(multipartFile.getContentType()).thenReturn(MediaType.IMAGE_JPEG_VALUE);
        when(multipartFile.getOriginalFilename()).thenReturn("test.jpg");

        // Giả lập trả về một mảng byte hợp lệ cho multipartFile.getBytes()
        byte[] dummyContent = "dummy image content".getBytes();
        when(multipartFile.getBytes()).thenReturn(dummyContent);

        // Giả lập hành vi Files.write
        Path mockPath = Path.of(path);
        when(Files.write(mockPath, dummyContent)).thenReturn(mockPath);

        // Thực hiện phương thức uploadImage và kiểm tra kết quả
        List<ImageResponse> result = imageService.uploadImage(new MultipartFile[]{multipartFile});

        // Kiểm tra số lượng ảnh đã tải lên
        assertEquals(1, result.size());
        assertEquals("/post-image/test.jpg", result.get(0).getUrl());

        // Kiểm tra rằng getBytes đã được gọi một lần
        verify(multipartFile, times(1)).getBytes();
    }
//    @Test
//    void testUploadImage_FileEmpty() {
//        when(multipartFile.isEmpty()).thenReturn(true);
//
//        EOException exception = assertThrows(EOException.class, () -> {
//            imageService.uploadImage(new MultipartFile[]{multipartFile});
//        });
//
//        assertEquals(MessageCodes.NOT_NULL, exception.getMessageCode());
//    }
//
//    @Test
//    void testUploadImage_InvalidFormat() {
//        when(multipartFile.isEmpty()).thenReturn(false);
//        when(multipartFile.getContentType()).thenReturn(MediaType.APPLICATION_PDF_VALUE);
//
//        EOException exception = assertThrows(EOException.class, () -> {
//            imageService.uploadImage(new MultipartFile[]{multipartFile});
//        });
//
//        assertEquals(MessageCodes.FILE_UPLOAD_NOT_FORMAT, exception.getMessageCode());
//    }
//
//    @Test
//    void testGetImage_Success() throws IOException {
//        String fileName = "test-image.jpg";
//        Path path = Paths.get(IMAGE_UPLOAD_DIR + fileName);
//        Files.createDirectories(path.getParent());
//        Files.write(path, "dummy data".getBytes());
//
//        byte[] result = imageService.getImage(fileName);
//
//        assertNotNull(result);
//        assertTrue(result.length > 0);
//        Files.deleteIfExists(path);
//    }
//
//    @Test
//    void testGetImage_FileNotFound() {
//        byte[] result = imageService.getImage("non-existent.jpg");
//
//        assertNull(result);
//    }
//
//    @Test
//    void testDeleteImage_Success() throws IOException {
//        String fileName = "test-image.jpg";
//        Path path = Paths.get(IMAGE_UPLOAD_DIR + fileName);
//        Files.createDirectories(path.getParent());
//        Files.write(path, "dummy data".getBytes());
//
//        String result = imageService.deleteImage(fileName);
//
//        assertEquals(MessageCodes.PROCESSED_SUCCESSFULLY, result);
//        assertFalse(Files.exists(path));
//    }
//
//    @Test
//    void testDeleteImage_FileNotFound() {
//        String result = imageService.deleteImage("non-existent.jpg");
//
//        assertNotEquals(MessageCodes.PROCESSED_SUCCESSFULLY, result);
//    }
//
//    @Test
//    void testUpdateImage_Success() {
//        ImageRequest imageRequest = new ImageRequest();
//        imageRequest.setUrl(Arrays.asList("/old-image.jpg"));
//
//        List<ImageResponse> result = imageService.updateImage(imageRequest, new MultipartFile[]{multipartFile});
//
//        assertNotNull(result);
//        assertFalse(result.isEmpty());
//    }
//
//    @Test
//    void testUploadAvatar_Success() throws IOException {
//        when(multipartFile.isEmpty()).thenReturn(false);
//        when(multipartFile.getContentType()).thenReturn(MediaType.IMAGE_JPEG_VALUE);
//        when(multipartFile.getOriginalFilename()).thenReturn("new-avatar.jpg");
//        when(userRepository.findByEmail(anyString())).thenReturn(user);
//        SecurityUtils.mockStatic("test@example.com");
//
//        ImageResponse response = imageService.uploadAvatar(multipartFile);
//
//        assertNotNull(response);
//        assertEquals("/avatar-image/new-avatar.jpg", response.getUrl());
//        verify(userRepository, times(1)).save(user);
//    }
//
//    @Test
//    void testUploadAvatar_UserNotFound() {
//        when(multipartFile.isEmpty()).thenReturn(false);
//        when(multipartFile.getContentType()).thenReturn(MediaType.IMAGE_JPEG_VALUE);
//        when(multipartFile.getOriginalFilename()).thenReturn("new-avatar.jpg");
//        when(userRepository.findByEmail(anyString())).thenReturn(null);
//        SecurityUtils.mockStatic("test@example.com");
//
//        EOException exception = assertThrows(EOException.class, () -> {
//            imageService.uploadAvatar(multipartFile);
//        });
//
//        assertEquals(CommonStatus.ACCOUNT_NOT_FOUND, exception.getCommonStatus());
//    }
//}
}