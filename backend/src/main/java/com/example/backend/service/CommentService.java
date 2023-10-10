package com.example.backend.service;

import com.example.backend.dto.CommentRequest;
import com.example.backend.dto.CommunityRequest;
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

    public Comment saveComment(CommentRequest commentRequest) {
        Comment comment = Comment.builder()
                .communityId(commentRequest.getCommunityId())
                .email(commentRequest.getEmail())
                .commentInfo(commentRequest.getCommentInfo())
                .date(commentRequest.getDate())
                .build();

        return commentRepository.save(comment);
    }
    public Comment findCommentById(Long commentId) { return commentRepository.findCommentByCommentId(commentId); }

    // communityId를 바탕으로 커뮤니티 게시글의 댓글을 불러옴
    public List<Comment> findAllCommentByCommunityId(Long communityId) {
        return commentRepository.findAllCommentByCommunityId(communityId);
    }

    public Comment updateCommentByCommunityId(CommentRequest commentRequest, Long commentId) {
        Comment commentToChange = findCommentById(commentId);

        // 댓글 내용 수정
        commentToChange.setCommentInfo(commentRequest.getCommentInfo());

        if (commentToChange.getCommentInfo().length() == 0) {
            System.out.println("내용을 입력하세요!");

            return null;
        }

        return commentRepository.save(commentToChange);
    }
}