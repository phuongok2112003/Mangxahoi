package com.example.Mangxahoi.repository;

import com.example.Mangxahoi.entity.PostEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<PostEntity, Long> {
    @Query("SELECT p FROM PostEntity p " +
            "JOIN p.user u " +
            "JOIN FriendEntity f ON (f.sender = u OR f.receiver = u) " +
            "WHERE (((f.sender.id = :userId OR f.receiver.id = :userId) " +
            "AND f.status = 'ACCEPTED') " +
            "OR u.id = :userId )" +
            "AND p.status='PUBLIC'")
    Page<PostEntity> findPostOfFriend(Long userId, Pageable pageable);

    Page<PostEntity> findByUserId(Long userId,Pageable pageable);

    long countByUserIdAndCreatedAtBetween(Long userId, Instant startOfWeek, Instant endOfWeek);
}
