package com.example.backend.repository;

import com.example.backend.entity.Community;
import com.example.backend.entity.Invitation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CommunityRepository extends JpaRepository<Community, Long> {
    @Query("SELECT c FROM Community c WHERE c.communityId = ?1")
    Community findPostById(Long communityId);

    @Query("SELECT c FROM Community c WHERE c.email = ?1")
    Community findPostByEmail(String email);
}
