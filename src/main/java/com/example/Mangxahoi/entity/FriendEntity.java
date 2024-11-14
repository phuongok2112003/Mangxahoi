package com.example.Mangxahoi.entity;

import com.example.Mangxahoi.constans.enums.FriendshipStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "friend")
public class FriendEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne // Thay đổi từ @OneToOne thành @ManyToOne
    @JoinColumn(name = "sender_id")// sử dụng @JoinColumn
    private UserEntity sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private UserEntity receiver;

    @Enumerated(EnumType.STRING)
    private FriendshipStatus status; // PENDING, ACCEPTED, REJECTED

    @Column(name = "created_at")
    private Instant createdAt;

}

