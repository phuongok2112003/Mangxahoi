package com.example.Mangxahoi.service;

import com.example.Mangxahoi.constans.enums.UserRole;
import com.example.Mangxahoi.dto.request.CommentRequest;
import com.example.Mangxahoi.dto.response.CommentResponse;
import com.example.Mangxahoi.entity.CommentEntity;
import com.example.Mangxahoi.entity.PostEntity;
import com.example.Mangxahoi.entity.UserEntity;
import com.example.Mangxahoi.repository.CommentRepository;
import com.example.Mangxahoi.repository.PostRepository;
import com.example.Mangxahoi.repository.UserRepository;
import com.example.Mangxahoi.services.Impl.CommentServiceImpl;
import com.example.Mangxahoi.services.mapper.CommentMapper;
import com.example.Mangxahoi.utils.SecurityUtils;
import com.example.Mangxahoi.utils.TokenUtils;
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

    private CommentEntity commentEntity;
    private CommentResponse commentResponse;
    private CommentRequest commentRequest;
    private UserEntity user;

    @BeforeEach
    public void setup() {

        user=UserEntity.builder()
                .email("admin@gmail.com")
                .username("admin")
                .dateBirth(LocalDate.parse("2003-11-02"))
                .id(1L)
                .gender(true)
                .password(bCryptPasswordEncoder.encode("1234"))
                .build();
        user.setRole(UserRole.USER);


        commentRequest.setComment("This is a test comment");
    }

    // Test for addComment method
    @Test
    void addComment_ShouldReturnCommentResponse_WhenSuccessful() {
        try (MockedStatic<SecurityUtils> mockedUtils = mockStatic(SecurityUtils.class)) {
            MockedStatic<CommentMapper> mockedStatic=mockStatic((CommentMapper.class));
            Long postId = 1L;
            String email = "test@example.com";
            CommentRequest commentRequest = new CommentRequest();
            commentRequest.setComment("This is a test comment");

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
            mockedStatic.when(()->CommentMapper.entityToResponse(any(CommentEntity.class))).thenReturn(new CommentResponse());

            // When
            CommentResponse response = commentService.addComment(postId, commentRequest);

            // Then
            assertNotNull(response);
            verify(commentRepository).save(any(CommentEntity.class));
        }
    }
//    // Test for updateComment method
//    @Test
//    void updateComment_ShouldReturnUpdatedCommentResponse_WhenUserIsAuthorized() {
//        // Given
//        Long commentId = 1L;
//        String username = "testUser";
//        CommentRequest commentRequest = new CommentRequest();
//        commentRequest.setComment("Updated comment");
//
//        CommentEntity commentEntity = new CommentEntity();
//        UserEntity userEntity = new UserEntity();
//        userEntity.setUsername(username);
//        commentEntity.setUser(userEntity);
//
//        when(commentRepository.findById(commentId)).thenReturn(Optional.of(commentEntity));
//        when(SecurityUtils.checkUser(username)).thenReturn(true);
//        when(CommentMapper.entityToResponse(any(CommentEntity.class))).thenReturn(new CommentResponse());
//
//        // When
//        CommentResponse response = commentService.updateComment(commentId, commentRequest);
//
//        // Then
//        assertNotNull(response);
//        assertEquals("Updated comment", commentEntity.getContent());
//        verify(commentRepository).save(commentEntity);
//    }
//
//    // Test for deleteComment method
//    @Test
//    void deleteComment_ShouldReturnSuccessMessage_WhenUserIsAuthorized() {
//        // Given
//        Long commentId = 1L;
//        String username = "testUser";
//
//        CommentEntity commentEntity = new CommentEntity();
//        UserEntity userEntity = new UserEntity();
//        userEntity.setUsername(username);
//        commentEntity.setUser(userEntity);
//
//        when(commentRepository.findById(commentId)).thenReturn(Optional.of(commentEntity));
//        when(SecurityUtils.checkUser(username)).thenReturn(true);
//
//        // When
//        String result = commentService.deleteComment(commentId);
//
//        // Then
//        assertEquals(MessageCodes.PROCESSED_SUCCESSFULLY, result);
//        verify(commentRepository).delete(commentEntity);
//    }
//
//    // Test for deleteComment when unauthorized
//    @Test
//    void deleteComment_ShouldReturnFailureMessage_WhenUserIsUnauthorized() {
//        // Given
//        Long commentId = 1L;
//        String username = "unauthorizedUser";
//
//        CommentEntity commentEntity = new CommentEntity();
//        UserEntity userEntity = new UserEntity();
//        userEntity.setUsername("testUser");
//        commentEntity.setUser(userEntity);
//
//        when(commentRepository.findById(commentId)).thenReturn(Optional.of(commentEntity));
//        when(SecurityUtils.checkUser(username)).thenReturn(false);
//
//        // When
//        String result = commentService.deleteComment(commentId);
//
//        // Then
//        assertEquals(MessageCodes.FAILURE, result);
//        verify(commentRepository, never()).delete(commentEntity);
//    }
}
