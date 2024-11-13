package com.example.Mangxahoi.services;

import com.example.Mangxahoi.dto.request.NotificationRequest;
import com.example.Mangxahoi.dto.response.NotificationResponse;

import java.util.List;

public interface NotificationService {
    List<NotificationResponse> getNotificationAll();

    NotificationResponse createNotification(NotificationRequest notificationRequest);
}
