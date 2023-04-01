package com.example.backend.controller;

import com.example.backend.util.FileUploadUtil;
import com.example.backend.util.ImageUtil;
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

@RestController
public class ImageController {

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

}
