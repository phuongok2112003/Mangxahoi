package com.example.Mangxahoi.services.Impl;
import com.example.Mangxahoi.constans.ErrorCodes;
import com.example.Mangxahoi.constans.MessageCodes;
import com.example.Mangxahoi.dto.request.PostRequest;
import com.example.Mangxahoi.dto.response.*;
import com.example.Mangxahoi.entity.ImageEntity;
import com.example.Mangxahoi.entity.PostEntity;
import com.example.Mangxahoi.entity.UserEntity;
import com.example.Mangxahoi.error.CommonStatus;
import com.example.Mangxahoi.exceptions.EOException;
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
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static com.example.Mangxahoi.constans.ErrorCodes.ENTITY_NOT_FOUND;

@Service
@AllArgsConstructor
@Slf4j
public class PostServiceImpl implements PostService {
    private final ImageService imageService;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final DateTimeService dateTimeService;
    private final ImageRepository imageRepository;

    @Override
    public PostResponse createPost(  PostRequest postRequest, MultipartFile[] files)  {

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
        List<ImageResponse> imageResponseList=new ArrayList<>();
        if(files!=null){
            imageResponseList = imageService.uploadImage(files);
            saveImage(imageEntities,imageResponseList,files,post);
        }
        post.setImages(imageEntities);
        postRepository.save(post);
        if(!imageEntities.isEmpty()){
            imageRepository.saveAll(imageEntities);
        }

        return PostMapper.entiyToResponse(post);
    }

    @Override
    public PostResponse updatePost(@NonNull Long id,@NonNull PostRequest postRequest, MultipartFile[] files) {

        PostEntity post=postRepository.findById(id).orElseThrow(
                () -> new EOException(ErrorCodes.ENTITY_NOT_FOUND, MessageCodes.ENTITY_NOT_FOUND, String.valueOf(id)));
        List<ImageEntity> imageEntities = new ArrayList<>();
        List<ImageResponse> imageResponseList=new ArrayList<>();
        if(files!=null){
            imageResponseList = imageService.uploadImage(files);
            postRequest.getImageRequest().getUrl().forEach(imageService::deleteImage);
            List<ImageEntity> image=imageRepository.findByUrlAll(postRequest.getImageRequest().getUrl());
//            List<ImageEntity> image=imageRepository.findByPost(post.getId());
            if(!image.isEmpty())
                imageRepository.deleteAll(image);
            saveImage(imageEntities,imageResponseList,files,post);
        }
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
                () -> new EOException(ErrorCodes.ENTITY_NOT_FOUND, MessageCodes.ENTITY_NOT_FOUND, String.valueOf(id)));

        return PostMapper.entiyToResponse(post);
    }

    @Override
    public List<PostResponse> getPostOfFriend() {
        UserEntity userEntity= SecurityUtils.getCurrentUser();
        List<PostEntity> list=postRepository.findPostOfFriend(userEntity.getId());
        if(list.isEmpty()){
            throw new EOException(ENTITY_NOT_FOUND,
                    MessageCodes.NOT_POST, String.valueOf(userEntity.getId()));
        }
        return list.stream().map(PostMapper::entiyToResponse).toList();
    }

    public void saveImage(  List<ImageEntity> imageEntities, List<ImageResponse> imageResponseList,MultipartFile[] files,PostEntity post){

        for (ImageResponse imageResponse : imageResponseList) {
            ImageEntity imageEntity = ImageEntity.builder()
                    .createdAt(Instant.now())
                    .createdAt(Instant.now())
                    .url(imageResponse.getUrl())
                    .post(post)
                    .build();
            imageEntities.add(imageEntity);
        }
    }



}
