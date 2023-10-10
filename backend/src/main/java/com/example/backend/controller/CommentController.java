package com.example.backend.controller;

import com.example.backend.dto.CommentRequest;
import com.example.backend.entity.Comment;
import com.example.backend.entity.Invitation;
import com.example.backend.service.CommentService;
import org.hibernate.service.spi.InjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/comment", produces = MediaType.APPLICATION_JSON_VALUE)
public class CommentController {
    @Autowired
    private CommentService commentService;

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

    @GetMapping("/get/{communityId}/{commentId}")
    public ResponseEntity<Comment> getComment(@PathVariable("communityId") Long communityId,
                                              @PathVariable("commentId") String commentId) {
        return ResponseEntity.ok(commentService.findCommentById(commentId));
    }
}