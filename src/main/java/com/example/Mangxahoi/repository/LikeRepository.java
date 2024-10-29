package com.example.Mangxahoi.repository;

import com.example.Mangxahoi.entity.FavoriteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;

public interface LikeRepository extends JpaRepository<FavoriteEntity,Long> {
    long countByUserIdAndCreatedAtBetween(Long userId, Instant startOfWeek, Instant endOfWeek);
}
