package com.example.Mangxahoi.entity;


import com.example.Mangxahoi.constans.enums.UserRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Entity
@Table(name = "users")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor

public class UserEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long  id;

    @Column(name = "username", nullable = false, length = 100)
    private String username;

    @Column(name = "email", nullable = false, length = 100)
    private String email;

    @Column(name = "password", nullable = false, length = 100)
    private String password;

    @Column(name = "role", nullable = false)

    private String role ;

    @Column(name = "verify_token", length = 191)
    private String verifyToken;

    @Column(name = "is_active", nullable = false)
    private boolean isActive=true ;

    @Column(name = "forgot_token", length = 512)
    private String forgotToken;

    @Column(name = "forgot_token_expire")
    private Date forgotTokenExpire;

    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

    @OneToMany(mappedBy = "user")
    private List<PostEntity> posts;

    @OneToMany(mappedBy = "user")
    private List<CommentEntity> comments;
    @OneToMany(mappedBy = "user")
    private List<LikeEntiy> likes;

    private List<FriendEntity> friends;

    @OneToMany(mappedBy = "user")
    private List<ImageEntity> images;

    public UserRole getPermission() {
        return UserRole.parseByCode(role);
    }

    public void setPermission(UserRole permission) {
        this.role = permission.toString();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return  Collections.singletonList(new SimpleGrantedAuthority(role));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isActive;
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", permission='" + role + '\'' +
                ", verifyToken='" + verifyToken + '\'' +
                ", isActive=" + isActive +
                ", forgotToken='" + forgotToken + '\'' +
                ", forgotTokenExpire=" + forgotTokenExpire +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", posts=" + posts +
                ", comments=" + comments +
                '}';
    }
}