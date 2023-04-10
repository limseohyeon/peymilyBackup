package com.example.backend.service;

import com.example.backend.entity.Pet;
import com.example.backend.repository.PetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PetService {

    private final PetRepository petRepository;

    public void savePet(Pet pet) {
        petRepository.save(pet);
    }

    public List<Pet> getAllPets() {
        return petRepository.findAll();
    }

    public List<String> getAllPetNames() {
        return petRepository.findAllPetNames();
    }

    public Pet getPetByName(String petName) {
        return petRepository.findByPetName(petName);
    }

    public void deletePet(Long id) {
        petRepository.deleteById(id);
    }

    public void updatePet(Pet pet) {
        petRepository.save(pet);
    }

    public void uploadPetImage(String petName, MultipartFile file) throws IOException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        Path uploadDir = Paths.get("image-uploads", petName);
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }
        Path filePath = uploadDir.resolve(petName);
        Files.copy(file.getInputStream(), filePath);
    }

    public Resource downloadPetImage(String petName, String imageName) throws MalformedURLException {
        // 이미지 파일이 저장된 경로
        Path downloadDir = Paths.get("image-uploads", petName);
        // 펫 이미지 파일 경로
        Path imagePath = downloadDir.resolve(imageName);
        // 파일 리소스 생성
        Resource resource = new FileSystemResource(imagePath);
        // 파일이 존재하고 읽기 가능한지 확인
        if (!resource.exists() || !resource.isReadable()) {
            throw new RuntimeException("Failed to read image: " + imageName);
        }
        // 파일 리소스 리턴
        return resource;
    }
}
