package com.example.backend.repository;

import com.example.backend.entity.Comment;
import com.example.backend.entity.Community;
import com.example.backend.entity.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    //특정 댓글 가져오기
    @Query("SELECT c FROM Comment c WHERE c.commentId = ?1")
     Comment findCommentByCommentId(Long commentId);
    //해당 게시글에 속한 모든 댓글 가져오기
    @Query("SELECT c FROM Comment c WHERE c.communityId = ?1")
    List<Comment> findAllCommentByCommunityId(Long communityId);
    //사용자 기준 댓글 가져오기
    @Query("SELECT c FROM Comment c WHERE c.communityId = ?1")
    List<Comment> findAllCommentByCommunityEmail(String email);

    //특정 댓글 삭제하기
    @Modifying
    @Query("DELETE FROM Comment c WHERE c.commentId = ?1")
    int deleteByCommentId(Long commentId);

    //게시글 삭제시 속한 모든 댓글 지우기
    @Modifying
    @Query("DELETE FROM Comment c WHERE c.communityId = ?1")
    List<Comment> deleteByCommunityId(Long communityId);

    //사용자 탈퇴시 모든 댓글 지우기
    @Modifying
    @Query("DELETE FROM Comment c WHERE c.email = ?1")
    List<Comment> deleteByEmail(String email);
}