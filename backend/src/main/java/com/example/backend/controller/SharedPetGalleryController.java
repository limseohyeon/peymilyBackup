package com.example.backend.controller;

import com.example.backend.dto.SharedPetGalleryRequest;
import com.example.backend.entity.SharedPetGallery;
import com.example.backend.service.SharedPetGalleryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/sharedPetGallery", produces = MediaType.APPLICATION_JSON_VALUE)
public class SharedPetGalleryController {

    @Autowired
    private SharedPetGalleryService sharedPetGalleryService;

    @PostMapping("/post/{email}")
    public ResponseEntity<SharedPetGallery> PostCommunity(@PathVariable("email") String email,
                                                          @RequestBody @Valid SharedPetGalleryRequest sharedPetGalleryRequest) {
        try {
            SharedPetGallery newPost = sharedPetGalleryService.savePhoto(sharedPetGalleryRequest, email);

            return ResponseEntity.ok(newPost);
        } catch(Exception e) {
            System.out.println("Can't save photo request. Error : " + e);
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/get/{photoId}")
    public ResponseEntity<SharedPetGallery> GetCommunity(@PathVariable("photoId") Long photoId) {
        SharedPetGallery sharedPetGalleryPosted = sharedPetGalleryService.findPostById(photoId);

        if (sharedPetGalleryPosted.equals(null)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(sharedPetGalleryPosted);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<SharedPetGallery>> GetAllSharedPetGallery() {
        List<SharedPetGallery> allOfSharedPetGalleryPosted = sharedPetGalleryService.findAllPost();

        if (allOfSharedPetGalleryPosted.size() == 0) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(allOfSharedPetGalleryPosted);
    }

    @PutMapping("/update")
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

    @PutMapping("/updateLikes/{email}")
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

    @DeleteMapping("/delete/{photoId}")
    public ResponseEntity<SharedPetGallery> DeleteSharedPetGallery(@PathVariable("photoId") Long photoId) {
        SharedPetGallery GalleryToDelete = sharedPetGalleryService.findPostById(photoId);

        sharedPetGalleryService.deleteSharedPetGallery(photoId);

        return ResponseEntity.ok(GalleryToDelete);
    }
}
