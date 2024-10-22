package com.example.Mangxahoi.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class CommentRequest {
    private int id;
    private UserRequest user;
    private String comment;
    private PostRequest post;
    private String status = "unseen";
    private Date createdAt;
    private Date updatedAt;
}
