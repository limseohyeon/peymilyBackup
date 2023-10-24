package com.example.backend.controller;

import com.example.backend.dto.SharedPetGalleryRequest;
import com.example.backend.entity.SharedPetGallery;
import com.example.backend.repository.SharedPetGalleryRepository;
import com.example.backend.service.SharedPetGalleryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/sharedPetGallery/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
public class SharedPetGalleryController {

    @Autowired
    private SharedPetGalleryService sharedPetGalleryService;
    @Autowired
    private SharedPetGalleryRepository sharedPetGalleryRepository;

//사진 올리기
    @PostMapping("/post/{petId}")
    public ResponseEntity<SharedPetGallery> PostSharedPetGallery(@PathVariable("email") String email,
                                                                 @PathVariable("petId") Long petId,
                                                                 @RequestBody @Valid SharedPetGalleryRequest sharedPetGalleryRequest) {
        try {
            SharedPetGallery newPost = sharedPetGalleryService.savePhoto(sharedPetGalleryRequest, email, petId);
            return ResponseEntity.ok(newPost);
        } catch(Exception e) {
            System.out.println("Can't save photo request. Error : " + e);
            return ResponseEntity.notFound().build();
        }
    }
//해당 펫에 속한 모든 사진 가져오기
@GetMapping("/get/{petId}")
public ResponseEntity<List<SharedPetGallery>> GetAllSharedPetGalleryByPetId(@PathVariable("petId") Long petId) {
    List<SharedPetGallery> sharedAllPetGallery = sharedPetGalleryService.findAllPostByPetId(petId);

    if (sharedAllPetGallery.equals(null)) {
        return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(sharedAllPetGallery);
}
//특정 사진 가져오기
    @GetMapping("/get/{photoId}")
    public ResponseEntity<Optional<SharedPetGallery>> GetSharedPetGallery(@PathVariable("photoId") Long photoId) {
        Optional<SharedPetGallery> sharedPetGalleryPosted = sharedPetGalleryRepository.findById(photoId);

        if (sharedPetGalleryPosted.equals(null)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(sharedPetGalleryPosted);
    }
//특정 사진 수정하기
    @PutMapping("/update/{photoId}")
    public ResponseEntity<SharedPetGallery> UpdateSharedPetGallery(@Valid @RequestBody SharedPetGalleryRequest updatedGalleryData) {
        try {
            SharedPetGallery existingSharedPetGallery = sharedPetGalleryService.findPostById(updatedGalleryData.getPhotoId());

            if (existingSharedPetGallery.equals(null)) {
                System.out.println("수정할 게시글을 찾을 수 없습니다");
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            SharedPetGallery newCommunity = sharedPetGalleryService.updateSharedPetGallery(updatedGalleryData);
            System.out.println("게시글 수정 성공");

            return new ResponseEntity<>(newCommunity, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println("게시글 수정 실패");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
//좋아요 업데이트
    @PutMapping("/updateLikes/{petId}")
    public ResponseEntity<SharedPetGallery> UpdateLikes(@Valid @RequestBody SharedPetGalleryRequest updatedGalleryData,
                                                 @PathVariable("email") String email) {
        try {
            SharedPetGallery sharedPetGalleryFound = sharedPetGalleryService.findPostById(updatedGalleryData.getPhotoId());

            if (!sharedPetGalleryFound.getLikedBy().contains(email)) {
                sharedPetGalleryFound.getLikedBy().add(email);
                sharedPetGalleryFound.setLikes(sharedPetGalleryFound.getLikes() + 1);
            } else {
                sharedPetGalleryFound.getLikedBy().remove(email);
                sharedPetGalleryFound.setLikes(sharedPetGalleryFound.getLikes() - 1);
            }

            SharedPetGallery newSharedPetGallery = sharedPetGalleryService.updateSharedPetGallery(updatedGalleryData);
            System.out.println("좋아요 수정 성공");

            return new ResponseEntity<>(newSharedPetGallery, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println("좋아요 수정 실패");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
//특정 사진 삭제하기
    @DeleteMapping("/delete/{photoId}")
    public ResponseEntity<SharedPetGallery> DeleteSharedPetGallery(@PathVariable("photoId") Long photoId) {
        SharedPetGallery GalleryToDelete = sharedPetGalleryService.findPostById(photoId);

        sharedPetGalleryService.deleteSharedPetGallery(photoId);

        return ResponseEntity.ok(GalleryToDelete);
    }
    //사용자에 속한 모든 사진 삭제하기(계정 탈퇴)
    @DeleteMapping("/deleteByEmail")
    public ResponseEntity<List<SharedPetGallery>> DeleteSharedPetGalleryByEmail(@PathVariable("email") String email) {
        List<SharedPetGallery> GalleryToDelete = sharedPetGalleryService.findAllPostByEmail(email);
        sharedPetGalleryService.deleteSharedPetGalleryByEmail(email);

        return ResponseEntity.ok(GalleryToDelete);
    }
    //펫에 속한 모든 사진 삭제하기(펫 계정 삭제)
    @DeleteMapping("/deleteByPetId/{petId}")
    public ResponseEntity<List<SharedPetGallery>> DeleteSharedPetGalleryByPetId(@PathVariable("petId") Long petId) {
        List<SharedPetGallery> GalleryToDelete = sharedPetGalleryService.findAllPostByPetId(petId);
        sharedPetGalleryService.deleteSharedPetGalleryByPetId(petId);

        return ResponseEntity.ok(GalleryToDelete);
    }
    //사용자 && 펫에 속한 모든 사진 삭제하기(양육자 나가기)
    @DeleteMapping("/deleteByEmailAndPetId/{petId}")
        public ResponseEntity<List<SharedPetGallery>> DeleteSharedPetGalleryByEmailAndPetId(@PathVariable("email") String email,
                                                                                            @PathVariable("petId") Long petId) {
            List<SharedPetGallery> GalleryToDelete = sharedPetGalleryService.findAllPostByUserAndPetId(email,petId);
            sharedPetGalleryService.deleteSharedPetGalleryByPetIdAndEmail(email,petId);

            return ResponseEntity.ok(GalleryToDelete);}
}
