package com.example.Mangxahoi.services;

import com.example.Mangxahoi.dto.request.NotificationRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaSevice {
    private final NotificationService notificationService;
    @KafkaListener(topics = "add-friend")
    public void listener(NotificationRequest dto){
       notificationService.createNotification(dto);
    }

    @KafkaListener(topics = "birthday")
    public void listenerBirth(NotificationRequest dto){
        notificationService.createNotification(dto);
    }
}
