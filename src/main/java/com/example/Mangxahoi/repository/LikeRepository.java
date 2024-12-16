package com.example.Mangxahoi.repository;

import com.example.Mangxahoi.entity.FavoriteEntity;
import com.example.Mangxahoi.entity.PostEntity;
import com.example.Mangxahoi.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;

public interface LikeRepository extends JpaRepository<FavoriteEntity,Long> {
    boolean existsByPostAndUser(PostEntity post,UserEntity user);
    long countByUserIdAndCreatedAtBetween(Long userId, Instant startOfWeek, Instant endOfWeek);
}
