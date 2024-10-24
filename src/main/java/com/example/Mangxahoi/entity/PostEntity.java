package com.example.Mangxahoi.entity;


import jakarta.persistence.*;
import lombok.*;


import java.time.Instant;
import java.util.Date;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "posts")
@Builder
public class PostEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserEntity user;

    @OneToMany(mappedBy = "post")
    private List<CommentEntity> comments;

    @OneToMany(mappedBy = "post")
    private List<FavoriteEntity> likes;

    @OneToMany(mappedBy = "post")
    private List<ImageEntity> images;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;
    @Column(name = "updated_at")
    private Instant updatedAt;
}