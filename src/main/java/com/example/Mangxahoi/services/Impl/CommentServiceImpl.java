package com.example.Mangxahoi.services.Impl;

import com.example.Mangxahoi.constans.ErrorCodes;
import com.example.Mangxahoi.constans.MessageCodes;
import com.example.Mangxahoi.dto.request.CommentRequest;
import com.example.Mangxahoi.dto.response.CommentResponse;
import com.example.Mangxahoi.dto.response.UserResponseDto;
import com.example.Mangxahoi.entity.CommentEntity;
import com.example.Mangxahoi.entity.PostEntity;
import com.example.Mangxahoi.entity.UserEntity;
import com.example.Mangxahoi.error.CommonStatus;
import com.example.Mangxahoi.exceptions.EOException;
import com.example.Mangxahoi.repository.CommentRepository;
import com.example.Mangxahoi.repository.PostRepository;
import com.example.Mangxahoi.repository.UserRepository;
import com.example.Mangxahoi.services.CommentService;
import com.example.Mangxahoi.services.DateTimeService;
import com.example.Mangxahoi.utils.EbsSecurityUtils;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@AllArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final DateTimeService dateTimeService;

    @Override
    public CommentResponse addComment(Long postId,CommentRequest commentRequest) {
        String email = EbsSecurityUtils.getEmail();
        UserEntity userEntity = userRepository.findByEmail(email);
        if (null == userEntity) {
            throw new EOException(CommonStatus.ACCOUNT_NOT_FOUND);
        }
        PostEntity postEntity = postRepository.findById(postId).orElseThrow(
                () -> new EOException(ErrorCodes.ENTITY_NOT_FOUND,
                        MessageCodes.ENTITY_NOT_FOUND, String.valueOf(postId)));

        CommentEntity comment = CommentEntity.builder()
                .user(userEntity)
                .post(postEntity)
                .content(commentRequest.getComment())
                .createdAt(Instant.now())
                .build();

        commentRepository.save(comment);

        return CommentResponse.builder()
                .comment(comment.getContent())
                .postId(comment.getPost().getId())
                .createdAt(dateTimeService.format(comment.getCreatedAt()))
                .username(comment.getUser().getUsername())
                .build();
    }


    @Override
    public CommentResponse updateComment(Long id, CommentRequest commentRequest) {

        CommentEntity commentEntity=commentRepository.findById(id).orElseThrow(
                () -> new EOException(ErrorCodes.ENTITY_NOT_FOUND,
                        MessageCodes.ENTITY_NOT_FOUND, String.valueOf(id)));
        if (EbsSecurityUtils.checkUser(commentEntity.getUser().getUsername())) {
            commentEntity.setContent(commentRequest.getComment());
            commentEntity.setUpdatedAt(Instant.now());
            commentEntity.setCreatedAt(Instant.now());
            commentRepository.save(commentEntity);
        }else {
            throw new EOException(CommonStatus.FORBIDDEN);
        }
        return CommentResponse.builder()
                .comment(commentEntity.getContent())
                .postId(commentEntity.getPost().getId())
                .createdAt(dateTimeService.format(commentEntity.getCreatedAt()))
                .username(commentEntity.getUser().getUsername())
                .build();
        }

    @Override
    public String deleteComment(Long id) {
        CommentEntity commentEntity=commentRepository.findById(id).orElseThrow(
                () -> new EOException(ErrorCodes.ENTITY_NOT_FOUND,
                        MessageCodes.ENTITY_NOT_FOUND, String.valueOf(id)));
        if (EbsSecurityUtils.checkUser(commentEntity.getUser().getUsername())||
                EbsSecurityUtils.checkUser(commentEntity.getPost().getUser().getUsername())) {
            commentRepository.delete(commentEntity);
            return  MessageCodes.PROCESSED_SUCCESSFULLY ;
        }
      return  MessageCodes.FAILURE ;
    }


}
