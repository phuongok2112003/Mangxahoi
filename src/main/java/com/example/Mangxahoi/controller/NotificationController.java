package com.example.Mangxahoi.controller;

import com.example.Mangxahoi.dto.request.PostRequest;
import com.example.Mangxahoi.dto.response.NotificationResponse;
import com.example.Mangxahoi.dto.response.PostResponse;
import com.example.Mangxahoi.services.NotificationService;
import com.example.Mangxahoi.utils.ConvertUtils;
import com.example.Mangxahoi.utils.EOResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notification")
@Slf4j
public class NotificationController {

    private final NotificationService notificationService;
    @GetMapping("")
    public  EOResponse<List<NotificationResponse>> getNotification() {
        return EOResponse.build(notificationService.getNotificationAll());
    }
}
