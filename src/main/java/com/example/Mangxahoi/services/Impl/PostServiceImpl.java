package com.example.Mangxahoi.services.Impl;
import com.example.Mangxahoi.constans.ErrorCodes;
import com.example.Mangxahoi.constans.MessageCodes;
import com.example.Mangxahoi.constans.enums.PostStatus;
import com.example.Mangxahoi.dto.request.ImageRequest;
import com.example.Mangxahoi.dto.request.PostRequest;
import com.example.Mangxahoi.dto.response.*;
import com.example.Mangxahoi.entity.CommentEntity;
import com.example.Mangxahoi.entity.ImageEntity;
import com.example.Mangxahoi.entity.PostEntity;
import com.example.Mangxahoi.entity.UserEntity;
import com.example.Mangxahoi.error.CommonStatus;
import com.example.Mangxahoi.exceptions.EOException;
import com.example.Mangxahoi.exceptions.EntityNotFoundException;
import com.example.Mangxahoi.repository.ImageRepository;
import com.example.Mangxahoi.repository.PostRepository;
import com.example.Mangxahoi.repository.UserRepository;
import com.example.Mangxahoi.services.DateTimeService;
import com.example.Mangxahoi.services.ImageService;
import com.example.Mangxahoi.services.PostService;
import com.example.Mangxahoi.services.mapper.PostMapper;
import com.example.Mangxahoi.utils.SecurityUtils;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.example.Mangxahoi.constans.ErrorCodes.ENTITY_NOT_FOUND;
import static com.example.Mangxahoi.constans.ErrorCodes.ERROR_CODE;

@Service
@AllArgsConstructor
@Slf4j
public class PostServiceImpl implements PostService {
    private final ImageService imageService;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final ImageRepository imageRepository;

    @Override
    public PostResponse createPost(  PostRequest postRequest)  {
        if(postRequest.getImageRequest().getUrl().isEmpty() && postRequest.getContent().isEmpty()){
            throw new EOException(CommonStatus.CHECK_POST);
        }

        String email = SecurityUtils.getEmail();
        UserEntity userEntity = userRepository.findByEmail(email);
        if (null == userEntity) {
            throw new EOException(CommonStatus.ACCOUNT_NOT_FOUND);
        }
        PostEntity post = PostEntity.builder()
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .user(userEntity)
                .status(postRequest.getStatus())
                .content(postRequest.getContent())
                .build();
        List<ImageEntity> imageEntities = new ArrayList<>();

        saveImage(imageEntities,postRequest.getImageRequest(),post);

        post.setImages(imageEntities);
        postRepository.save(post);
        if(!imageEntities.isEmpty()){
            imageRepository.saveAll(imageEntities);
        }

        return PostMapper.entiyToResponse(post);
    }

    @Override
    public PostResponse updatePost(@NonNull Long id,@NonNull PostRequest postRequest) {
        if(postRequest.getImageRequest().getUrl().isEmpty() && postRequest.getContent().isEmpty()){
            throw new EOException(CommonStatus.CHECK_POST);
        }
        PostEntity post=postRepository.findById(id).orElseThrow(
                () ->  new EntityNotFoundException(PostEntity.class.getName(), "id", id.toString()));


        if(!SecurityUtils.checkUser(post.getUser().getUsername()))
                throw new EOException(CommonStatus.FORBIDDEN);

        List<ImageEntity> imageEntities = new ArrayList<>();


        saveImage(imageEntities,postRequest.getImageRequest(),post);

        post.setUpdatedAt(Instant.now());
        post.setContent(postRequest.getContent());
        post.setStatus(postRequest.getStatus());
        if(!imageEntities.isEmpty()){
            imageRepository.saveAll(imageEntities);
        }
        postRepository.save(post);

        return PostMapper.entiyToResponse(post);
    }

    @Override
    public PostResponse getPost(Long id) {
        PostEntity post=postRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(PostEntity.class.getName(), "id", id.toString()));

        if(!SecurityUtils.checkUser(post.getUser().getUsername())&&post.getStatus().equals(PostStatus.PRIVATE)){
            throw new EOException(CommonStatus.FORBIDDEN);
        }else {
            return PostMapper.entiyToResponse(post);
        }
    }

    @Override
    public PageResponse<PostResponse> getPostOfFriend(int page,int size) {
        UserEntity userEntity= SecurityUtils.getCurrentUser();
        Sort sort = Sort.by("createdAt").ascending();
        Pageable pageable= PageRequest.of(page-1,size,sort);
        Page<PostEntity> list=postRepository.findPostOfFriend(userEntity.getId(),pageable);
        if(list.isEmpty()){
            throw new EOException(ENTITY_NOT_FOUND,
                    MessageCodes.NOT_POST, String.valueOf(userEntity.getId()));
        }
        List<PostResponse>responseList= list.stream().map(PostMapper::entiyToResponse).toList();
        return PageResponse.<PostResponse>builder()
                .currentPage(page)
                .pageSize(list.getSize())
                .totalElements(list.getTotalElements())
                .content(responseList)
                .totalPages(list.getTotalPages())
                .build();
    }

    @Override
    public PageResponse<PostResponse> getPost(Long userId,int page, int size) {

        Sort sort = Sort.by("createdAt").ascending();
        Pageable pageable= PageRequest.of(page-1,size,sort);
        Page<PostEntity> list=null;
        if(Objects.equals(SecurityUtils.getCurrentUser().getId(), userId)){
           list=postRepository.findByUserId(userId,pageable);
        }
        else{
           list=postRepository.findByUserIdStatus(userId,pageable);
        }

        if(list.isEmpty()){
            throw new EOException(ENTITY_NOT_FOUND,
                    MessageCodes.NOT_POST, String.valueOf(userId));
        }
        List<PostResponse>responseList= list.stream().map(PostMapper::entiyToResponse).toList();
        return PageResponse.<PostResponse>builder()
                .currentPage(page)
                .pageSize(list.getSize())
                .totalElements(list.getTotalElements())
                .content(responseList)
                .totalPages(list.getTotalPages())
                .build();
    }

    @Override
    public String deletePost(Long id) {

        PostEntity post=postRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(PostEntity.class.getName(), "id", id.toString()));
        if (SecurityUtils.checkUser(post.getUser().getUsername())) {
            postRepository.delete(post);
            for(ImageEntity image : post.getImages()){

                imageService.deleteImage(image.getUrl());
            }
            return  MessageCodes.PROCESSED_SUCCESSFULLY ;
        }
        throw new EOException(CommonStatus.FORBIDDEN) ;

    }

    public void saveImage(List<ImageEntity> imageEntities, ImageRequest imageRequest, PostEntity post) {


        for (String url : imageRequest.getUrl()) {
            if (url == null ||
                    !(url.toLowerCase().endsWith(".jpeg") ||
                            url.toLowerCase().endsWith(".jpg") ||
                            url.toLowerCase().endsWith(".png") ||
                            url.toLowerCase().endsWith(".gif"))) {
                throw new EOException(ERROR_CODE, MessageCodes.FILE_UPLOAD_NOT_FORMAT, url);
            }


            ImageEntity imageEntity = ImageEntity.builder()
                    .createdAt(Instant.now())
                    .url(url)
                    .post(post)
                    .build();
            imageEntities.add(imageEntity);
        }
    }




}
