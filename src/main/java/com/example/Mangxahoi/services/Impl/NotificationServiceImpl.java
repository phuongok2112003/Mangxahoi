package com.example.Mangxahoi.services.Impl;

import com.example.Mangxahoi.constans.MessageCodes;
import com.example.Mangxahoi.dto.request.NotificationRequest;
import com.example.Mangxahoi.dto.response.NotificationResponse;
import com.example.Mangxahoi.entity.Notification;
import com.example.Mangxahoi.entity.UserEntity;
import com.example.Mangxahoi.exceptions.EOException;
import com.example.Mangxahoi.exceptions.EntityNotFoundException;
import com.example.Mangxahoi.repository.NotificationRepository;
import com.example.Mangxahoi.repository.UserRepository;
import com.example.Mangxahoi.services.DateTimeService;
import com.example.Mangxahoi.services.NotificationService;
import com.example.Mangxahoi.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.Mangxahoi.constans.ErrorCodes.ENTITY_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final DateTimeService dateTimeService;
    @Override
    public List<NotificationResponse> getNotificationAll() {
        UserEntity userEntity= SecurityUtils.getCurrentUser();

        List<Notification> notificationList=notificationRepository.findByUserIdOrderByCreatedAtDesc(userEntity.getId());

        return notificationList.stream().map(notification ->
                NotificationResponse.builder()
                        .content(notification.getContent())
                        .id(notification.getId())
                        .createdAt(dateTimeService.format(notification.getCreatedAt()))
                        .build()
        ).collect(Collectors.toList());
    }

    @Override
    public NotificationResponse createNotification(NotificationRequest notificationRequest) {


        UserEntity user=userRepository.findById(notificationRequest.getUserId()).orElseThrow(()->
                  new EntityNotFoundException(UserEntity.class.getName(), "id", notificationRequest.getUserId().toString()));

        Notification notification=Notification.builder()
                .user(user)
                .content(notificationRequest.getContent())
                .createdAt(Instant.now())
                .build();
        notification=notificationRepository.save(notification);

        return NotificationResponse.builder()
                .content(notification.getContent())
                .id(notification.getId())
                .createdAt(dateTimeService.format(notification.getCreatedAt()))
                .build();
    }
}
