package com.example.backend.controller;

import com.example.backend.dto.CommentRequest;
import com.example.backend.entity.Comment;
import com.example.backend.entity.Invitation;
import com.example.backend.repository.CommentRepository;
import com.example.backend.service.CommentService;
import com.example.backend.service.CommunityService;
import org.hibernate.service.spi.InjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/comment", produces = MediaType.APPLICATION_JSON_VALUE)
public class CommentController {
    @Autowired
    private CommentService commentService;
    @Autowired
    private CommentRepository commentRepository;

    @PostMapping("/post/{email}")
    public ResponseEntity<Comment> PostComment(@RequestBody @Valid CommentRequest commentRequest,
                                               @PathVariable("email") String email) {
        try {
            Comment newComment = commentService.saveComment(commentRequest);

            return ResponseEntity.ok(newComment);
        } catch (Exception e) {
            System.out.println("unable to save comment. Error : " + e);

            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get/{commentId}")
    public ResponseEntity<Comment> getComment(@PathVariable("commentId") Long commentId) {
        return ResponseEntity.ok(commentService.findCommentById(commentId));
    }

    @GetMapping("/getAll/{communityId}")
    public ResponseEntity<List<Comment>> getComments(@PathVariable("communityId") Long communityId) {
        List<Comment> allComments = commentService.findAllCommentByCommunityId(communityId);

        if (allComments.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(allComments);
    }

    @DeleteMapping("/delete/{commentId}")
    public ResponseEntity<Void> deleteCommentById(@PathVariable("commentId") Long commentId) {
        try {
            commentRepository.deleteById(commentId);
            return ResponseEntity.noContent().build(); // 삭제 성공 시 204 No Content 반환
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build(); // 댓글을 찾을 수 없을 때 404 Not Found 반환
        }
    }
    @DeleteMapping("/delete/{communityId}")
    public ResponseEntity<Void> deleteCommentByCommunityId(@PathVariable("communityId") Long communityId) {
        try {
            commentService.deleteAllCommentByCommunityId(communityId);
            return ResponseEntity.noContent().build(); // 삭제 성공 시 204 No Content 반환
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build(); // 댓글을 찾을 수 없을 때 404 Not Found 반환
        }
    }


}