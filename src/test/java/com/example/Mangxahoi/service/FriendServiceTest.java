package com.example.Mangxahoi.service;

import com.example.Mangxahoi.constans.MessageCodes;
import com.example.Mangxahoi.constans.enums.FriendshipStatus;
import com.example.Mangxahoi.constans.enums.UserRole;
import com.example.Mangxahoi.dto.request.FriendRequest;
import com.example.Mangxahoi.dto.response.FriendResponse;
import com.example.Mangxahoi.dto.response.PageResponse;
import com.example.Mangxahoi.dto.response.UserResponseDto;
import com.example.Mangxahoi.entity.FriendEntity;
import com.example.Mangxahoi.entity.UserEntity;
import com.example.Mangxahoi.error.CommonStatus;
import com.example.Mangxahoi.error.UserStatus;
import com.example.Mangxahoi.exceptions.EOException;
import com.example.Mangxahoi.exceptions.EntityNotFoundException;
import com.example.Mangxahoi.repository.FriendRepository;
import com.example.Mangxahoi.repository.UserRepository;
import com.example.Mangxahoi.services.DateTimeService;
import com.example.Mangxahoi.services.Impl.FriendServiceIml;
import com.example.Mangxahoi.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.Instant;
import java.time.LocalDate;
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
public class FriendServiceTest {
    @Mock
    private FriendRepository friendRepository;

    @Mock
    private UserRepository userRepository;
    @Mock
    private DateTimeService dateTimeService;

    @InjectMocks
    private FriendServiceIml friendService;
    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private UserEntity sender;
    private UserEntity receiver;
    FriendEntity friendEntity ;
    private Pageable pageable;
    private UserEntity friend1;
    private UserEntity friend2;
    @BeforeEach
    void setUp() {
        sender=UserEntity.builder()
                .email("user1@gmail.com")
                .username("user1")
                .dateBirth(LocalDate.parse("2003-11-02"))
                .id(1L)
                .gender(true)
                .password(bCryptPasswordEncoder.encode("1234"))
                .build();
        sender.setRole(UserRole.USER);

        receiver=UserEntity.builder()
                .email("user2@gmail.com")
                .username("user2")
                .dateBirth(LocalDate.parse("2003-11-02"))
                .id(2L)
                .gender(true)
                .password(bCryptPasswordEncoder.encode("1234"))
                .build();
        receiver.setRole(UserRole.USER);

        friendEntity = new FriendEntity();
        friendEntity.setSender(sender);
        friendEntity.setReceiver(receiver);
        friendEntity.setStatus(FriendshipStatus.PENDING);
        friendEntity.setCreatedAt(Instant.now());


        friend1 = new UserEntity();
        friend1.setId(2L);
        friend1.setUsername("user1");
        friend1.setEmail("user1@example.com");
        friend1.setCreatedAt(Instant.now());

        friend2 = new UserEntity();
        friend2.setId(3L);
        friend2.setUsername("user2");
        friend2.setEmail("user2@example.com");
        friend2.setCreatedAt(Instant.now());
        pageable = PageRequest.of(0, 5, Sort.by("createdAt").descending());
    }

    @Test
    void testAddFriend_Success() {
        try (MockedStatic<SecurityUtils> mockedUtils = mockStatic(SecurityUtils.class)) {
            mockedUtils.when(SecurityUtils::getEmail).thenReturn("user1@gmail.com");
            when(userRepository.findByEmail("user1@gmail.com")).thenReturn(sender);
            when(userRepository.findById(2L)).thenReturn(Optional.of(receiver));
            when(friendRepository.existsBySenderAndReceiver(sender, receiver)).thenReturn(false);
            when(friendRepository.save(friendEntity)).thenReturn(friendEntity);

            FriendResponse response = friendService.addFriend(2L);

            // Assert
            assertNotNull(response);
            assertEquals(FriendshipStatus.PENDING, response.getStatus());
        }
    }

    @Test
    void testAddFriend_AccountNotFound() {
        try (MockedStatic<SecurityUtils> mockedUtils = mockStatic(SecurityUtils.class)) {
            mockedUtils.when(SecurityUtils::getEmail).thenReturn("user1@gmail.com");
            when(userRepository.findByEmail("user1@gmail.com")).thenReturn(null);

        // Act & Assert
        EOException exception = assertThrows(EOException.class, () -> friendService.addFriend(2L));
        assertEquals(CommonStatus.ACCOUNT_NOT_FOUND.getMessage(), exception.getMessage());
    }
    }

    @Test
    void testAddFriend_FriendAlreadySent() {
        try (MockedStatic<SecurityUtils> mockedUtils = mockStatic(SecurityUtils.class)) {
            mockedUtils.when(SecurityUtils::getEmail).thenReturn("user1@gmail.com");
            when(userRepository.findByEmail("user1@gmail.com")).thenReturn(sender);
        when(userRepository.findById(2L)).thenReturn(Optional.of(receiver));
        when(friendRepository.existsBySenderAndReceiver(sender, receiver)).thenReturn(true);

        // Act & Assert
        EOException exception = assertThrows(EOException.class, () -> friendService.addFriend(2L));
        assertEquals(UserStatus.SENDED_FRIEND.getMessage(), exception.getMessage());
    }
    }


    @Test
    void testAddFriend_NotSendToSelf() {
        try (MockedStatic<SecurityUtils> mockedUtils = mockStatic(SecurityUtils.class)) {
            mockedUtils.when(SecurityUtils::getEmail).thenReturn("user1@gmail.com");
            when(userRepository.findByEmail("user1@gmail.com")).thenReturn(sender);
        when(userRepository.findById(1L)).thenReturn(Optional.of(sender));

        // Act & Assert
        EOException exception = assertThrows(EOException.class, () -> friendService.addFriend(1L));
        assertEquals( MessageCodes.NOT_SEND_ADD_FRIEND_FOR_ME, exception.getMessage());
    }
    }
    @Test
    void responseFriend_success() {


        Long senderId = 1L;
        FriendRequest request = new FriendRequest();
        request.setStatus(FriendshipStatus.ACCEPTED);
        try (MockedStatic<SecurityUtils> mockedUtils = mockStatic(SecurityUtils.class)) {
            mockedUtils.when(SecurityUtils::getEmail).thenReturn("user2@gmail.com");
            when(userRepository.findByEmail("user2@gmail.com")).thenReturn(receiver);


        when(userRepository.findById(senderId)).thenReturn(Optional.of(sender));


        when(friendRepository.findBySenderAndReceiver(sender, receiver)).thenReturn(friendEntity);
        when(friendRepository.save(friendEntity)).thenReturn(friendEntity);


        FriendResponse result = friendService.responseFriend(senderId, request);

        assertNotNull(result);
        assertEquals(FriendshipStatus.ACCEPTED, result.getStatus());
    }
    }

    @Test
    void responseFriend_friendEntityNotFound() {
        Long senderId = 1L;
        FriendRequest request = new FriendRequest();
        request.setStatus(FriendshipStatus.ACCEPTED);

        when(userRepository.findById(senderId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            friendService.responseFriend(senderId, request);
        });

        assertEquals( MessageCodes.ENTITY_NOT_FOUND, exception.getMessage());
    }

    @Test
    void getListFriend_success() {


        try (MockedStatic<SecurityUtils> mockedUtils = mockStatic(SecurityUtils.class)) {
            mockedUtils.when(SecurityUtils::getCurrentUser).thenReturn(sender);

            // Giả lập đầu ra cho findAllFriendsByUserId
            Page<UserEntity> friendPage = new PageImpl<>(List.of(friend1, friend2), pageable, 2);
            when(friendRepository.findAllFriendsByUserId(sender.getId(), pageable)).thenReturn(friendPage);

            // Gọi phương thức cần test
            PageResponse<FriendResponse> result = friendService.getListFriend(1, 5);

            // Kiểm tra kết quả
            assertEquals(1, result.getCurrentPage());
            assertEquals(5, result.getPageSize());
            assertEquals(2, result.getTotalElements());
            assertEquals(1, result.getTotalPages());
            assertEquals("user1", result.getData().get(0).getSender().getUsername());
            assertEquals("user2", result.getData().get(1).getSender().getUsername());

            // Kiểm tra gọi đúng phương thức phụ thuộc
            verify(friendRepository, times(1)).findAllFriendsByUserId(sender.getId(), pageable);
            verify(dateTimeService, times(2)).format(any());
    }}

    @Test
    void getListFriendPENDING_success() {
        try (MockedStatic<SecurityUtils> mockedUtils = mockStatic(SecurityUtils.class)) {


            FriendEntity pendingFriend = new FriendEntity();
            pendingFriend.setStatus(FriendshipStatus.PENDING);
            pendingFriend.setSender(sender);

            mockedUtils.when(()->SecurityUtils.getCurrentUser()).thenReturn(receiver);
            when(friendRepository.findAllFriendsPENDINGByUserId(receiver.getId())).thenReturn(List.of(pendingFriend));


            List<FriendResponse> result = friendService.getListFriendPENDING();

            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals(FriendshipStatus.PENDING, result.get(0).getStatus());
        }
    }
    @Test
    void rejected_success() {
        try (MockedStatic<SecurityUtils> mockedUtils = mockStatic(SecurityUtils.class)) {

        FriendEntity rejectedFriend = new FriendEntity();
        rejectedFriend.setStatus(FriendshipStatus.REJECTED);
        rejectedFriend.setSender(sender);

        mockedUtils.when(SecurityUtils::getCurrentUser).thenReturn(receiver);
        when(friendRepository.findAllFriendsREJECTEDByUserId(receiver.getId())).thenReturn(List.of(rejectedFriend));


        List<FriendResponse> result = friendService.rejected();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(FriendshipStatus.REJECTED, result.get(0).getStatus());
    }}
}
