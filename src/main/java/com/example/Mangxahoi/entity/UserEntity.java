package com.example.Mangxahoi.entity;


import com.example.Mangxahoi.constans.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long  id;

    @Column(name = "username", nullable = false, length = 100)
    private String username;

    @Column(name = "email", nullable = false, length = 100)
    private String email;

    @Column(name = "password", nullable = false, length = 100)
    private String password;

    @Column(name = "role", nullable = false)
    private String role ;

    @Column(name = "gender", nullable = false)
    private Boolean gender ;

    @Column(name = "is_active", nullable = false)
    private boolean isActive=true ;

    @Column(name = "created_at", nullable = false)
    private Date createdAt;
    @Column(name = "updated_at")
    private Date updatedAt;
    @Column(name = "otp")
    private String otp;

    @Column(name = "dateBirth", nullable = false)
    private LocalDate dateBirth;



    @OneToMany(mappedBy = "user")
    private List<PostEntity> posts;

    @OneToMany(mappedBy = "user")
    private List<CommentEntity> comments;
    @OneToMany(mappedBy = "user")
    private List<FavoriteEntity> likes;
    @OneToMany(mappedBy = "user")
    private List<ImageEntity> images;

    public UserRole getRole() {
        return UserRole.parseByCode(role);
    }

    public void setRole(UserRole role) {
        this.role = role.getCode();
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
                ", isActive=" + isActive +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", posts=" + posts +
                ", comments=" + comments +
                '}';
    }
}