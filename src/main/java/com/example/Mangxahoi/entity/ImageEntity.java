package com.example.Mangxahoi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "image")
public class ImageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String url;

    @ManyToOne
    @JoinColumn(name = "user_id",referencedColumnName = "id")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "post_id",referencedColumnName = "id")
    private PostEntity post;

    @Column(name = "created_at", nullable = false)
    private Date createdAt;
    @Column(name = "updated_at")
    private Date updatedAt;
}
