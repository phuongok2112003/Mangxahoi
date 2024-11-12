package com.example.Mangxahoi.service;

import com.example.Mangxahoi.constans.MessageCodes;
import com.example.Mangxahoi.constans.enums.UserRole;
import com.example.Mangxahoi.dto.request.ImageRequest;
import com.example.Mangxahoi.dto.request.PostRequest;
import com.example.Mangxahoi.dto.response.ImageResponse;
import com.example.Mangxahoi.dto.response.PostResponse;
import com.example.Mangxahoi.entity.ImageEntity;
import com.example.Mangxahoi.entity.PostEntity;
import com.example.Mangxahoi.entity.UserEntity;
import com.example.Mangxahoi.error.CommonStatus;
import com.example.Mangxahoi.exceptions.EOException;
import com.example.Mangxahoi.repository.ImageRepository;
import com.example.Mangxahoi.repository.PostRepository;
import com.example.Mangxahoi.repository.UserRepository;
import com.example.Mangxahoi.services.ImageService;
import com.example.Mangxahoi.services.Impl.PostServiceImpl;
import com.example.Mangxahoi.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
@RequiredArgsConstructor
@Slf4j
public class PostServiceTest {
    @Mock
    private ImageRepository imageRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ImageService imageService;
    @Mock
    private PostRepository postRepository;
    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @InjectMocks
    private PostServiceImpl postService;

    private UserEntity userEntity;
    private PostEntity postEntity;
    private PostRequest postRequest;
    private List<ImageEntity> image;
    private List<ImageResponse> responseList;

   private MockMultipartFile file1 ;
   private   MockMultipartFile file2 ;


    MultipartFile[] files ;
    @BeforeEach
    void setUp() {
        image=Arrays.asList(
                ImageEntity.builder()
                        .post(postEntity)
                        .url("anh1.jpg")
                        .build(),
                ImageEntity.builder()
                        .post(postEntity)
                        .url("anh2.jpg")
                        .build()
        );
        responseList=Arrays.asList(ImageResponse.builder()
                        .url("anh1.jpg")
                .build(),ImageResponse.builder()
                        .url("anh2.jpg")
                .build());
        userEntity=UserEntity.builder()
                .email("admin@gmail.com")
                .username("admin")
                .dateBirth(LocalDate.parse("2003-11-02"))
                .id(1L)
                .gender(true)
                .password(bCryptPasswordEncoder.encode("1234"))
                .build();
        userEntity.setRole(UserRole.USER);


        postEntity = new PostEntity();
        postEntity.setId(1L);
        postEntity.setContent("Test post content");
        postEntity.setUser(userEntity);
        postEntity.setImages(image);
        postEntity.setCreatedAt(Instant.now());
        postEntity.setUpdatedAt(Instant.now());

        // Set up mock PostRequest
        postRequest = new PostRequest();
        postRequest.setContent("Test post content");
        postRequest.setImageRequest(ImageRequest.builder()
                        .url(Arrays.asList("anh1.jp","anh2.jpg"))
                .build());

       file1 = new MockMultipartFile("file1", "file1.jpg", "image/jpeg", "file content 1".getBytes());
        file2 = new MockMultipartFile("file2", "file2.jpg", "image/jpeg", "file content 2".getBytes());

        // Gán các tệp vào mảng MultipartFile[]
        files = new MultipartFile[]{file1, file2};



    }

    @Test
    void testCreatePost_Success() {
        try( MockedStatic<SecurityUtils> mockedUtils = mockStatic(SecurityUtils.class)) {
            mockedUtils.when(SecurityUtils::getEmail).thenReturn("admin@gmail.com");
            when(userRepository.findByEmail("admin@gmail.com")).thenReturn(userEntity);
            when(postRepository.save(any(PostEntity.class))).thenReturn(postEntity);

            // Act
            PostResponse response = postService.createPost(postRequest, files);

            // Assert
            assertNotNull(response);
            assertEquals("Test post content", response.getContent());
            log.info(response.getImages().toString());

        }
    }
    @Test
    void testCreatePost_UserNotFound() {
        // Arrange
        try (MockedStatic<SecurityUtils> mockedUtils = mockStatic(SecurityUtils.class)) {
            mockedUtils.when(SecurityUtils::getEmail).thenReturn("admin@gmail.com");
            when(userRepository.findByEmail("admin@gmail.com")).thenReturn(null);


            // Act & Assert
            EOException exception = assertThrows(EOException.class, () -> {
                postService.createPost(postRequest, files);
            });
            Assertions.assertEquals(CommonStatus.ACCOUNT_NOT_FOUND.getMessage(), exception.getMessage());
        }
    }
    @Test
    void testUpdatePost_Success() {
        // Arrange
        Long postId = 1L;
        when(postRepository.findById(postId)).thenReturn(Optional.of(postEntity));

        when(postRepository.save(any(PostEntity.class))).thenReturn(postEntity);


        when(imageRepository.findByUrlAll(postRequest.getImageRequest().getUrl())).thenReturn(image);
        when(imageService.uploadImage(files)).thenReturn(responseList);
        PostResponse response = postService.updatePost(postId, postRequest, files);

        // Assert
        assertNotNull(response);
        assertEquals("Test post content", response.getContent());

    }

    @Test
    void updatePost_PostNotFound_ThrowsEOException() {
        // Arrange
        Long postId = 1L;


        when(postRepository.findById(postId)).thenReturn(Optional.empty());

        // Act & Assert
        EOException thrown = assertThrows(EOException.class, () -> {
            postService.updatePost(postId, postRequest, files);
        });

        assertEquals(MessageCodes.ENTITY_NOT_FOUND, thrown.getMessage());
        verify(postRepository, never()).save(any());
    }

    @Test
    void testGetPost_Success() {
        // Arrange
        Long postId = 1L;
        when(postRepository.findById(postId)).thenReturn(Optional.of(postEntity));

        // Act
        PostResponse response = postService.getPost(postId);

        // Assert
        assertNotNull(response);
        assertEquals("Test post content", response.getContent());
    }

    @Test
    void testGetPost_PostNotFound() {
        // Arrange
        Long postId = 1L;
        when(postRepository.findById(postId)).thenReturn(Optional.empty());

        // Act & Assert
        EOException exception = assertThrows(EOException.class, () -> {
            postService.getPost(postId);
        });
        assertEquals(MessageCodes.ENTITY_NOT_FOUND, exception.getMessage());
    }
}
