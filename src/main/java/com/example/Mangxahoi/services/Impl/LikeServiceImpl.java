package com.example.Mangxahoi.services.Impl;

import com.example.Mangxahoi.constans.ErrorCodes;
import com.example.Mangxahoi.constans.MessageCodes;
import com.example.Mangxahoi.dto.request.FavoriteRequest;
import com.example.Mangxahoi.dto.response.FavoriteResponse;
import com.example.Mangxahoi.entity.CommentEntity;
import com.example.Mangxahoi.entity.FavoriteEntity;
import com.example.Mangxahoi.entity.PostEntity;
import com.example.Mangxahoi.entity.UserEntity;
import com.example.Mangxahoi.error.CommonStatus;
import com.example.Mangxahoi.exceptions.EOException;
import com.example.Mangxahoi.repository.CommentRepository;
import com.example.Mangxahoi.repository.LikeRepository;
import com.example.Mangxahoi.repository.PostRepository;
import com.example.Mangxahoi.repository.UserRepository;
import com.example.Mangxahoi.services.DateTimeService;
import com.example.Mangxahoi.services.LikeService;
import com.example.Mangxahoi.utils.EbsSecurityUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@AllArgsConstructor
public class LikeServiceImpl implements LikeService {
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final LikeRepository likeRepository;
    private final DateTimeService dateTimeService;
    @Override
    public FavoriteResponse like(Long postId) {
        String email = EbsSecurityUtils.getEmail();
        UserEntity userEntity = userRepository.findByEmail(email);
        if (null == userEntity) {
            throw new EOException(CommonStatus.ACCOUNT_NOT_FOUND);
        }
        PostEntity postEntity = postRepository.findById(postId).orElseThrow(
                () -> new EOException(ErrorCodes.ENTITY_NOT_FOUND,
                        MessageCodes.ENTITY_NOT_FOUND, String.valueOf(postId)));
        FavoriteEntity favoriteEntity= FavoriteEntity.builder()
                .post(postEntity)
                .user(userEntity)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        likeRepository.save(favoriteEntity);
        return FavoriteResponse.builder()
                .id(favoriteEntity.getId())
                .username(favoriteEntity.getUser().getUsername())
                .postId(favoriteEntity.getPost().getId())
                .createAt(dateTimeService.format(favoriteEntity.getCreatedAt()))
                .build();
    }

    @Override
    public String unLike(Long id) {

        FavoriteEntity favoriteEntity=likeRepository.findById(id).orElseThrow(
                () -> new EOException(ErrorCodes.ENTITY_NOT_FOUND,
                        MessageCodes.ENTITY_NOT_FOUND, String.valueOf(id)));
        if (EbsSecurityUtils.checkUser(favoriteEntity.getUser().getUsername())) {
            likeRepository.delete(favoriteEntity);
        }else {
            throw new EOException(CommonStatus.FORBIDDEN);
        }
        return MessageCodes.PROCESSED_SUCCESSFULLY;
    }
}
