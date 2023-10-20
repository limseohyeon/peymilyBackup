package com.example.backend.repository;

import com.example.backend.entity.Community;
import com.example.backend.entity.Invitation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommunityRepository extends JpaRepository<Community, Long> {
    @Query("SELECT c FROM Community c WHERE c.communityId = ?1")
    Community findPostById(Long communityId);

    @Query("SELECT c FROM Community c WHERE c.email = ?1")
    Community findPostByEmail(String email);

    @Modifying
    @Query("DELETE FROM Community c WHERE c.communityId = :communityId")
    void deleteByCommunityId(@Param("communityId") Long communityId);

//    @Modifying
//    @Query("DELETE FROM Comment c WHERE c.commentId = :commentId")
//    int deleteByCommentId(@Param("commentId") Long commentId);

}
