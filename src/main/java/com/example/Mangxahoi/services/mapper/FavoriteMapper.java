package com.example.Mangxahoi.services.mapper;

import com.example.Mangxahoi.dto.response.FavoriteResponse;
import com.example.Mangxahoi.entity.FavoriteEntity;
import com.example.Mangxahoi.services.DateTimeService;

import static com.example.Mangxahoi.constans.enums.Variables.dateTimeService;

public class FavoriteMapper {

    public static FavoriteResponse entityToResponse(FavoriteEntity favoriteEntity){
     return   FavoriteResponse.builder()
                .id(favoriteEntity.getId())
                .createAt(dateTimeService.format(favoriteEntity.getCreatedAt()))
                .postId(favoriteEntity.getPost().getId())
                .username(favoriteEntity.getUser().getUsername())
                .build();
    }
}
