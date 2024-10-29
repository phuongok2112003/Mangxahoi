package com.example.Mangxahoi.services.mapper;

import com.example.Mangxahoi.dto.response.CommentResponse;
import com.example.Mangxahoi.entity.CommentEntity;
import com.example.Mangxahoi.services.DateTimeService;
import lombok.RequiredArgsConstructor;

import static com.example.Mangxahoi.constans.enums.Variables.dateTimeService;


public class CommentMapper {

    public static CommentResponse entityToResponse(CommentEntity commentEntity){
        return   CommentResponse.builder()
                .comment(commentEntity.getContent())
                .postId(commentEntity.getPost().getId())
                .createdAt(dateTimeService.format(commentEntity.getCreatedAt()))
                .username(commentEntity.getUser().getUsername())
                .build();
    }
}
