package com.example.Mangxahoi.services.Impl;

import com.example.Mangxahoi.constans.ErrorCodes;
import com.example.Mangxahoi.constans.MessageCodes;
import com.example.Mangxahoi.dto.response.FavoriteResponse;
import com.example.Mangxahoi.entity.FavoriteEntity;
import com.example.Mangxahoi.entity.PostEntity;
import com.example.Mangxahoi.entity.UserEntity;
import com.example.Mangxahoi.error.CommonStatus;
import com.example.Mangxahoi.exceptions.EOException;
import com.example.Mangxahoi.exceptions.EntityNotFoundException;
import com.example.Mangxahoi.repository.LikeRepository;
import com.example.Mangxahoi.repository.PostRepository;
import com.example.Mangxahoi.repository.UserRepository;
import com.example.Mangxahoi.services.LikeService;
import com.example.Mangxahoi.services.mapper.FavoriteMapper;
import com.example.Mangxahoi.utils.SecurityUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@AllArgsConstructor
public class LikeServiceImpl implements LikeService {
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final LikeRepository likeRepository;
    @Override
    public FavoriteResponse like(Long postId) {
        String email = SecurityUtils.getEmail();
        UserEntity userEntity = userRepository.findByEmail(email);
        if (null == userEntity) {
            throw new EOException(CommonStatus.ACCOUNT_NOT_FOUND);
        }
        PostEntity postEntity = postRepository.findById(postId).orElseThrow(
                () ->  new EntityNotFoundException(PostEntity.class.getName(), "id", postId.toString()));
        FavoriteEntity favoriteEntity= FavoriteEntity.builder()
                .post(postEntity)
                .user(userEntity)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        likeRepository.save(favoriteEntity);
        return FavoriteMapper.entityToResponse(favoriteEntity);
    }

    @Override
    public String unLike(Long id) {

        FavoriteEntity favoriteEntity=likeRepository.findById(id).orElseThrow(
                () ->  new EntityNotFoundException(FavoriteEntity.class.getName(), "id", id.toString()));
        if (SecurityUtils.checkUser(favoriteEntity.getUser().getUsername())) {
            likeRepository.delete(favoriteEntity);
        }else {
            throw new EOException(CommonStatus.FORBIDDEN);
        }
        return MessageCodes.PROCESSED_SUCCESSFULLY;
    }
}
