package com.example.Mangxahoi.dto.response;

import com.example.Mangxahoi.constans.enums.UserRole;
import com.example.Mangxahoi.entity.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {
    private Long id;
    private String username;
    private String email;
    private String password;
    private UserRole role ;
    private Boolean gender ;
    private boolean isActive=true ;
    private Date createdAt;
    private  Date dateBirth;
    private Date updatedAt;
    private List<PostEntity> posts;
    private List<CommentEntity> comments;
    private List<LikeEntity> likes;
    private List<FriendEntity> friends;
    private List<ImageEntity> images;

}
