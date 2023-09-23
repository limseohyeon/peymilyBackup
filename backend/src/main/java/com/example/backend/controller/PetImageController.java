package com.example.backend.controller;

import com.example.backend.entity.User;
import com.example.backend.repository.UserRepository;
import com.example.backend.util.FileUploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.Optional;

@RestController
@RequestMapping("/pet/{email}")
public class PetImageController {

    @Autowired
    UserRepository userRepository;

    @PostMapping("/uploadImage/{petId}")
    public ResponseEntity<String> uploadImage(@PathVariable("email") String email,
                                              @PathVariable("petId") Long petId,
                                              @RequestParam("file") MultipartFile file) throws IOException {
        // 파일 이름에서 확장자 추출
        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
        String fileName = petId.toString() + ".jpg"; // petId를 기준으로 파일 이름을 정함
        String uploadDir = "image-uploads/";

        Optional<User> user = userRepository.findByEmail(email);
        String inviter = user.get().getInviter();

        // 유저 정보를 기반으로 업로드 디렉토리 생성
        String userUploadDir = uploadDir + inviter + "/";
        FileUploadUtil.saveFile(userUploadDir, fileName, file);

        return new ResponseEntity<>("Image uploaded successfully", HttpStatus.OK);
    }

    @GetMapping("/downloadImage/{imageName:.+}") // 이미지 확장자를 포함하기 위해 ".+"를 추가
    public ResponseEntity<Resource> downloadImage(@PathVariable("email") String email,
                                                  @PathVariable String imageName) throws IOException {
        String downloadDir = "image-uploads/";

        Optional<User> user = userRepository.findByEmail(email);
        String inviter = user.get().getInviter();

        // 유저 정보를 기반으로 다운로드 디렉토리 지정
        String userDownloadDir = downloadDir + inviter + "/";
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

    @PutMapping("/updateImage/{petId}")
    public ResponseEntity<String> updateImage(@PathVariable("email") String email,
                                              @PathVariable("petId") Long petId,
                                              @RequestParam("file") MultipartFile file) throws IOException {
        Optional<User> user = userRepository.findByEmail(email);
        String inviter = user.get().getInviter();

        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
        String fileName = petId.toString() + StringUtils.getFilenameExtension(originalFilename);
        String uploadDir = "image-uploads/";
        String userUploadDir = uploadDir + inviter + "/";

        Path existingImagePath = Paths.get(userUploadDir, fileName);
        Files.deleteIfExists(existingImagePath);

        FileUploadUtil.saveFile(userUploadDir, fileName, file);

        return new ResponseEntity<>("Image updated successfully", HttpStatus.OK);
    }

    @DeleteMapping("/deleteImage/{petId}")
    public ResponseEntity<String> deleteImage(@PathVariable("email") String email,
                                              @PathVariable("petId") String petId) throws IOException {
        String fileName = petId;
        String uploadDir = "image-uploads/";

        Optional<User> user = userRepository.findByEmail(email);
        String inviter = user.get().getInviter();

        String userUploadDir = uploadDir + inviter + "/";
        Path filePath = Paths.get(userUploadDir, fileName);

        if (Files.exists(filePath)) {
            Files.delete(filePath);
            return new ResponseEntity<>("Image deleted successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Image not found", HttpStatus.NOT_FOUND);
        }
    }
}