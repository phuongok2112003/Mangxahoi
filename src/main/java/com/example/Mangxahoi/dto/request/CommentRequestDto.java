package com.example.Mangxahoi.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class CommentRequestDto {
    private int id;
    private UserRequestDto user;
    private String comment;
    private PostRequestDto post;
    private String status = "unseen";
    private Date createdAt;
    private Date updatedAt;
}
