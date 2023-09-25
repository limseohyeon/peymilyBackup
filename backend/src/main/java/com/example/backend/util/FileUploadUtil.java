package com.example.backend.util;

import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileUploadUtil {

    public static void saveFile(String uploadDir, String fileName, MultipartFile multipartFile) throws IOException {
        Path uploadPath = Paths.get(uploadDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        try (InputStream inputStream = multipartFile.getInputStream()) {
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new IOException("Could not save file: " + fileName, e);
        }
    }

    public static List<String> getFileNames(String directoryPath) {
        List<String> fileNames = new ArrayList<>();
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            return fileNames;
        }
        File[] fileList = directory.listFiles();
        if (fileList == null) {
            return fileNames;
        }
        Arrays.stream(fileList)
                .filter(File::isFile)
                .forEach(file -> fileNames.add(file.getName()));
        return fileNames;
    }

    public static void saveImage(String uploadDir, String fileName, BufferedImage image) throws IOException {
        // File 객체 생성
        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File targetFile = new File(uploadDir + File.separator + fileName);

        // 이미지를 파일로 저장
        ImageIO.write(image, "jpg", targetFile);
    }
}