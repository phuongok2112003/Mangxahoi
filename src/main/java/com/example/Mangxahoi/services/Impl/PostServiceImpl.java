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
import com.example.Mangxahoi.utils.EbsSecurityUtils;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
        List<ImageEntity> imageEntities = new ArrayList<>();
        List<ImageResponse> imageResponseList=new ArrayList<>();
        if(files!=null){
            imageResponseList = imageService.uploadImage(files);
            saveImage(imageEntities,imageResponseList,files,post);
        }
//        post.setImages(imageEntities);
        postRepository.save(post);
        if(!imageEntities.isEmpty()){
            imageRepository.saveAll(imageEntities);
        }

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
        if(!imageEntities.isEmpty()){
            imageRepository.saveAll(imageEntities);
        }
        postRepository.save(post);

        return PostResponse.builder()
                .createdAt(dateTimeService.format(post.getCreatedAt()))
                .id(post.getId())
                .content(post.getContent())
                .user(UserResponseDto.builder()
                        .id(post.getUser().getId())
                        .gender(post.getUser().getGender())
                        .username(post.getUser().getUsername())
                        .email((post.getUser().getEmail()))
                        .build())
                .images(imageResponseList)
                .build();
    }

    @Override
    public PostResponse getPost(Long id) {
        PostEntity post=postRepository.findById(id).orElseThrow(
                () -> new EOException(ErrorCodes.ENTITY_NOT_FOUND, MessageCodes.ENTITY_NOT_FOUND, String.valueOf(id)));
        List<ImageResponse> imageResponseList=post.getImages().stream().map(
                imageEntity ->
                    ImageResponse.builder()
                            .url(imageEntity.getUrl())
                            .build()
                ).toList();
        List<CommentResponse> commentResponseList=post.getComments().stream().map(commentEntity ->
                CommentResponse.builder()
                        .comment(commentEntity.getContent())
                        .postId(commentEntity.getPost().getId())
                        .createdAt(dateTimeService.format(commentEntity.getCreatedAt()))
                        .username(commentEntity.getUser().getUsername())
                        .build()
                ).toList();
        List<FavoriteResponse>favoriteResponses=post.getLikes().stream().map(favoriteEntity ->
                        FavoriteResponse.builder()
                                .id(favoriteEntity.getId())
                                .createAt(dateTimeService.format(favoriteEntity.getCreatedAt()))
                                .postId(favoriteEntity.getPost().getId())
                                .username(favoriteEntity.getUser().getUsername())
                                .build()
                ).toList();
        return PostResponse.builder()
                .createdAt(dateTimeService.format(post.getCreatedAt()))
                .id(post.getId())
                .content(post.getContent())
                .comments(commentResponseList)
                .user(UserResponseDto.builder()
                        .id(post.getUser().getId())
                        .gender(post.getUser().getGender())
                        .username(post.getUser().getUsername())
                        .email((post.getUser().getEmail()))

                        .build())
                .likes(favoriteResponses)
                .images(imageResponseList)
                .build();
    }

    @Override
    public List<PostResponse> getPostOfFriend(Long userId) {
        return null;
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
