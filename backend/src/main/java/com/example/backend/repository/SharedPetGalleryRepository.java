package com.example.backend.repository;

import com.example.backend.entity.SharedPetGallery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface SharedPetGalleryRepository extends JpaRepository<SharedPetGallery, Long> {

    @Query("SELECT c FROM SharedPetGallery c WHERE c.photoId = ?1")
    SharedPetGallery findPostById(Long photoId);

    @Query("SELECT c FROM SharedPetGallery c WHERE c.email = ?1")
    SharedPetGallery findPostByEmail(String email);

    @Modifying
    @Query("DELETE FROM SharedPetGallery c WHERE c.photoId = ?1")
    int deleteByPhotoId(Long photoId);

    @Modifying
    @Query("DELETE FROM SharedPetGallery c WHERE c.email = ?1")
    int deleteByEmail(String email);
}
