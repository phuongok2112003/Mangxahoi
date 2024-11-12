package com.example.Mangxahoi.service;

import com.example.Mangxahoi.constans.MessageCodes;
import com.example.Mangxahoi.constans.enums.UserRole;
import com.example.Mangxahoi.dto.request.CommentRequest;
import com.example.Mangxahoi.dto.response.CommentResponse;
import com.example.Mangxahoi.entity.CommentEntity;
import com.example.Mangxahoi.entity.PostEntity;
import com.example.Mangxahoi.entity.UserEntity;
import com.example.Mangxahoi.error.CommonStatus;
import com.example.Mangxahoi.exceptions.EOException;
import com.example.Mangxahoi.repository.CommentRepository;
import com.example.Mangxahoi.repository.PostRepository;
import com.example.Mangxahoi.repository.UserRepository;
import com.example.Mangxahoi.services.Impl.CommentServiceImpl;
import com.example.Mangxahoi.services.mapper.CommentMapper;
import com.example.Mangxahoi.utils.SecurityUtils;
import com.example.Mangxahoi.utils.TokenUtils;
import jakarta.persistence.criteria.CriteriaBuilder;
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
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.Instant;
import java.time.LocalDate;
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
public class CommentServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private PostRepository postRepository;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @InjectMocks
    private CommentServiceImpl commentService;
    private PostEntity postEntity;
    private CommentEntity commentEntity;
    private CommentResponse commentResponse;
    private CommentRequest commentRequest;
    private UserEntity user;

    @BeforeEach
    public void setup() {
        commentRequest=new CommentRequest();
        commentRequest.setComment("This is a test comment");


        commentResponse=CommentResponse.builder()
                .comment("This is a test comment")
                .build();

        user=UserEntity.builder()
                .email("admin@gmail.com")
                .username("admin")
                .dateBirth(LocalDate.parse("2003-11-02"))
                .id(1L)
                .gender(true)
                .password(bCryptPasswordEncoder.encode("1234"))
                .build();
        user.setRole(UserRole.USER);

        postEntity = new PostEntity();
        postEntity.setId(1L);
        postEntity.setContent("Test post content");
        postEntity.setUser(user);

        postEntity.setCreatedAt(Instant.now());
        postEntity.setUpdatedAt(Instant.now());

      commentEntity=CommentEntity.builder()
              .id(1L)
              .post(postEntity)
              .user(user)
              .content("This is a test comment")
              .updatedAt(Instant.now())
              .createdAt(Instant.now())
              .build();

    }

    // Test for addComment method
    @Test
    void addComment_ShouldReturnCommentResponse_WhenSuccessful() {
        try (MockedStatic<SecurityUtils> mockedUtils = mockStatic(SecurityUtils.class)) {
            MockedStatic<CommentMapper> mockedStatic=mockStatic((CommentMapper.class));
            Long postId = 1L;
            String email = "test@example.com";


            UserEntity userEntity = new UserEntity();
            userEntity.setEmail(email);

            PostEntity postEntity = new PostEntity();
            postEntity.setId(postId);

            CommentEntity commentEntity = new CommentEntity();
            commentEntity.setUser(userEntity);
            commentEntity.setPost(postEntity);
            commentEntity.setContent(commentRequest.getComment());
            commentEntity.setCreatedAt(Instant.now());

            mockedUtils.when(SecurityUtils::getEmail).thenReturn(email);
            when(userRepository.findByEmail(email)).thenReturn(userEntity);
            when(postRepository.findById(postId)).thenReturn(Optional.of(postEntity));
            when(commentRepository.save(any(CommentEntity.class))).thenReturn(commentEntity);
            mockedStatic.when(()->CommentMapper.entityToResponse(any(CommentEntity.class))).thenReturn(commentResponse);

            // When
            commentResponse = commentService.addComment(postId, commentRequest);

            // Then
            assertNotNull(commentResponse);
            assertEquals("This is a test comment",commentResponse.getComment());
        }
    }
    // Test for updateComment method
    @Test
    void testAddComment_UserNotFound() {
        try (MockedStatic<SecurityUtils> mockedUtils = mockStatic(SecurityUtils.class)) {
            String email = "test@example.com";
            mockedUtils.when(SecurityUtils::getEmail).thenReturn(email);
        when(userRepository.findByEmail(anyString())).thenReturn(null);

        // Act & Assert
        EOException exception = assertThrows(EOException.class, () ->
                commentService.addComment(1L, commentRequest));
        assertEquals(CommonStatus.ACCOUNT_NOT_FOUND.getMessage(), exception.getMessage());
    }

    }

    @Test
    void testUpdateComment_Success() {
        try (MockedStatic<SecurityUtils> mockedUtils = mockStatic(SecurityUtils.class)) {
            when(commentRepository.findById(anyLong())).thenReturn(Optional.of(commentEntity));

            mockedUtils.when(()->SecurityUtils.checkUser(commentEntity.getUser().getUsername())).thenReturn(true);
            when(commentRepository.save(any(CommentEntity.class))).thenReturn(commentEntity);

            // Act
            CommentResponse response = commentService.updateComment(commentEntity.getId(), commentRequest);

            // Assert
            assertNotNull(response);
            verify(commentRepository, times(1)).save(any(CommentEntity.class));
            assertEquals(commentRequest.getComment(), commentEntity.getContent());
        }
    }
    @Test
    void testUpdateComment_Forbidden() {
        try (MockedStatic<SecurityUtils> mockedUtils = mockStatic(SecurityUtils.class)) {
            when(commentRepository.findById(anyLong())).thenReturn(Optional.of(commentEntity));
            mockedUtils.when(()->SecurityUtils.checkUser(commentEntity.getUser().getUsername())).thenReturn(false);

            // Act & Assert
            EOException exception = assertThrows(EOException.class, () ->
                    commentService.updateComment(commentEntity.getId(), commentRequest));
            assertEquals(CommonStatus.FORBIDDEN.getMessage(), exception.getMessage());
        }
    }
    @Test
    void testDeleteComment_Success() {
        try (MockedStatic<SecurityUtils> mockedUtils = mockStatic(SecurityUtils.class)) {
            when(commentRepository.findById(anyLong())).thenReturn(Optional.of(commentEntity));
            mockedUtils.when(()->SecurityUtils.checkUser(commentEntity.getUser().getUsername())).thenReturn(true);

            // Act
            String response = commentService.deleteComment(commentEntity.getId());

            // Assert
            assertEquals(MessageCodes.PROCESSED_SUCCESSFULLY, response);
            verify(commentRepository, times(1)).delete(any(CommentEntity.class));
        }
    }

    @Test
    void testDeleteComment_Failure() {
        try (MockedStatic<SecurityUtils> mockedUtils = mockStatic(SecurityUtils.class)) {
        when(commentRepository.findById(anyLong())).thenReturn(Optional.of(commentEntity));
        mockedUtils.when(()->SecurityUtils.checkUser(commentEntity.getUser().getUsername())).thenReturn(false);
            mockedUtils.when(()->SecurityUtils.checkUser(commentEntity.getPost().getUser().getUsername())).thenReturn(false);

        // Act
        String response = commentService.deleteComment(commentEntity.getId());

        // Assert
        assertEquals(MessageCodes.FAILURE, response);
        verify(commentRepository, never()).delete(any(CommentEntity.class));
    }
    }
}