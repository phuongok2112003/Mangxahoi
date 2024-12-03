package com.example.Mangxahoi.services.Impl;
import com.example.Mangxahoi.constans.MessageCodes;
import com.example.Mangxahoi.constans.enums.FriendshipStatus;
import com.example.Mangxahoi.dto.request.FriendRequest;
import com.example.Mangxahoi.dto.request.NotificationRequest;
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
import com.example.Mangxahoi.services.FriendService;
import com.example.Mangxahoi.services.NotificationService;
import com.example.Mangxahoi.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.kafka.core.KafkaTemplate;
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
    private final NotificationService notificationService;
    private final KafkaTemplate<String,Object> template;
    @Transactional
    @Override
    public FriendResponse addFriend(Long receiverId) {
        String email = SecurityUtils.getEmail();
        UserEntity sender = userRepository.findByEmail(email);
        if (null == sender) {
            throw new EOException(CommonStatus.ACCOUNT_NOT_FOUND);
        }
        UserEntity receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new EntityNotFoundException(UserEntity.class.getName(), "id", receiverId.toString()));
        if (friendRepository.existsBySenderAndReceiver(sender, receiver)||friendRepository.existsBySenderAndReceiver(receiver,sender)) {
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


            template.send("add-friend",NotificationRequest.builder()
                    .userId(receiverId)
                    .content("add-friend: "+sender.getUsername()+" send friend to you ")
                    .build());

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
                            .fullName(receiver.getFullName())
                            .build()).build();
        } else {
            throw new EOException(ERROR_CODE,
                    MessageCodes.NOT_SEND_ADD_FRIEND_FOR_ME, String.valueOf(receiverId));
        }

    }

    @Override
    public FriendResponse responseFriend(Long senderId, FriendRequest request) {
        UserEntity sender = userRepository.findById(senderId)
                .orElseThrow(() ->  new EntityNotFoundException(UserEntity.class.getName(), "id", senderId.toString()));

        String email = SecurityUtils.getEmail();
        UserEntity userCurr = userRepository.findByEmail(email);
        if (null == userCurr) {
            throw new EOException(CommonStatus.ACCOUNT_NOT_FOUND);
        }

        FriendEntity friendEntity = friendRepository.findBySenderAndReceiver(sender, userCurr);

        if (friendEntity == null) {
            throw new EOException(ENTITY_NOT_FOUND, MessageCodes.ACC_NOT_SEND_ADD_FRIEND, senderId.toString());
        }

        if(request.getStatus()==FriendshipStatus.REJECTED){
            friendRepository.delete(friendEntity);
        }
        else  {
            friendEntity.setStatus(request.getStatus());

            notificationService.createNotification(NotificationRequest.builder()
                    .userId(senderId)
                    .content(userCurr.getUsername() + " response is " + request.getStatus())
                    .build());
            friendEntity.setCreatedAt(Instant.now());

            friendRepository.save(friendEntity);
        }
        return FriendResponse.builder()
                .createdAt(dateTimeService.format(friendEntity.getCreatedAt()))
                .status(request.getStatus())
                .id(friendEntity.getId())
                .sender(UserResponseDto.builder()
                        .id(sender.getId())
                        .aboutMe(sender.getAboutMe())
                        .email(sender.getEmail())
                        .gender(sender.getGender())
                        .location(sender.getLocation())
                        .occupation(sender.getOccupation())
                        .fullName(sender.getFullName())
                        .build()).build();
    }

    @Override
    public PageResponse<FriendResponse> getListFriend(int page, int size) {
        UserEntity userEntity= SecurityUtils.getCurrentUser();
        Sort sort = Sort.by("createdAt").descending();
        Pageable pageable= PageRequest.of(page-1,size,sort);
        Page<UserEntity> friendEntityList = friendRepository.findAllFriendsByUserId(userEntity.getId(),pageable);
        List<FriendResponse> friendResponses = friendEntityList.stream().map(friendEntity ->
                FriendResponse.builder()
                        .createdAt(dateTimeService.format(friendEntity.getCreatedAt()))
                        .id(friendEntity.getId())
                        .sender(UserResponseDto.builder()
                                .id(friendEntity.getId())
                                .aboutMe(friendEntity.getAboutMe())
                                .email(friendEntity.getEmail())
                                .gender(friendEntity.getGender())
                                .location(friendEntity.getLocation())
                                .occupation(friendEntity.getOccupation())
                                .fullName(friendEntity.getFullName())
                                .build()).build()
        ).toList();
        return PageResponse.<FriendResponse>builder()
                .currentPage(page)
                .pageSize(friendEntityList.getSize())
                .totalElements(friendEntityList.getTotalElements())
                .content(friendResponses)
                .totalPages(friendEntityList.getTotalPages())
                .build();
    }

    @Override
    public List<FriendResponse> getListFriendPENDING() {
        UserEntity userEntity= SecurityUtils.getCurrentUser();
        List<FriendEntity> friendEntityList = friendRepository.findAllFriendsPENDINGByUserId(userEntity.getId());
        List<FriendResponse> friendResponses = friendEntityList.stream().map(friendEntity ->
                FriendResponse.builder()
                        .createdAt(dateTimeService.format(friendEntity.getCreatedAt()))
                        .status(friendEntity.getStatus())
                        .id(friendEntity.getId())
                        .sender(UserResponseDto.builder()
                                .id(friendEntity.getSender().getId())
                                .aboutMe(friendEntity.getSender().getAboutMe())
                                .email(friendEntity.getSender().getEmail())
                                .gender(friendEntity.getSender().getGender())
                                .location(friendEntity.getSender().getLocation())
                                .occupation(friendEntity.getSender().getOccupation())
                                .fullName(friendEntity.getSender().getFullName())
                                .build()).build()
        ).toList();
        return friendResponses;
    }

}