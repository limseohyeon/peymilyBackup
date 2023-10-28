package com.example.backend.controller;

import com.example.backend.dto.CommentRequest;
import com.example.backend.dto.SharedPetGalleryCommentRequest;
import com.example.backend.entity.Comment;
import com.example.backend.entity.Community;
import com.example.backend.entity.SharedPetGalleryComment;
import com.example.backend.service.CommentService;
import com.example.backend.service.SharedPetGalleryCommentService;
import com.example.backend.service.SharedPetGalleryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping(value = "/GalleryComment", produces = MediaType.APPLICATION_JSON_VALUE)
public class SharedPetGalleryCommentController {
    @Autowired
    private SharedPetGalleryCommentService sharedPetGalleryCommentService;

    //게시글 등록
    @PostMapping("/post/{email}")
    public ResponseEntity<SharedPetGalleryComment> PostComment(@RequestBody @Valid SharedPetGalleryCommentRequest sharedPetGalleryCommentRequest,
                                                               @PathVariable("email") String email
                                                              ) {
        try {
            SharedPetGalleryComment newSharedPetGalleryComment = sharedPetGalleryCommentService.saveComment(sharedPetGalleryCommentRequest);

            return ResponseEntity.ok(newSharedPetGalleryComment);
        } catch (Exception e) {
            System.out.println("unable to save comment. Error : " + e);

            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
//특정 댓글 가져오기
    @GetMapping("/get/{commentId}")
    public ResponseEntity<SharedPetGalleryComment> getComment(@PathVariable("commentId") Long commentId) {
        return ResponseEntity.ok(sharedPetGalleryCommentService.findCommentById(commentId));
    }
//모든 댓글 가져오기
    @GetMapping("/getAll/{photoId}")
    public ResponseEntity<List<SharedPetGalleryComment>> getComments(@PathVariable("photoId") Long photoId) {
        List<SharedPetGalleryComment> allComments = sharedPetGalleryCommentService.findAllCommentByPhotoId(photoId);
        if (allComments.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(allComments);
    }
    //특정 댓글 지우기
    @DeleteMapping("/delete/{commentId}")
    public ResponseEntity<SharedPetGalleryComment> DeleteComment(@PathVariable("commentId") Long commentId) {
        SharedPetGalleryComment commentToDelete = sharedPetGalleryCommentService.findCommentById(commentId);
        sharedPetGalleryCommentService.deleteCommentById(commentId);

        return ResponseEntity.ok(commentToDelete);
    }
    //게시글 기준 댓글 지우기 (게시글 삭제)
    @DeleteMapping("/delete/{photoId}")
    public ResponseEntity<List<SharedPetGalleryComment>> DeleteCommentByPhotoId(@PathVariable("photoId") Long photoId) {
        List<SharedPetGalleryComment> commentToDelete = sharedPetGalleryCommentService.findAllCommentByPhotoId(photoId);
        sharedPetGalleryCommentService.deleteCommentByPhotoId(photoId);
        return ResponseEntity.ok(commentToDelete);
    }
    //사용자 기준 댓글 지우기 (계정 탈퇴)
    @DeleteMapping("/delete/{email}")
    public ResponseEntity<List<SharedPetGalleryComment>> DeleteCommentByEmail(@PathVariable("email") String email) {
        List<SharedPetGalleryComment> commentToDelete = sharedPetGalleryCommentService.findAllCommentByEmail(email);
        sharedPetGalleryCommentService.deleteCommentByEmail(email);
        return ResponseEntity.ok(commentToDelete);
    }
    //양육자 기준 댓글 지우기 (양육자 삭제)
    @DeleteMapping("/delete/{email}/{petId}")
    public ResponseEntity<List<SharedPetGalleryComment>> DeleteCommentByEmailAndPetId(@PathVariable("email") String email,
                                                                                      @PathVariable("petId") Long petId) {
        List<SharedPetGalleryComment> commentToDelete = sharedPetGalleryCommentService.findAllCommentByEmailAndPetId(email, petId);
        sharedPetGalleryCommentService.deleteCommentByEmailAndPetId(email, petId);
        return ResponseEntity.ok(commentToDelete);
    }
    //펫 기준 댓글 지우기 (펫 계정 삭제)
    @DeleteMapping("/delete/{petId}")
    public ResponseEntity<List<SharedPetGalleryComment>> DeleteCommentByPetId(@PathVariable("petId") Long petId) {
        List<SharedPetGalleryComment> commentToDelete = sharedPetGalleryCommentService.findAllCommentByPetId(petId);
        sharedPetGalleryCommentService.deleteCommentByPetId(petId);
        return ResponseEntity.ok(commentToDelete);
    }

}
