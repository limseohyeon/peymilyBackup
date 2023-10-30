package com.example.backend.service;

import com.example.backend.dto.CommentRequest;
import com.example.backend.dto.CommunityRequest;
import com.example.backend.entity.Comment;
import com.example.backend.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    @Autowired
    private final CommentRepository commentRepository;

    //댓글 저장
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

    // 해당 게시글에 속한 모든 댓글 불러오기
    public List<Comment> findAllCommentByCommunityId(Long communityId) {
        return commentRepository.findAllCommentByCommunityId(communityId);
    }
    // 특정 댓글 불러오기
    public Comment findAllCommentByCommentId(Long commentId) {
        return commentRepository.findCommentByCommentId(commentId);
    }
    // 사용자에 속한 모든 댓글 불러오기
    public List<Comment> findAllCommentByEmail(String email) {
        return commentRepository.findAllCommentByCommunityEmail(email);
    }

    // 댓글 내용 수정
    public Comment updateCommentByCommunityId(CommentRequest commentRequest, Long commentId) {
        Comment commentToChange = findCommentById(commentId);
        commentToChange.setCommentInfo(commentRequest.getCommentInfo());
        if (commentToChange.getCommentInfo().length() == 0) {
            System.out.println("내용을 입력하세요!");
            return null;
        }
        return commentRepository.save(commentToChange);
    }
    public void deleteCommentByCommentId(Long commentId) {
        Comment commentToDelete = commentRepository.findCommentByCommentId(commentId);

        if (commentToDelete != null) {
            commentRepository.deleteById(commentId);
        } else {
            throw new EntityNotFoundException("Comment not found"); // 댓글을 찾을 수 없을 때 예외 던지기
        }
    }

//    // 게시글에 속한 모든 댓글 삭제
//    public void deleteAllCommentByCommunityId(Long communityId) {
//        List<Comment> commentToDelete = findAllCommentByCommunityId(communityId);
//        commentRepository.deleteByCommunityId(communityId);
//    }
//    // 사용자에 속한 모든 댓글 삭제
//    public List<Comment> deleteAllCommentByEmail( String email) {
//        List<Comment> commentToDelete = findAllCommentByEmail(email);
//        commentRepository.deleteByEmail(email);
//        return commentToDelete;
//    }
}