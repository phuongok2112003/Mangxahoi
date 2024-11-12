package com.example.Mangxahoi.services.mapper;

import com.example.Mangxahoi.dto.response.*;
import com.example.Mangxahoi.entity.PostEntity;

import java.util.List;

import static com.example.Mangxahoi.constans.enums.Variables.dateTimeService;

public class PostMapper {
    public static PostResponse entiyToResponse(PostEntity entity){

        List<ImageResponse> imageResponseList=entity.getImages()!=null?
                entity.getImages().stream().map(ImageMapper::entityToResponse).toList():null;

        List<CommentResponse> commentResponseList=entity.getComments()!=null?
                entity.getComments().stream().map(CommentMapper::entityToResponse).toList():null;

        List<FavoriteResponse>favoriteResponses=entity.getLikes()!=null?
                entity.getLikes().stream().map(FavoriteMapper::entityToResponse).toList():null;

        return PostResponse.builder()
                .createdAt(dateTimeService.format(entity.getCreatedAt()))
                .id(entity.getId())
                .content(entity.getContent())
                .comments(commentResponseList)
                .status(entity.getStatus())
                .user(UserResponseDto.builder()
                        .id(entity.getUser().getId())
                        .gender(entity.getUser().getGender())
                        .username(entity.getUser().getUsername())
                        .email((entity.getUser().getEmail()))
                        .build())
                .likes(favoriteResponses)
                .images(imageResponseList)
                .build();
    }
}
