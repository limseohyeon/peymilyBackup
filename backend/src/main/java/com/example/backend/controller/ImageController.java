package com.example.backend.controller;

import com.example.backend.util.FileUploadUtil;
import com.example.backend.util.ImageUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.apache.commons.io.FileUtils;
import com.example.backend.consts.ConstURL;

import java.io.File;
import java.io.IOException;

@RestController
public class ImageController {

    @PostMapping("/uploadImage")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) throws IOException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        String uploadDir = "image-uploads/";
        FileUploadUtil.saveFile(uploadDir, fileName, file);

        return new ResponseEntity<>("Image uploaded successfully", HttpStatus.OK);
    }

    // ':'는 해당 변수가 있다는 것을 나타내고, .+는 해당 변수가 확장자를 포함하는 것을 나타냅니다.
    // 즉, /{imageName:.+}는 URL 경로에서 imageName이라는 변수가 있으며, 확장자를 포함할 수 있습니다.
    @GetMapping("/downloadImage/{imageName:.+}")
    public ResponseEntity<byte[]> downloadImage(@PathVariable("imageName") String imageName) throws IOException {
        String downloadDir = "image-uploads/";
        File file = new File(downloadDir + imageName);
        byte[] bytes = FileUtils.readFileToByteArray(file);

        return ResponseEntity
                .ok()
                .contentType(MediaType.IMAGE_JPEG)
                .contentLength(file.length())
                .body(bytes);
    }

    @GetMapping("/showImage/{imageName:.+}")
    public ResponseEntity<byte[]> showImage(@PathVariable("imageName") String imageName) throws IOException {
        String imageUrl = "http://" + ConstURL.url + "/image-uploads/" + imageName;
        byte[] bytes = ImageUtil.readImageToByteArray(imageUrl);

        return ResponseEntity
                .ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(bytes);
    }
}
