package com.example.Mangxahoi.services;
import com.example.Mangxahoi.dto.request.EmailRequest;
import com.example.Mangxahoi.dto.request.Recipient;
import com.example.Mangxahoi.dto.request.SendEmailRequest;
import com.example.Mangxahoi.dto.request.Sender;
import com.example.Mangxahoi.dto.response.EmailResponse;
import com.example.Mangxahoi.error.CommonStatus;
import com.example.Mangxahoi.exceptions.EOException;
import com.example.Mangxahoi.repository.httpClient.EmailClient;
import feign.FeignException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmailService {
    EmailClient emailClient;

    @Value("${notification.email.brevo-apikey}")
    @NonFinal
    String apiKey;

    public EmailResponse sendEmail(SendEmailRequest request) {
        List<Recipient> recipients=new ArrayList<>();
        recipients.add(request.getTo());
        EmailRequest emailRequest = EmailRequest.builder()
                .sender(Sender.builder()
                        .name("Social Network")
                        .email("phuong0961070156@gmail.com")
                        .build())
                .to(recipients)
                .subject(request.getSubject())
                .htmlContent(request.getHtmlContent())
                .build();
        try {
            return emailClient.sendEmail(apiKey, emailRequest);
        } catch (FeignException e){
            throw new EOException(CommonStatus.CANNOT_SEND_EMAIL);
        }
    }
}
