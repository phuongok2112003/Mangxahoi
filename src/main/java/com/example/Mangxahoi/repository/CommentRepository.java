package com.example.Mangxahoi.repository;

import com.example.Mangxahoi.entity.CommentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;

public interface CommentRepository extends JpaRepository<CommentEntity,Long> {
    long countByUserIdAndCreatedAtBetween(Long userId, Instant startOfWeek, Instant endOfWeek);
    @Query("SELECT COUNT(c) FROM CommentEntity c " +
            "WHERE c.post.user.id = :userId " +
            "AND c.createdAt >= :startDate " +
            "AND c.createdAt <= :endDate")
    Long countCommentForAllPostsOfUserInLastWeek(@Param("userId") Long userId,
                                               @Param("startDate") Instant startDate,
                                               @Param("endDate") Instant endDate);
    @Query(value = "SELECT c FROM CommentEntity c WHERE c.post.id = :postId")
    Page<CommentEntity> findCommentsByPostId(@Param("postId") Long postId, Pageable pageable);
}
