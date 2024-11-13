package com.example.Mangxahoi.scheduled;

import com.example.Mangxahoi.dto.request.NotificationRequest;
import com.example.Mangxahoi.entity.UserEntity;
import com.example.Mangxahoi.repository.FriendRepository;
import com.example.Mangxahoi.repository.UserRepository;
import com.example.Mangxahoi.services.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BirthdayService {
    private final UserRepository userRepository;
    private final NotificationService notificationService;
    private final FriendRepository friendRepository;
    private final KafkaTemplate<String,Object> template;
    @Scheduled(cron = "0 0 9 * * ?")
    public void checkBirthdaysAndSendNotifications() {
        List<UserEntity> users = userRepository.findAll();

        LocalDate today = LocalDate.now();
        int month = today.getMonthValue();
        int day = today.getDayOfMonth();

        List<UserEntity>entityList= userRepository.findUsersWithBirthdayToday(month, day);
        for(UserEntity user: entityList){

            template.send("birthday",NotificationRequest.builder()
                    .content("birthday: "+"Happy Birthday, " + user.getUsername() + "!")
                    .userId(user.getId())
                    .build());

           List<UserEntity>friendsByUserId= friendRepository.findAllFriendsByUserId(user.getId());
           for(UserEntity userEntity:friendsByUserId){

               template.send("birthday",NotificationRequest.builder()
                       .content("birthday: "+"Today is " + user.getUsername() + "'s birthday! Send them your best wishes!")
                       .userId(userEntity.getId())
                       .build());

           }

        }
    }
}
