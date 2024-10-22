package com.example.Mangxahoi.repository;

import com.example.Mangxahoi.entity.FavoriteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<FavoriteEntity,Long> {
}
