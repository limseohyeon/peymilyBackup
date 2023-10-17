package com.example.backend.service;

import com.example.backend.dto.SharedPetGalleryRequest;
import com.example.backend.entity.SharedPetGallery;
import com.example.backend.entity.User;
import com.example.backend.repository.SharedPetGalleryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SharedPetGalleryService {
    @Autowired
    private final SharedPetGalleryRepository sharedPetGalleryRepository;
    @Autowired
    private final UserService userService;
    @Autowired
    private final PetService petService;

    //게시글 Id로 불러오기
    public SharedPetGallery findPostById(Long photoId) {
        return sharedPetGalleryRepository.findPostById(photoId);
    }
    //사용자 Email 로 불러오기
    public List<SharedPetGallery> findAllPostByEmail(String email) {
        return sharedPetGalleryRepository.findPostByEmail(email);
    }
    //petId로 불러오기
    public List<SharedPetGallery> findAllPostByPetId(Long petId) {
        return sharedPetGalleryRepository.findPostByPetId(petId);
    }
    //petId && Email 로 불러오기
    public List<SharedPetGallery> findAllPostByUserAndPetId( String email,Long petId) {
        return sharedPetGalleryRepository.findPostsByUserAndPetId(email,petId);
    }

    //게시글 저장하기
    public SharedPetGallery savePhoto(SharedPetGalleryRequest sharedPetGalleryRequest, String email, Long petId) {
        Optional<User> optionalUser = userService.findByEmail(email);

        if (optionalUser.isPresent()) {
            SharedPetGallery sharedPetGallery = SharedPetGallery.builder()
                    .email(email)
                    .likes(sharedPetGalleryRequest.getLikes())
                    .title(sharedPetGalleryRequest.getTitle())
                    .wrote(sharedPetGalleryRequest.getWrote())
                    .date(sharedPetGalleryRequest.getDate())
                    .petId(sharedPetGalleryRequest.getPetId())
                    .build();

            return sharedPetGalleryRepository.save(sharedPetGallery);
        } else {
            throw new UsernameNotFoundException("Invalid SharedPetGallery request");
        }
    }
//게시글 수정
    public SharedPetGallery updateSharedPetGallery(SharedPetGalleryRequest sharedPetGalleryRequest) {
        SharedPetGallery sharedPetGallery = sharedPetGalleryRepository.findPostById(sharedPetGalleryRequest.getPhotoId());

        sharedPetGallery.setLikes(sharedPetGalleryRequest.getLikes());
        sharedPetGallery.setTitle(sharedPetGalleryRequest.getTitle());
        sharedPetGallery.setWrote(sharedPetGalleryRequest.getWrote());

        return sharedPetGalleryRepository.save(sharedPetGallery);
    }
//특정 게시글 삭제
    public SharedPetGallery deleteSharedPetGallery(Long photoId) {
        SharedPetGallery sharedPetGallery = findPostById(photoId);
        sharedPetGalleryRepository.deleteByPhotoId(photoId);
        return sharedPetGallery;
    }
    //사용자에 속한 모든 게시글 삭제 - (계정 탈퇴)
    public List<SharedPetGallery> deleteSharedPetGalleryByEmail(String email) {
        List<SharedPetGallery> allSharedPetGallery = findAllPostByEmail(email);
        sharedPetGalleryRepository.deleteByEmail(email);
        return allSharedPetGallery;
    }
    //펫에 속한 모든 게시글 삭제 - (펫 계정 지우기)
    public List<SharedPetGallery> deleteSharedPetGalleryByPetId(Long petId) {
        List<SharedPetGallery> allSharedPetGallery = findAllPostByPetId(petId);
        sharedPetGalleryRepository.deleteByPetId(petId);
        return allSharedPetGallery;
    }
    //펫&&사용자에 속한 게시글 삭제 - (양육자 삭제)
    public List<SharedPetGallery> deleteSharedPetGalleryByPetIdAndEmail( String email, Long petId) {
        List<SharedPetGallery> allSharedPetGallery = findAllPostByUserAndPetId(email, petId);
        sharedPetGalleryRepository.deletePostsByUserAndPetId(email,petId);
        return allSharedPetGallery;
    }
}
