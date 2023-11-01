package com.example.backend.controller;

import com.example.backend.dto.CommentRequest;
import com.example.backend.dto.SharedPetGalleryCommentRequest;
import com.example.backend.entity.Comment;
import com.example.backend.entity.Community;
import com.example.backend.entity.SharedPetGalleryComment;
import com.example.backend.repository.SharedPetGalleryCommentRepository;
import com.example.backend.service.CommentService;
import com.example.backend.service.SharedPetGalleryCommentService;
import com.example.backend.service.SharedPetGalleryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping(value = "/GalleryComment", produces = MediaType.APPLICATION_JSON_VALUE)
public class SharedPetGalleryCommentController {
    @Autowired
    private SharedPetGalleryCommentService sharedPetGalleryCommentService;
    @Autowired
    private SharedPetGalleryCommentRepository sharedPetGalleryCommentRepository;

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
    Optional<SharedPetGalleryComment> commentOptional = sharedPetGalleryCommentService.findCommentById(commentId);
    if (commentOptional.isPresent()) {
        SharedPetGalleryComment comment = commentOptional.get();
        return ResponseEntity.ok(comment);
    } else {
        // 원하는 처리를 수행하세요. (댓글을 찾지 못한 경우)
        return ResponseEntity.notFound().build();
    }
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
        Optional<SharedPetGalleryComment> commentToDelete = sharedPetGalleryCommentService.findCommentById(commentId);

        try {
            sharedPetGalleryCommentRepository.deleteById(commentId);
            return ResponseEntity.noContent().build(); // 삭제 성공 시 204 No Content 반환
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build(); // 댓글을 찾을 수 없을 때 404 Not Found 반환
        }

    }
    //게시글 기준 댓글 지우기 (게시글 삭제)
    @DeleteMapping("/deleteByPhotoId/{photoId}")
    public ResponseEntity<List<SharedPetGalleryComment>> DeleteCommentByPhotoId(@PathVariable("photoId") Long photoId) {
        List<SharedPetGalleryComment> commentToDelete = sharedPetGalleryCommentRepository.findAllCommentByPhotoId(photoId);
        if(!commentToDelete.isEmpty()){
            for (SharedPetGalleryComment comment : commentToDelete) {
                sharedPetGalleryCommentRepository.deleteById(comment.getCommentId());
            }
            return new ResponseEntity<>(HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }
    //사용자 기준 댓글 지우기 (계정 탈퇴)
    @DeleteMapping("/deleteByEmail/{email}")
    public ResponseEntity<List<SharedPetGalleryComment>> DeleteCommentByEmail(@PathVariable("email") String email) {
        List<SharedPetGalleryComment> commentToDelete = sharedPetGalleryCommentService.findAllCommentByEmail(email);
        sharedPetGalleryCommentService.deleteCommentByEmail(email);
        return ResponseEntity.ok(commentToDelete);
    }
    //양육자 기준 댓글 지우기 (양육자 삭제)
    @DeleteMapping("/deleteByOwner/{email}/{petId}")
    public ResponseEntity<List<SharedPetGalleryComment>> DeleteCommentByEmailAndPetId(@PathVariable("email") String email,
                                                                                      @PathVariable("petId") Long petId) {
        List<SharedPetGalleryComment> commentToDelete = sharedPetGalleryCommentRepository.findCommentByEmailAndPetId(email, petId);
        if(!commentToDelete.isEmpty()){
            for (SharedPetGalleryComment comment : commentToDelete) {
                sharedPetGalleryCommentRepository.deleteById(comment.getCommentId());
            }
            return new ResponseEntity<>(HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }
    //펫 기준 댓글 지우기 (펫 계정 삭제)
    @DeleteMapping("/deleteByPetId/{petId}")
    public ResponseEntity<List<SharedPetGalleryComment>> DeleteCommentByPetId(@PathVariable("petId") Long petId) {
        List<SharedPetGalleryComment> commentToDelete = sharedPetGalleryCommentRepository.findCommentByPetId(petId);
        if(!commentToDelete.isEmpty()){
            for (SharedPetGalleryComment comment : commentToDelete) {
                sharedPetGalleryCommentRepository.deleteById(comment.getCommentId());
            }
            return new ResponseEntity<>(HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

}
