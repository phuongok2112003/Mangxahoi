package com.example.Mangxahoi.service;

import com.example.Mangxahoi.constans.ErrorCodes;
import com.example.Mangxahoi.constans.MessageCodes;
import com.example.Mangxahoi.constans.enums.UserRole;
import com.example.Mangxahoi.dto.response.FavoriteResponse;
import com.example.Mangxahoi.entity.FavoriteEntity;
import com.example.Mangxahoi.entity.PostEntity;
import com.example.Mangxahoi.entity.UserEntity;
import com.example.Mangxahoi.error.CommonStatus;
import com.example.Mangxahoi.exceptions.EOException;
import com.example.Mangxahoi.repository.LikeRepository;
import com.example.Mangxahoi.repository.PostRepository;
import com.example.Mangxahoi.repository.UserRepository;
import com.example.Mangxahoi.services.Impl.LikeServiceImpl;
import com.example.Mangxahoi.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.assertEquals;

@SpringBootTest
@AutoConfigureMockMvc
@RequiredArgsConstructor
@Slf4j
public class LikeService {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PostRepository postRepository;
    @Mock
    private LikeRepository likeRepository;
    @InjectMocks
    private LikeServiceImpl likeService;

    private UserEntity userEntity;
    private UserEntity userPost;
    private PostEntity postEntity;
    private FavoriteEntity favoriteEntity;

    @BeforeEach
    public void setUp() {
        userEntity=UserEntity.builder()
                .email("admin@gmail.com")
                .username("admin")
                .dateBirth(LocalDate.parse("2003-11-02"))
                .id(1L)
                .gender(true)
                .build();
        userEntity.setRole(UserRole.USER);

        userPost=UserEntity.builder()
                .email("admin1@gmail.com")
                .username("admin")
                .dateBirth(LocalDate.parse("2003-11-02"))
                .id(2L)
                .gender(true)
                .build();
        userPost.setRole(UserRole.USER);

        postEntity = new PostEntity();
        postEntity.setId(1L);
        postEntity.setContent("Test post content");
        postEntity.setUser(userEntity);
        postEntity.setCreatedAt(Instant.now());
        postEntity.setUpdatedAt(Instant.now());

        favoriteEntity = FavoriteEntity.builder()
                .user(userEntity)
                .post(postEntity)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();
    }

    @Test
    public void testLike_Success() {
        try (MockedStatic<SecurityUtils> mockedUtils = mockStatic(SecurityUtils.class)) {
            mockedUtils.when(SecurityUtils::getEmail).thenReturn(userEntity.getEmail());

            // Mock user and post retrieval from repositories
            when(userRepository.findByEmail(userEntity.getEmail())).thenReturn(userEntity);
            when(postRepository.findById(1L)).thenReturn(Optional.of(postEntity));

            // Mock saving the favorite entity
            when(likeRepository.save(any(FavoriteEntity.class))).thenReturn(favoriteEntity);

            // Call the like method
            FavoriteResponse response = likeService.like(1L);


            Assertions.assertEquals(favoriteEntity.getUser().getUsername(), response.getUsername());

        }
    }
    @Test
    public void testLike_UserNotFound() {
        try (MockedStatic<SecurityUtils> mockedUtils = mockStatic(SecurityUtils.class)) {
            mockedUtils.when(SecurityUtils::getEmail).thenReturn(userEntity.getEmail());


            when(userRepository.findByEmail(userEntity.getEmail())).thenReturn(null);

            EOException exception = assertThrows(EOException.class, () -> likeService.like(1L));
            Assertions.assertEquals(CommonStatus.ACCOUNT_NOT_FOUND.getMessage(), exception.getMessage());
        }
    }
    @Test
    public void testLike_PostNotFound() {
        try (MockedStatic<SecurityUtils> mockedUtils = mockStatic(SecurityUtils.class)) {
            mockedUtils.when(SecurityUtils::getEmail).thenReturn(userEntity.getEmail());


            when(userRepository.findByEmail(userEntity.getEmail())).thenReturn(userEntity);
            when(postRepository.findById(1L)).thenReturn(Optional.empty());

            EOException exception = assertThrows(EOException.class, () -> likeService.like(1L));
            Assertions.assertEquals(ErrorCodes.ENTITY_NOT_FOUND, exception.getCode());
        }
    }
    @Test
    public void testUnLike_Success() {
        try (MockedStatic<SecurityUtils> mockedUtils = mockStatic(SecurityUtils.class)) {
           mockedUtils.when(()->SecurityUtils.checkUser(userEntity.getUsername())).thenReturn(true);

            when(userRepository.findByEmail(userEntity.getEmail())).thenReturn(userEntity);
            when(likeRepository.findById(1L)).thenReturn(Optional.of(favoriteEntity));


            String response = likeService.unLike(1L);

            Assertions.assertEquals(MessageCodes.PROCESSED_SUCCESSFULLY, response);
        }
    }

    @Test
    public void testUnLike_FavoriteEntityNotFound() {
        when(likeRepository.findById(1L)).thenReturn(Optional.empty());

        EOException exception = assertThrows(EOException.class, () -> likeService.unLike(1L));
        Assertions.assertEquals(ErrorCodes.ENTITY_NOT_FOUND, exception.getCode());
    }

    @Test
    public void testUnLike_Forbidden() {
        try (MockedStatic<SecurityUtils> mockedUtils = mockStatic(SecurityUtils.class)) {
        when(likeRepository.findById(1L)).thenReturn(Optional.of(favoriteEntity));
       mockedUtils.when(()->SecurityUtils.checkUser("testuser")).thenReturn(false);

        EOException exception = assertThrows(EOException.class, () -> likeService.unLike(1L));
        Assertions.assertEquals(CommonStatus.FORBIDDEN.getMessage(), exception.getMessage());
    }
    }
}