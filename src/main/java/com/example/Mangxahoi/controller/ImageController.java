package com.example.Mangxahoi.controller;

import com.example.Mangxahoi.dto.response.ImageResponse;
import com.example.Mangxahoi.services.ImageService;
import com.example.Mangxahoi.utils.EOResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    @GetMapping("/{filename}")
    public ResponseEntity<byte[]> getImage(@PathVariable String filename) {
        byte[] imgBytes=imageService.getImage(filename);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("inline", filename);
        if(imgBytes!=null)
            return ResponseEntity.ok().headers(headers).body(imgBytes);
        else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }


}
