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

    //게시글 Id로 불러오기
    public SharedPetGallery findPostById(Long photoId) {
        return sharedPetGalleryRepository.findPostById(photoId);
    }
    //사용자 Email 로 불러오기
    public SharedPetGallery findPostByEmail(String email) {
        return sharedPetGalleryRepository.findPostByEmail(email);
    }
    //모든 게시글 불러오기
    public List<SharedPetGallery> findAllPost() { return sharedPetGalleryRepository.findAll(); }
//게시글 저장하기
    public SharedPetGallery savePhoto(SharedPetGalleryRequest sharedPetGalleryRequest, String email) {
        Optional<User> optionalUser = userService.findByEmail(email);

        if (optionalUser.isPresent()) {
            SharedPetGallery sharedPetGallery = SharedPetGallery.builder()
                    .email(email)
                    .likes(sharedPetGalleryRequest.getLikes())
                    .title(sharedPetGalleryRequest.getTitle())
                    .wrote(sharedPetGalleryRequest.getWrote())
                    .date(sharedPetGalleryRequest.getDate())
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
//게시글 삭제
    public SharedPetGallery deleteSharedPetGallery(Long photoId) {
        SharedPetGallery sharedPetGallery = findPostById(photoId);

        sharedPetGalleryRepository.deleteByPhotoId(photoId);

        return sharedPetGallery;
    }
}
