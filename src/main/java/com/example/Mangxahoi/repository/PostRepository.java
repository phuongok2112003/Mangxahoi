package com.example.Mangxahoi.repository;

import com.example.Mangxahoi.entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<PostEntity, Long> {
    @Query("SELECT p FROM PostEntity p " +
            "JOIN p.user u " +
            "JOIN FriendEntity f ON (f.sender = u OR f.receiver = u) " +
            "WHERE (f.sender.id = :userId OR f.receiver.id = :userId) " +
            "AND f.status = 'ACCEPTED' " +
            "AND u.id <> :userId " +
            "ORDER BY p.createdAt DESC")
    List<PostEntity> findPostOfFriend(Long userId);
}
