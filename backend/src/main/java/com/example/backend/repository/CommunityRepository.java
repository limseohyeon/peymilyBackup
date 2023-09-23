package com.example.backend.repository;

import com.example.backend.entity.Community;
import com.example.backend.entity.Invitation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface CommunityRepository extends JpaRepository<Community, Long> {
    @Query("SELECT c FROM Community c WHERE c.communityId = ?1")
    Community findPostById(Long communityId);

    @Query("SELECT c FROM Community c WHERE c.email = ?1")
    Community findPostByEmail(String email);

    // @Query 어노테이션으로 DELETE 또는 UPDATE 쿼리를 실행할 때는 @Modifying 어노테이션도 함께 사용해야 합니다.
    // 이 어노테이션은 해당 쿼리가 데이터베이스를 수정하는 것임을 알려줍니다.
    @Modifying
    @Query("DELETE FROM Community c WHERE c.communityId = ?1")
    int deleteByCommunityId(Long communityId);
}
