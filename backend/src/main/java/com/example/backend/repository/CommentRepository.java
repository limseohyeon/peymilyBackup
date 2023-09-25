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
    @Query("SELECT c FROM Comment c WHERE c.commentId = ?1")
    Comment findCommentByCommentId(Long commentId);

    @Query("SELECT c FROM COMMENT WHERE c.communityId = ?1")
    List<Comment> findAllCommentByCommunityId(Long communityId);

    // @Query 어노테이션으로 DELETE 또는 UPDATE 쿼리를 실행할 때는 @Modifying 어노테이션도 함께 사용해야 합니다.
    // 이 어노테이션은 해당 쿼리가 데이터베이스를 수정하는 것임을 알려줍니다.
    @Modifying
    @Query("DELETE FROM Comment c WHERE c.commentId = ?1")
    int deleteByCommentId(Long commentId);
}