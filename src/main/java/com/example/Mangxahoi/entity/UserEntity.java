package com.example.Mangxahoi.entity;
import com.example.Mangxahoi.constans.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
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

    @Column(name = "full_name", nullable = false, length = 100)
    private String fullName;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password", nullable = false, length = 100)
    private String password;

    @Column(name = "role", nullable = false)
    private String role ;

    @Column(name = "gender", nullable = false)
    private Boolean gender ;

    @Column(name = "is_active")
    private boolean isActive=true ;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @Column(name = "occupation")
    private String occupation;//nghe nghiep

    @Column(name = "location")
    private String location;

    @Column(name = "about_me")
    private String aboutMe;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "dateBirth", nullable = false)
    private LocalDate dateBirth;

    @OneToMany(mappedBy = "user")
    private List<PostEntity> posts;

    @OneToMany(mappedBy = "user")
    private List<Notification> notifications;

    @OneToMany(mappedBy = "user")
    private List<CommentEntity> comments;
    @OneToMany(mappedBy = "user")
    private List<FavoriteEntity> likes;

    @OneToMany(mappedBy = "sender")
    private  Set<FriendEntity> sentFriendRequest;

    @OneToMany(mappedBy = "receiver")
    private Set<FriendEntity> receivedRequests = new HashSet<>();

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
    public String getUsername() {
        return email;
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


}