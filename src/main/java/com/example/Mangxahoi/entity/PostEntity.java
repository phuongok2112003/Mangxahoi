package com.example.Mangxahoi.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.util.Date;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "posts")
public class PostEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id",referencedColumnName = "id")
    private UserEntity user;

    @OneToMany(mappedBy = "post")
    private List<CommentEntity> comments;

    @OneToMany(mappedBy = "post")
    private List<LikeEntity> likes;

    @OneToMany(mappedBy = "post")
    private List<ImageEntity> images;

    @Column(name = "created_at", nullable = false)
    private Date createdAt;
    @Column(name = "updated_at")
    private Date updatedAt;
}