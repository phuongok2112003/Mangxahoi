package com.example.Mangxahoi.services.Impl;
import com.example.Mangxahoi.constans.MessageCodes;
import com.example.Mangxahoi.constans.enums.FriendshipStatus;
import com.example.Mangxahoi.dto.request.FriendRequest;
import com.example.Mangxahoi.dto.response.FriendResponse;
import com.example.Mangxahoi.dto.response.UserResponseDto;
import com.example.Mangxahoi.entity.FriendEntity;
import com.example.Mangxahoi.entity.UserEntity;
import com.example.Mangxahoi.error.CommonStatus;
import com.example.Mangxahoi.error.UserStatus;
import com.example.Mangxahoi.exceptions.EOException;
import com.example.Mangxahoi.repository.FriendRepository;
import com.example.Mangxahoi.repository.UserRepository;
import com.example.Mangxahoi.services.DateTimeService;
import com.example.Mangxahoi.services.FriendService;
import com.example.Mangxahoi.utils.EbsSecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

import static com.example.Mangxahoi.constans.ErrorCodes.ENTITY_NOT_FOUND;
import static com.example.Mangxahoi.constans.ErrorCodes.ERROR_CODE;

@Service
@RequiredArgsConstructor
public class FriendServiceIml implements FriendService {
    private final FriendRepository friendRepository;
    private final UserRepository userRepository;
    private final DateTimeService dateTimeService;

    @Transactional
    @Override
    public FriendResponse addFriend(Long receiverId) {
        String email = EbsSecurityUtils.getEmail();
        UserEntity sender = userRepository.findByEmail(email);
        if (null == sender) {
            throw new EOException(CommonStatus.ACCOUNT_NOT_FOUND);
        }
        UserEntity receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new EOException(ENTITY_NOT_FOUND,
                        MessageCodes.ENTITY_NOT_FOUND, String.valueOf(receiverId)));
        if (friendRepository.existsBySenderAndReceiver(sender, receiver)) {
            throw new EOException(UserStatus.SENDED_FRIEND);
        }
        if (sender != receiver) {
            FriendEntity friendEntity = FriendEntity.builder()
                    .receiver(receiver)
                    .sender(sender)
                    .createdAt(Instant.now())
                    .status(FriendshipStatus.PENDING)
                    .build();
            friendRepository.save(friendEntity);
            return FriendResponse.builder()
                    .createdAt(dateTimeService.format(friendEntity.getCreatedAt()))
                    .status(friendEntity.getStatus())
                    .receiver(UserResponseDto.builder()
                            .id(receiver.getId())
                            .aboutMe(receiver.getAboutMe())
                            .email(receiver.getEmail())
                            .gender(receiver.getGender())
                            .location(receiver.getLocation())
                            .occupation(receiver.getOccupation())
                            .username(receiver.getUsername())
                            .build()).build();
        } else {
            throw new EOException(ERROR_CODE,
                    MessageCodes.NOT_SEND_ADD_FRIEND_FOR_ME, String.valueOf(receiverId));
        }

    }

    @Override
    public FriendResponse responseFriend(Long senderId, FriendRequest request) {
        UserEntity sender = userRepository.findById(senderId)
                .orElseThrow(() -> new EOException(ENTITY_NOT_FOUND,
                        MessageCodes.ENTITY_NOT_FOUND, String.valueOf(senderId)));

        String email = EbsSecurityUtils.getEmail();
        UserEntity userCurr = userRepository.findByEmail(email);
        if (null == userCurr) {
            throw new EOException(CommonStatus.ACCOUNT_NOT_FOUND);
        }

        FriendEntity friendEntity = friendRepository.findBySenderAndReceiver(sender, userCurr);

        if (friendEntity == null) {
            throw new EOException(ENTITY_NOT_FOUND, MessageCodes.ACC_NOT_SEND_ADD_FRIEND, senderId.toString());
        }

        friendEntity.setStatus(request.getStatus());

        friendRepository.save(friendEntity);

        return FriendResponse.builder()
                .createdAt(dateTimeService.format(friendEntity.getCreatedAt()))
                .sender(UserResponseDto.builder()
                        .id(sender.getId())
                        .aboutMe(sender.getAboutMe())
                        .email(sender.getEmail())
                        .gender(sender.getGender())
                        .location(sender.getLocation())
                        .occupation(sender.getOccupation())
                        .username(sender.getUsername())
                        .build()).build();
    }

    @Override
    public List<FriendResponse> getListFriend(Long userId) {
        List<UserEntity> friendEntityList = friendRepository.findAllFriendsByUserId(userId);
        List<FriendResponse> friendResponses = friendEntityList.stream().map(friendEntity ->
                FriendResponse.builder()
                        .createdAt(dateTimeService.format(friendEntity.getCreatedAt()))
                        .sender(UserResponseDto.builder()
                                .id(friendEntity.getId())
                                .aboutMe(friendEntity.getAboutMe())
                                .email(friendEntity.getEmail())
                                .gender(friendEntity.getGender())
                                .location(friendEntity.getLocation())
                                .occupation(friendEntity.getOccupation())
                                .username(friendEntity.getUsername())
                                .build()).build()
        ).toList();
        return friendResponses;
    }

    @Override
    public List<FriendResponse> getListFriendPENDING(Long userId) {
        List<FriendEntity> friendEntityList = friendRepository.findAllFriendsPENDINGByUserId(userId);
        List<FriendResponse> friendResponses = friendEntityList.stream().map(friendEntity ->
                FriendResponse.builder()
                        .createdAt(dateTimeService.format(friendEntity.getCreatedAt()))
                        .sender(UserResponseDto.builder()
                                .id(friendEntity.getSender().getId())
                                .aboutMe(friendEntity.getSender().getAboutMe())
                                .email(friendEntity.getSender().getEmail())
                                .gender(friendEntity.getSender().getGender())
                                .location(friendEntity.getSender().getLocation())
                                .occupation(friendEntity.getSender().getOccupation())
                                .username(friendEntity.getSender().getUsername())
                                .build()).build()
        ).toList();
        return friendResponses;
    }

    @Override
    public List<FriendResponse> rejected(Long userId) {
        List<FriendEntity> friendEntityList = friendRepository.findAllFriendsREJECTEDByUserId(userId);
        List<FriendResponse> friendResponses = friendEntityList.stream().map(friendEntity ->
                FriendResponse.builder()
                        .createdAt(dateTimeService.format(friendEntity.getCreatedAt()))
                        .sender(UserResponseDto.builder()
                                .id(friendEntity.getSender().getId())
                                .aboutMe(friendEntity.getSender().getAboutMe())
                                .email(friendEntity.getSender().getEmail())
                                .gender(friendEntity.getSender().getGender())
                                .location(friendEntity.getSender().getLocation())
                                .occupation(friendEntity.getSender().getOccupation())
                                .username(friendEntity.getSender().getUsername())
                                .build()).build()
        ).toList();
        return friendResponses;
    }
}