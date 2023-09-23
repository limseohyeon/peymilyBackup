package com.example.backend.controller;

import com.example.backend.entity.User;
import com.example.backend.repository.UserRepository;
import com.example.backend.service.UserService;
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

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping(value = "/communityImage", produces = MediaType.APPLICATION_JSON_VALUE)
public class CommunityImageController {
    @Autowired
    UserService userService;

    @PostMapping("/uploadImage/{email}")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file,
                                              @PathVariable("email") String email) throws IOException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        String uploadDir = "communityImage/" + email;
        String filePath = uploadDir + "/" + fileName;

        // 이미지 크기 조절
        Image originalImage = ImageIO.read(file.getInputStream());
        Image resizedImage = originalImage.getScaledInstance(200, 200, Image.SCALE_SMOOTH);

        FileUploadUtil.saveFile(uploadDir, fileName, file);

        return new ResponseEntity<>("Image uploaded successfully", HttpStatus.OK);
    }

    @GetMapping("/getAllImages")
    public List<String> getAllImages() throws IOException {
        String baseDir = "communityImage";
        File fileDir = new File(baseDir);
        String[] fileNames = fileDir.list();
        List<String> imageUrls = new ArrayList<>();

        File[] emailDirectories = fileDir.listFiles(File::isDirectory);

        if (emailDirectories != null) {
            for (File emailDir : emailDirectories) {
                File[] imageFiles = emailDir.listFiles((dir, name) ->
                        name.toLowerCase().endsWith(".jpg") ||
                                name.toLowerCase().endsWith(".jpeg") ||
                                name.toLowerCase().endsWith(".png")
                );

                if (imageFiles != null) {
                    for (File imageFile : imageFiles) {
                        String constUrl = "http://43.200.8.47:8080/";
                        String imageUrl = constUrl + "communityImage/" + emailDir.getName() + "/" + imageFile.getName();
                        imageUrls.add(imageUrl);
                    }
                }
            }
        }

        System.out.println("Images found : " + imageUrls);

        return imageUrls;
    }

    @GetMapping("/{email}/{imageName:.+}")
    public ResponseEntity<Resource> downloadImage(@PathVariable("email") String email,
                                                  @PathVariable("imageName") String imageName) throws IOException {
        String downloadDir = "communityImage/" + email;
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

    @PutMapping("update/{email}")
    public ResponseEntity<String> updateImage(@PathVariable("email") String email,
                                              @RequestParam("file") MultipartFile file) throws IOException {
        //Optional<User> user = userService.findByEmail(email);

        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
        String fileName = email.toString() + "." + StringUtils.getFilenameExtension(originalFilename);
        String uploadDir = "communityImage/" + email;

        Path existingImagePath = Paths.get(uploadDir, fileName);
        Files.deleteIfExists(existingImagePath);

        FileUploadUtil.saveFile(uploadDir, fileName, file);

        return new ResponseEntity<>("Image updated successfully", HttpStatus.OK);
    }

    @DeleteMapping("/delete/{email}")
    public ResponseEntity<String> deleteImage(@PathVariable("email") String email) throws IOException {
        String fileName = email.toString() + ".jpg";    // 일단 하드코딩... 추후에 고치자
        String uploadDir = "communityImage/" + email;
        //Optional<User> user = userService.findByEmail(email);

        Path filePath = Paths.get(uploadDir, fileName);

        if (Files.exists(filePath)) {
            Files.delete(filePath);
            return new ResponseEntity<>("Image deleted successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Image not found", HttpStatus.NOT_FOUND);
        }
    }
}
