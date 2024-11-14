package com.example.Mangxahoi.repository;

import com.example.Mangxahoi.entity.FriendEntity;
import com.example.Mangxahoi.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

public interface FriendRepository extends JpaRepository<FriendEntity,Long> {
    boolean existsBySenderAndReceiver(UserEntity sender, UserEntity receiver);
    FriendEntity findBySenderAndReceiver(UserEntity user, UserEntity friend);
    @Query("SELECT u FROM UserEntity u " +
            "JOIN FriendEntity f ON (f.sender.id = :userId AND f.receiver = u) " +
            "OR (f.receiver.id = :userId AND f.sender = u) " +
            "WHERE f.status = 'ACCEPTED'")
    Page<UserEntity> findAllFriendsByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT u FROM UserEntity u " +
            "JOIN FriendEntity f ON (f.sender.id = :userId AND f.receiver = u) " +
            "OR (f.receiver.id = :userId AND f.sender = u) " +
            "WHERE f.status = 'ACCEPTED'")
    List<UserEntity> findAllFriendsByUserId(@Param("userId") Long userId);

    @Query("SELECT i FROM FriendEntity i WHERE  i.receiver.id = :userid " +
            "AND i.status =PENDING")
    List<FriendEntity> findAllFriendsPENDINGByUserId(@Param("userid") Long userid);


    @Query("SELECT COUNT(f) FROM FriendEntity f WHERE f.status = 'ACCEPTED'" +
            " AND (f.sender.id = :userId OR f.receiver.id = :userId) " +
            "AND f.createdAt BETWEEN :startOfWeek AND :endOfWeek")
    long countNewFriends(@Param("userId") Long userId, @Param("startOfWeek") Instant startOfWeek,
                         @Param("endOfWeek") Instant endOfWeek);
}
