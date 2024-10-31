package com.example.Mangxahoi.services.Impl;

import com.example.Mangxahoi.constans.MessageCodes;
import com.example.Mangxahoi.dto.request.ImageRequest;
import com.example.Mangxahoi.dto.response.ImageResponse;
import com.example.Mangxahoi.entity.UserEntity;
import com.example.Mangxahoi.error.CommonStatus;
import com.example.Mangxahoi.exceptions.EOException;
import com.example.Mangxahoi.repository.UserRepository;
import com.example.Mangxahoi.services.ImageService;
import com.example.Mangxahoi.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.example.Mangxahoi.constans.ErrorCodes.ERROR_CODE;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageServiceImpl implements ImageService {
    @Value("${file.upload-image-dir}")
    private String IMAGE_UPLOAD_DIR;
    private final UserRepository userRepository;


    @Override
    public List<ImageResponse> uploadImage(MultipartFile[] files) {
        List<ImageResponse> list = new ArrayList<>();
        for (MultipartFile file : files) {

            ImageResponse uploadFIleReponseDto = new ImageResponse();
            if (file.isEmpty()) {
                throw new EOException(ERROR_CODE,
                        MessageCodes.NOT_NULL, file.getOriginalFilename());
            }
            String contentType = file.getContentType();
            if (contentType == null || !(contentType.equals(MediaType.IMAGE_JPEG_VALUE) ||
                    contentType.equals(MediaType.IMAGE_PNG_VALUE) ||
                    contentType.equals(MediaType.IMAGE_GIF_VALUE))) {
                throw new EOException(ERROR_CODE,
                        MessageCodes.FILE_UPLOAD_NOT_FORMAT, file.getOriginalFilename());
            }
            try {
                File directory = new File(IMAGE_UPLOAD_DIR + "/post-image/");
                if (!directory.exists()) {
                    directory.mkdirs();
                }

                String fileNameImage = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
                String filename = isValidation(fileNameImage, IMAGE_UPLOAD_DIR + "/post-image/");
                Path path = Paths.get(IMAGE_UPLOAD_DIR + "/post-image/" + filename);
                Files.write(path, file.getBytes());
                uploadFIleReponseDto.setUrl("/post-image/" + filename);
                list.add(uploadFIleReponseDto);

            } catch (IOException e) {
                throw new EOException(ERROR_CODE, e.getMessage(), file.getName());
            }
        }

        return list;
    }

    public String isValidation(String fileNameImage, String aHeadFile) {
        int count = 1;
        Path path = Paths.get(aHeadFile + fileNameImage);
        String extension = fileNameImage.contains(".")
                ? fileNameImage.substring(fileNameImage.lastIndexOf("."))
                : "";
        String baseName = fileNameImage.replace(extension, "");
        String filename = fileNameImage;
        while (Files.exists(path)) {
            filename = baseName + "(" + count + ")" + extension;
            path = Paths.get(aHeadFile + filename);
            count++;
        }
        return filename;
    }

    @Override
    public byte[] getImage(String filename) {
        try {
            Path path = Paths.get(IMAGE_UPLOAD_DIR + filename);
            byte[] image = Files.readAllBytes(path);
            return image;
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public String deleteImage(String filename) {
        try {
            Path path = Paths.get(IMAGE_UPLOAD_DIR + filename);
            Files.deleteIfExists(path);
            return MessageCodes.PROCESSED_SUCCESSFULLY;
        } catch (IOException e) {
            return e.getMessage();
        }
    }

    @Override
    public List<ImageResponse> updateImage(ImageRequest imageCurr, MultipartFile[] files) {
        for (String img : imageCurr.getUrl()) {
            deleteImage(img);
        }
        return uploadImage(files);
    }

    @Override
    public ImageResponse uploadAvatar(MultipartFile file) {
        ImageResponse uploadFIleReponseDto = new ImageResponse();
        if (file.isEmpty()) {
            throw new EOException(ERROR_CODE,
                    MessageCodes.NOT_NULL, file.getOriginalFilename());
        }
        String contentType = file.getContentType();
        if (contentType == null || !(contentType.equals(MediaType.IMAGE_JPEG_VALUE) ||
                contentType.equals(MediaType.IMAGE_PNG_VALUE) ||
                contentType.equals(MediaType.IMAGE_GIF_VALUE))) {
            throw new EOException(ERROR_CODE,
                    MessageCodes.FILE_UPLOAD_NOT_FORMAT, file.getOriginalFilename());
        }
        try {
            File directory = new File(IMAGE_UPLOAD_DIR + "/avatar-image/");
            if (!directory.exists()) {
                directory.mkdirs();
            }
            String fileNameImage = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
            String filename = isValidation(fileNameImage, IMAGE_UPLOAD_DIR + "/avatar-image/");
            Path path = Paths.get(IMAGE_UPLOAD_DIR + "/avatar-image/" + filename);

            uploadFIleReponseDto.setUrl("/avatar-image/" + filename);
            String email = SecurityUtils.getEmail();
            UserEntity userEntity = userRepository.findByEmail(email);

            if (null == userEntity) {
                throw new EOException(CommonStatus.ACCOUNT_NOT_FOUND);
            } else {
                if (!userEntity.getAvatarUrl().isEmpty())
                    deleteImage(userEntity.getAvatarUrl());
                Files.write(path, file.getBytes());


                userEntity.setAvatarUrl(uploadFIleReponseDto.getUrl());
                userRepository.save(userEntity);
            }


        } catch (IOException e) {
            throw new EOException(ERROR_CODE,
                    e.getMessage(), file.getName());
        }
        return uploadFIleReponseDto;
    }
}
