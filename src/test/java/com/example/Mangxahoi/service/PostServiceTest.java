package com.example.Mangxahoi.service;

import com.example.Mangxahoi.constans.enums.UserRole;
import com.example.Mangxahoi.dto.request.PostRequest;
import com.example.Mangxahoi.dto.response.PostResponse;
import com.example.Mangxahoi.entity.ImageEntity;
import com.example.Mangxahoi.entity.PostEntity;
import com.example.Mangxahoi.entity.UserEntity;
import com.example.Mangxahoi.repository.PostRepository;
import com.example.Mangxahoi.repository.UserRepository;
import com.example.Mangxahoi.services.ImageService;
import com.example.Mangxahoi.services.Impl.PostServiceImpl;
import com.example.Mangxahoi.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
@RequiredArgsConstructor
@Slf4j
public class PostServiceTest {
    @Mock
    private ImageService imageService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PostRepository postRepository;
    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @InjectMocks
    private PostServiceImpl postService;

    private UserEntity userEntity;
    private PostEntity postEntity;
    private PostRequest postRequest;
    private List<ImageEntity> imageEntities;

    @BeforeEach
    void setUp() {

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
        postEntity.setImages(imageEntities);

        // Set up mock PostRequest
        postRequest = new PostRequest();
        postRequest.setContent("Test post content");
    }

    @Test
    void testCreatePost_Success() {
        MockedStatic<SecurityUtils> mockedUtils = mockStatic(SecurityUtils.class);
        mockedUtils.when(()->SecurityUtils.getEmail()).thenReturn("admin@gmail.com");
        when(userRepository.findByEmail("admin@gmail.com")).thenReturn(userEntity);
        when(postRepository.save(any(PostEntity.class))).thenReturn(postEntity);

        MockMultipartFile file1 = new MockMultipartFile("file1", "file1.jpg", "image/jpeg", "file content 1".getBytes());
        MockMultipartFile file2 = new MockMultipartFile("file2", "file2.jpg", "image/jpeg", "file content 2".getBytes());

        // Gán các tệp vào mảng MultipartFile[]
        MultipartFile[] files = {file1, file2}; // mock file upload if necessary

        // Act
        PostResponse response = postService.createPost(postRequest, files);

        // Assert
        assertNotNull(response);
        assertEquals("Test post content", response.getContent());
        log.info(response.getImages().toString());

    }

//    @Test
//    void testCreatePost_UserNotFound() {
//        // Arrange
//        when(SecurityUtils.getEmail()).thenReturn("test@example.com");
//        when(userRepository.findByEmail("test@example.com")).thenReturn(null);
//
//        MultipartFile[] files = {}; // mock file upload if necessary
//
//        // Act & Assert
//        EOException exception = assertThrows(EOException.class, () -> {
//            postService.createPost(postRequest, files);
//        });
//        assertEquals("Account not found", exception.getMessage());
//    }
//
//    @Test
//    void testUpdatePost_Success() {
//        // Arrange
//        Long postId = 1L;
//        when(postRepository.findById(postId)).thenReturn(Optional.of(postEntity));
//        when(postRepository.save(any(PostEntity.class))).thenReturn(postEntity);
//
//        MultipartFile[] files = {}; // mock file upload if necessary
//
//        // Act
//        PostResponse response = postService.updatePost(postId, postRequest, files);
//
//        // Assert
//        assertNotNull(response);
//        assertEquals("Test post content", response.getContent());
//        verify(postRepository, times(1)).save(any(PostEntity.class));
//    }
//
//    @Test
//    void testUpdatePost_PostNotFound() {
//        // Arrange
//        Long postId = 1L;
//        when(postRepository.findById(postId)).thenReturn(Optional.empty());
//
//        MultipartFile[] files = {}; // mock file upload if necessary
//
//        // Act & Assert
//        EOException exception = assertThrows(EOException.class, () -> {
//            postService.updatePost(postId, postRequest, files);
//        });
//        assertEquals("Entity not found", exception.getMessage());
//    }
//
//    @Test
//    void testGetPost_Success() {
//        // Arrange
//        Long postId = 1L;
//        when(postRepository.findById(postId)).thenReturn(Optional.of(postEntity));
//
//        // Act
//        PostResponse response = postService.getPost(postId);
//
//        // Assert
//        assertNotNull(response);
//        assertEquals("Test post content", response.getContent());
//    }
//
//    @Test
//    void testGetPost_PostNotFound() {
//        // Arrange
//        Long postId = 1L;
//        when(postRepository.findById(postId)).thenReturn(Optional.empty());
//
//        // Act & Assert
//        EOException exception = assertThrows(EOException.class, () -> {
//            postService.getPost(postId);
//        });
//        assertEquals("Entity not found", exception.getMessage());
//    }
}
