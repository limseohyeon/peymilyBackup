package com.example.backend.controller;

import com.example.backend.util.FileUploadUtil;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/pet/{inviter}")
public class PetImageController {

    @PostMapping("/uploadImage")
    public ResponseEntity<String> uploadImage(@PathVariable("inviter") String inviter, @RequestParam("file") MultipartFile file) throws IOException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        String uploadDir = "image-uploads/";
        // 유저 정보를 기반으로 업로드 디렉토리 생성
        String userUploadDir = uploadDir + inviter + "/";
        FileUploadUtil.saveFile(userUploadDir, fileName, file);

        return new ResponseEntity<>("Image uploaded successfully", HttpStatus.OK);
    }

    @GetMapping("/downloadImage/{imageName:.+}")
    public ResponseEntity<Resource> downloadImage(@PathVariable("inviter") String inviter, @PathVariable String imageName) throws IOException {
        String downloadDir = "image-uploads/";
        // 유저 정보를 기반으로 다운로드 디렉토리 지정
        String userDownloadDir = downloadDir + "/" + inviter + "/";
        Path path = Paths.get(userDownloadDir, imageName);
        Resource resource = new UrlResource(path.toUri());

        if (!resource.exists() || !resource.isReadable()) {
            throw new IOException("Failed to read image: " + imageName);
        }

        String contentType = Files.probeContentType(path);
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity
                .ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(resource);
    }
}