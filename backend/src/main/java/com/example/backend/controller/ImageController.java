package com.example.backend.controller;

import com.example.backend.entity.User;
import com.example.backend.repository.UserRepository;
import com.example.backend.util.FileUploadUtil;
import com.example.backend.util.ImageUtil;
import net.coobird.thumbnailator.Thumbnails;
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

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@RestController
@RequestMapping(value = "/profile", produces = MediaType.APPLICATION_JSON_VALUE)
public class ImageController {

    @Autowired
    UserRepository userRepository;

//    @PostMapping("/uploadImage")
//    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) throws IOException {
//        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
//        String uploadDir = "image-uploads/";
//        FileUploadUtil.saveFile(uploadDir, fileName, file);
//
//        return new ResponseEntity<>("Image uploaded successfully", HttpStatus.OK);
//    }
//
//    @GetMapping("/downloadImage/{imageName:.+}")
//    public ResponseEntity<Resource> downloadImage(@PathVariable String imageName) throws IOException {
//        String downloadDir = "image-uploads/";
//        Path path = Paths.get(downloadDir, imageName);
//        Resource resource = new UrlResource(path.toUri());
//
//        if (!resource.exists() || !resource.isReadable()) {
//            throw new IOException("Failed to read image: " + imageName);
//        }
//
//        String contentType = Files.probeContentType(path);
//        if (contentType == null) {
//            contentType = "application/octet-stream";
//        }
//
//        return ResponseEntity
//                .ok()
//                .contentType(MediaType.parseMediaType(contentType))
//                .body(resource);
//    }

    @PostMapping("/post/{email}")
    public ResponseEntity<String> uploadProfile(@RequestParam("file") MultipartFile file,
                                              @PathVariable("email") String email) throws IOException {
        String tmpFile = StringUtils.cleanPath(file.getOriginalFilename());
        String uploadDir = "profile/" + email;
        String fileName = email.toString() + ".jpg";

        System.out.println("File name : " + fileName);

        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // 이미지 크기 조절
        BufferedImage originalImage = ImageIO.read(file.getInputStream());
        BufferedImage resizedImage = Thumbnails.of(originalImage)
                .size(300, 300) // 원하는 크기로 조절
                //.rotate(90)
                .asBufferedImage();

        FileUploadUtil.saveImage(uploadDir, fileName, resizedImage);

        return new ResponseEntity<>("Image uploaded successfully. Directory : " + uploadDir, HttpStatus.OK);
    }

    @GetMapping("/get/{email}/{imageName}")
    public ResponseEntity<Resource> getProfile(@PathVariable String email,
                                               @PathVariable String imageName) throws IOException {
        String downloadDir = "profile/" + email + "/";

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

    @PutMapping("/update/{email}")
    public ResponseEntity<String> updateProfile(@PathVariable("email") String email,
                                              @RequestParam("file") MultipartFile file) throws IOException {
        Optional<User> user = userRepository.findByEmail(email);

        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
        String fileName = email.toString() + "." + StringUtils.getFilenameExtension(originalFilename);
        String uploadDir = "profile/" + email;

        Path existingImagePath = Paths.get(uploadDir, fileName);
        Files.deleteIfExists(existingImagePath);

        FileUploadUtil.saveFile(uploadDir, fileName, file);

        return new ResponseEntity<>("Image updated successfully", HttpStatus.OK);
    }

    @DeleteMapping("/delete/{email}")
    public ResponseEntity<String> deleteImage(@PathVariable("email") String email) throws IOException {
        String fileName = email.toString() + ".jpg";
        String uploadDir = "profile/" + email;

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
