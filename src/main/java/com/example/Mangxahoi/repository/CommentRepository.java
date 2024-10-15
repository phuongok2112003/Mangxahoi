package com.example.Mangxahoi.repository;

import com.example.Mangxahoi.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<CommentEntity,Long> {
}
