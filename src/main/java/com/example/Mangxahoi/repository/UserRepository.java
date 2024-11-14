package com.example.Mangxahoi.repository;


import com.example.Mangxahoi.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    @Query("SELECT u FROM UserEntity u WHERE u.email=:email")
    UserEntity findByEmail(String email);

    @Query("SELECT u FROM UserEntity u WHERE MONTH(u.dateBirth) = :month AND DAY(u.dateBirth) = :day")
    List<UserEntity> findUsersWithBirthdayToday(@Param("month") int month, @Param("day") int day);



}
