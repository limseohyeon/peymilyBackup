package com.example.backend.service;

import com.example.backend.entity.Comment;
import com.example.backend.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    @Autowired
    private final CommentRepository commentRepository;

    public Comment findCommentById(Long commentId) { return commentRepository.findCommentByCommentId(commentId); }

    // 동일 커뮤니티에서 모든 댓글을 불러오기 위해 필요
    public List<Comment> findAllCommentByPostType(Long postType) { return commentRepository.findAllCommentByPostType(postType); }


}
