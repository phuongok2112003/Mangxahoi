package com.example.Mangxahoi.services.Impl;


import com.example.Mangxahoi.dto.request.PostRequest;
import com.example.Mangxahoi.dto.response.ImageResponse;
import com.example.Mangxahoi.dto.response.PostResponse;
import com.example.Mangxahoi.dto.response.UserResponseDto;
import com.example.Mangxahoi.entity.ImageEntity;
import com.example.Mangxahoi.entity.PostEntity;
import com.example.Mangxahoi.entity.UserEntity;
import com.example.Mangxahoi.error.CommonStatus;
import com.example.Mangxahoi.exceptions.EOException;
import com.example.Mangxahoi.repository.ImagRepository;
import com.example.Mangxahoi.repository.PostRepository;
import com.example.Mangxahoi.repository.UserRepository;
import com.example.Mangxahoi.services.DateTimeService;
import com.example.Mangxahoi.services.ImageService;
import com.example.Mangxahoi.services.PostService;
import com.example.Mangxahoi.utils.EbsSecurityUtils;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PostServiceImpl implements PostService {
    private final ImageService imageService;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final DateTimeService dateTimeService;
    private final ImagRepository imagRepository;

    @Override
    public PostResponse createPost(PostRequest postRequest, MultipartFile[] files) {
        String email = EbsSecurityUtils.getEmail();
        UserEntity userEntity = userRepository.findByEmail(email);
        if (null == userEntity) {
            throw new EOException(CommonStatus.ACCOUNT_NOT_FOUND);
        }
        PostEntity post = PostEntity.builder()
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .user(userEntity)
                .content(postRequest.getContent())
                .build();
        List<ImageResponse> imageResponseList = imageService.uploadImage(files);
        List<ImageEntity> imageEntities = new ArrayList<>();
        for (ImageResponse imageResponse : imageResponseList) {
            ImageEntity imageEntity = ImageEntity.builder()
                    .createdAt(Instant.now())
                    .createdAt(Instant.now())
                    .url(imageResponse.getUrl())
                    .post(post)
                    .build();
            imageEntities.add(imageEntity);
        }

        post.setImages(imageEntities);

        postRepository.save(post);
        imagRepository.saveAll(imageEntities);

        return PostResponse.builder()
                .createdAt(dateTimeService.format(post.getCreatedAt()))
                .id(post.getId())
                .content(post.getContent())
                .user(UserResponseDto.builder()
                        .id(userEntity.getId())
                        .gender(userEntity.getGender())
                        .username(userEntity.getUsername())
                        .email((userEntity.getEmail()))
                        .build())
                .images(imageResponseList)
                .build();
    }
}
