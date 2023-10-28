package com.example.backend.controller;

import com.example.backend.entity.User;
import com.example.backend.repository.UserRepository;
import com.example.backend.util.FileUploadUtil;
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

import javax.imageio.ImageIO;
import javax.transaction.Transactional;
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
@RequestMapping("/shared-images/{petId}")
public class  SharedPetImageController {

    @Autowired
    UserRepository userRepository;

    @PostMapping("/uploadImage/{email}/{sharedPetId}")
    public ResponseEntity<String> uploadImage(@PathVariable("petId") Long petId,
                                              @PathVariable("email") String email,
                                              @PathVariable("sharedPetId") Long sharedPetId,
                                              @RequestParam("file") MultipartFile file) throws IOException {
        // 파일 이름에서 확장자 추출
        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
        String fileName = sharedPetId.toString() + ".jpg"; // petId를 기준으로 파일 이름을 정함
        String uploadDir = "shared-images/" + petId + "/";

        // 이미지 리사이징
        BufferedImage originalImage = ImageIO.read(file.getInputStream());
        BufferedImage resizedImage = Thumbnails.of(originalImage)
                .size(300, 300) // 원하는 크기로 조절
                .asBufferedImage();

        FileUploadUtil.saveImage(uploadDir, fileName, resizedImage);

        return new ResponseEntity<>("Image uploaded successfully in " + uploadDir, HttpStatus.OK);
    }

    @GetMapping("/getAllImages")
    public List<String> getAllImages(@PathVariable("petId") Long petId) throws IOException {
        String baseDir = "shared-images/" + petId;
        File fileDir = new File(baseDir);
        List<String> imageUrls = new ArrayList<>();

        if (fileDir.exists() && fileDir.isDirectory()) {
            File[] imageFiles = fileDir.listFiles((dir, name) ->
                    name.toLowerCase().endsWith(".jpg") ||
                            name.toLowerCase().endsWith(".jpeg") ||
                            name.toLowerCase().endsWith(".png")
            );

            for (File imageFile : imageFiles) {
                String constUrl = "http://43.200.8.47:8080/";
                String imageUrl = constUrl + "shared-images/" + petId + "/" + imageFile.getName();
                imageUrls.add(imageUrl);
            }
        }

        System.out.println("Images found : " + imageUrls);

        return imageUrls;
    }

    @GetMapping("/{imageName:.+}") // 이미지 확장자를 포함하기 위해 ".+"를 추가
    public ResponseEntity<Resource> downloadImage(@PathVariable("petId") Long petId,
                                                  @PathVariable String imageName) throws IOException {
        String downloadDir = "shared-images";

        //Optional<User> user = userRepository.findByEmail(email);
        //String inviter = user.get().getInviter();

        // 유저 정보를 기반으로 다운로드 디렉토리 지정
        String sharedDownloadDir = downloadDir + "/" + petId + "/";
        Path path = Paths.get(sharedDownloadDir, imageName);
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

    @PutMapping("/updateImage/{sharedPetId}")
    public ResponseEntity<String> updateImage(@PathVariable("petId") Long petId,
                                              @PathVariable("sharedPetId") Long sharedPetId,
                                              @RequestParam("file") MultipartFile file) throws IOException {
//        Optional<User> user = userRepository.findByEmail(email);
//        String inviter = user.get().getInviter();

        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
        String fileName = sharedPetId.toString() + StringUtils.getFilenameExtension(originalFilename);
        String uploadDir = "shared-images/";
        String userUploadDir = uploadDir + petId + "/";

        BufferedImage originalImage = ImageIO.read(file.getInputStream());
        BufferedImage resizedImage = Thumbnails.of(originalImage)
                .size(300, 300) // 원하는 크기로 조절
                //.rotate(90)
                .asBufferedImage();

        FileUploadUtil.saveImage(userUploadDir, fileName, resizedImage);

        return new ResponseEntity<>("Image updated successfully", HttpStatus.OK);
    }

    @DeleteMapping("/deleteImage/{email}/{sharedPetId}")
    @Transactional
    public ResponseEntity<String> deleteImage(@PathVariable("petId") Long petId,
                                              @PathVariable("email") String email,
                                              @PathVariable("sharedPetId") String sharedPetId) throws IOException {
        String fileName = sharedPetId;
        String uploadDir = "shared-images";

        //Optional<User> user = userRepository.findByEmail(email);
        //String inviter = user.get().getInviter();

        String userUploadDir = uploadDir + "/" + petId + "/";
        Path filePath = Paths.get(userUploadDir, fileName);

        if (Files.exists(filePath)) {
            Files.delete(filePath);
            return new ResponseEntity<>("Image deleted successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Image not found", HttpStatus.NOT_FOUND);
        }
    }
}
