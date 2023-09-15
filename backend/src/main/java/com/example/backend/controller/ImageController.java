package com.example.backend.controller;

import com.example.backend.entity.User;
import com.example.backend.repository.UserRepository;
import com.example.backend.util.FileUploadUtil;
import com.example.backend.util.ImageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@RestController
public class ImageController {

    @Autowired
    UserRepository userRepository;

    @PostMapping("/uploadImage")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) throws IOException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        String uploadDir = "image-uploads/";
        FileUploadUtil.saveFile(uploadDir, fileName, file);

        return new ResponseEntity<>("Image uploaded successfully", HttpStatus.OK);
    }

    @GetMapping("/downloadImage/{imageName:.+}")
    public ResponseEntity<Resource> downloadImage(@PathVariable String imageName) throws IOException {
        String downloadDir = "image-uploads/";
        Path path = Paths.get(downloadDir, imageName);
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

    @PostMapping("/profile/post/{email}")
    public ResponseEntity<String> uplodadProfile(@PathVariable String email,
                                                 @RequestParam("file") MultipartFile file) throws IOException {
        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
        String fileName = email.toString() + ".jpg"; // petId를 기준으로 파일 이름을 정함
        String uploadDir = "profile/{email}";

        // 유저 정보를 기반으로 업로드 디렉토리 생성
        FileUploadUtil.saveFile(uploadDir, fileName, file);

        return new ResponseEntity<>("Image uploaded successfully", HttpStatus.OK);
    }

    @GetMapping("/profile/get/{email}")
    public ResponseEntity<Resource> getProfile(@PathVariable String email,
                                             @PathVariable String imageName) throws IOException {
        String downloadDir = "profile/{email}";

        Optional<User> user = userRepository.findByEmail(email);

        Path path = Paths.get(downloadDir, imageName);
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

    @PutMapping("profile/update/{email}")
    public ResponseEntity<String> updateProfile(@PathVariable("email") String email,
                                              @RequestParam("file") MultipartFile file) throws IOException {
        Optional<User> user = userRepository.findByEmail(email);

        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
        String fileName = email.toString() + StringUtils.getFilenameExtension(originalFilename);
        String uploadDir = "profile/{email}";
        FileUploadUtil.saveFile(uploadDir, fileName, file);

        return new ResponseEntity<>("Image updated successfully", HttpStatus.OK);
    }

    @DeleteMapping("/profile/delete/{email}")
    public ResponseEntity<String> deleteImage(@PathVariable("email") String email) throws IOException {
        String fileName = email;
        String uploadDir = "profile/{email}";

        Optional<User> user = userRepository.findByEmail(email);

        Path filePath = Paths.get(uploadDir, fileName);

        if (Files.exists(filePath)) {
            Files.delete(filePath);
            return new ResponseEntity<>("Image deleted successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Image not found", HttpStatus.NOT_FOUND);
        }
    }
}
