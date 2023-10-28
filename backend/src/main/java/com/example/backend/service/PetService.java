package com.example.backend.service;

import com.example.backend.dto.PetRequest;
import com.example.backend.dto.UserRequest;
import com.example.backend.entity.Pet;
import com.example.backend.entity.User;
import com.example.backend.repository.PetRepository;
import com.example.backend.repository.UserRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PetService {

    @Autowired
    private final PetRepository petRepository;

    @Autowired
    private final UserService userService;

    public List<Pet> getAllPets() {
        return petRepository.findAll();
    }

    public List<String> getAllPetNames() {
        return petRepository.findAllPetNames();
    }

    public Optional<Pet> getPetByName(String petName) {
        return petRepository.findByPetName(petName);
    }

    public Optional<Pet> getPetById(Long petId){return petRepository.findById(petId);}

    public void deletePet(Long id) {
        petRepository.deleteById(id);
    }

    public void updatePet(Pet pet) {
        petRepository.save(pet);
    }

    public Pet savePet(PetRequest petRequest, String email) {
        Optional<User> optionalUser = userService.findByEmail(email);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            Pet pet = Pet.builder()
                    .user(user)
                    .petName(petRequest.getPetName())
                    .petCode(petRequest.getPetCode())
                    .petAge(petRequest.getPetAge())
                    .detailInfo(petRequest.getDetailInfo())
                    .inviter(petRequest.getInviter())
                    .gender(petRequest.getGender())
                    .build();

            return petRepository.save(pet);
        } else {
            throw new UsernameNotFoundException("Invalid username or password");
        }
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