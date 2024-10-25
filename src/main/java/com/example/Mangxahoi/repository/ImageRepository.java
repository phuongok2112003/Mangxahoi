package com.example.Mangxahoi.repository;

import com.example.Mangxahoi.entity.ImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ImageRepository extends JpaRepository<ImageEntity,Long> {
    @Query("SELECT i FROM ImageEntity i WHERE i.url IN :urls")
    List<ImageEntity> findByUrlAll(@Param("urls") List<String> urls);
    @Query("SELECT i FROM ImageEntity i WHERE i.post =:id")
    List<ImageEntity> findByPost(@Param("id")Long id);

}
