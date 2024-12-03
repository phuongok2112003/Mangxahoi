package com.example.Mangxahoi.services.Impl;

import com.example.Mangxahoi.constans.ErrorCodes;
import com.example.Mangxahoi.constans.MessageCodes;
import com.example.Mangxahoi.constans.enums.PostStatus;
import com.example.Mangxahoi.dto.request.CommentRequest;
import com.example.Mangxahoi.dto.request.NotificationRequest;
import com.example.Mangxahoi.dto.response.CommentResponse;
import com.example.Mangxahoi.dto.response.PageResponse;
import com.example.Mangxahoi.entity.CommentEntity;
import com.example.Mangxahoi.entity.PostEntity;
import com.example.Mangxahoi.entity.UserEntity;
import com.example.Mangxahoi.error.CommonStatus;
import com.example.Mangxahoi.exceptions.EOException;
import com.example.Mangxahoi.exceptions.EntityNotFoundException;
import com.example.Mangxahoi.repository.CommentRepository;
import com.example.Mangxahoi.repository.PostRepository;
import com.example.Mangxahoi.repository.UserRepository;
import com.example.Mangxahoi.services.CommentService;
import com.example.Mangxahoi.services.NotificationService;
import com.example.Mangxahoi.services.mapper.CommentMapper;
import com.example.Mangxahoi.utils.SecurityUtils;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final NotificationService notificationService;
    @Override
    public CommentResponse addComment(Long postId,CommentRequest commentRequest) {
        String email = SecurityUtils.getEmail();
        UserEntity userEntity = userRepository.findByEmail(email);
        if (null == userEntity) {
            throw new EOException(CommonStatus.ACCOUNT_NOT_FOUND);
        }
        PostEntity postEntity = postRepository.findById(postId).orElseThrow(
                () ->  new EntityNotFoundException(PostEntity.class.getName(), "id", postId.toString()));

        if(!SecurityUtils.checkUser(postEntity.getUser().getUsername())&&postEntity.getStatus().equals(PostStatus.PRIVATE))
            throw new EOException(CommonStatus.FORBIDDEN);

        CommentEntity comment = CommentEntity.builder()
                .user(userEntity)
                .post(postEntity)
                .content(commentRequest.getComment())
                .createdAt(Instant.now())
                .build();

        commentRepository.save(comment);
        notificationService.createNotification(NotificationRequest.builder()
                .userId(postEntity.getUser().getId())
                .content(userEntity.getUsername()+" comment in post you ")
                .build());
        return CommentMapper.entityToResponse(comment);
    }


    @Override
    public CommentResponse updateComment(Long id, CommentRequest commentRequest) {

        CommentEntity commentEntity=commentRepository.findById(id).orElseThrow(
                () ->  new EntityNotFoundException(CommentEntity.class.getName(), "id", id.toString()));
        if (SecurityUtils.checkUser(commentEntity.getUser().getUsername())) {
            commentEntity.setContent(commentRequest.getComment());
            commentEntity.setUpdatedAt(Instant.now());
            commentEntity.setCreatedAt(Instant.now());
            commentRepository.save(commentEntity);
            notificationService.createNotification(NotificationRequest.builder()
                    .userId(commentEntity.getPost().getUser().getId())
                    .content(commentEntity.getUser().getUsername()+" update comment in post you ")
                    .build());
        }else {
            throw new EOException(CommonStatus.FORBIDDEN);
        }
        return CommentMapper.entityToResponse(commentEntity);
        }

    @Override
    public String deleteComment(Long id) {
        CommentEntity commentEntity=commentRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(CommentEntity.class.getName(), "id", id.toString()));
        if (SecurityUtils.checkUser(commentEntity.getUser().getUsername())||
                SecurityUtils.checkUser(commentEntity.getPost().getUser().getUsername())) {
            commentRepository.delete(commentEntity);
            return  MessageCodes.PROCESSED_SUCCESSFULLY ;
        }
      return  MessageCodes.FAILURE ;
    }

    @Override
    public PageResponse<CommentResponse> getAll(Long postId, int page, int size) {
        PostEntity postEntity = postRepository.findById(postId).orElseThrow(
                () ->  new EntityNotFoundException(PostEntity.class.getName(), "id", postId.toString()));

        if(!SecurityUtils.checkUser(postEntity.getUser().getUsername())&&postEntity.getStatus().equals(PostStatus.PRIVATE))
            throw new EOException(CommonStatus.FORBIDDEN);
        Sort sort = Sort.by("createdAt").descending();
        Pageable pageable= PageRequest.of(page-1,size,sort);

        Page<CommentEntity> entityList= commentRepository.findCommentsByPostId(postId,pageable);
        List<CommentResponse> commentResponses=entityList.stream().map(CommentMapper::entityToResponse).collect(Collectors.toList());
        return PageResponse.<CommentResponse>builder()
                .currentPage(page)
                .pageSize(entityList.getSize())
                .totalElements(entityList.getTotalElements())
                .content(commentResponses)
                .totalPages(entityList.getTotalPages())
                .build();
    }


}
