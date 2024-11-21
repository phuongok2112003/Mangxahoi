package com.example.Mangxahoi.controller;

import com.example.Mangxahoi.dto.response.ImageResponse;
import com.example.Mangxahoi.services.ImageService;
import com.example.Mangxahoi.utils.EOResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/image")
@Slf4j
public class ImageController {
    private final ImageService imageService;
    @PostMapping(value = "/upload-image",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public EOResponse<List<ImageResponse>> uploadImage(@RequestPart("files") MultipartFile[] files) {

        return EOResponse.build(imageService.uploadImage(files));
    }

    @PostMapping(value = "/upload-avatar",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public EOResponse<ImageResponse> uploadAvatar(@RequestPart("files") MultipartFile files) {

        return EOResponse.build(imageService.uploadAvatar(files));
    }
    @GetMapping("/{action}/{filename}")
    public ResponseEntity<byte[]> getImageAvatar(@PathVariable String action,@PathVariable String filename) throws IOException {
        byte[] imgBytes = imageService.getImage(action+"/"+filename);

        if (imgBytes != null) {

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(MediaType.IMAGE_JPEG_VALUE));
            headers.setContentDisposition(ContentDisposition.inline().filename(filename).build());

            return ResponseEntity.ok().headers(headers).body(imgBytes);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }



}
