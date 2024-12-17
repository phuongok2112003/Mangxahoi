package com.example.Mangxahoi.repository;

import com.example.Mangxahoi.entity.FavoriteEntity;
import com.example.Mangxahoi.entity.PostEntity;
import com.example.Mangxahoi.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;

public interface LikeRepository extends JpaRepository<FavoriteEntity,Long> {
    boolean existsByPostAndUser(PostEntity post,UserEntity user);
    long countByUserIdAndCreatedAtBetween(Long userId, Instant startOfWeek, Instant endOfWeek);
    @Query("SELECT COUNT(f) FROM FavoriteEntity f " +
            "WHERE f.post.user.id = :userId " +
            "AND f.createdAt >= :startDate " +
            "AND f.createdAt <= :endDate")
    Long countLikesForAllPostsOfUserInLastWeek(@Param("userId") Long userId,
                                               @Param("startDate") Instant startDate,
                                               @Param("endDate") Instant endDate);
}
