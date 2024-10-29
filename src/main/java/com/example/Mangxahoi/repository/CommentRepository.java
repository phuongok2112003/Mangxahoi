package com.example.Mangxahoi.repository;

import com.example.Mangxahoi.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;

public interface CommentRepository extends JpaRepository<CommentEntity,Long> {
    long countByUserIdAndCreatedAtBetween(Long userId, Instant startOfWeek, Instant endOfWeek);
}
