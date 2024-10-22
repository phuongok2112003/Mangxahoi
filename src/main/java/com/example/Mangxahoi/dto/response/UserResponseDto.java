package com.example.Mangxahoi.dto.response;

import com.example.Mangxahoi.constans.enums.UserRole;
import com.example.Mangxahoi.repository.LikeRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
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
    private LocalDate dateBirth;
    private Date updatedAt;
    private List<PostResponse> posts;
    private List<CommentResponse> comments;
    private List<FavoriteResponse> favorite;
    private List<FriendResponse> friends;
    private List<ImageResponse> images;

}
