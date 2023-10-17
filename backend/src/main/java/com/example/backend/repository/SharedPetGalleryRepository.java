package com.example.backend.repository;

import com.example.backend.entity.SharedPetGallery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SharedPetGalleryRepository extends JpaRepository<SharedPetGallery, Long> {

    //특정 사진 가져오기
    @Query("SELECT c FROM SharedPetGallery c WHERE c.photoId = ?1")
    SharedPetGallery findPostById(Long photoId);

    //해당 펫에 속한 모든 사진 가져오기
    @Query("SELECT c FROM SharedPetGallery c WHERE c.petId = ?1")
   List<SharedPetGallery> findPostByPetId(Long petId);

    //해당 사용자에 속한 모든 사진 가져오기
    @Query("SELECT c FROM SharedPetGallery c WHERE c.email = ?1")
    List<SharedPetGallery> findPostByEmail(String email);

    // 사용자와 펫에 속한 모든 사진 가져오기
    @Query("SELECT c FROM SharedPetGallery c WHERE c.email = ?1 AND c.petId = ?2")
    List<SharedPetGallery> findPostsByUserAndPetId(String email, Long petId);



    //특정 사진 지우기
    @Modifying
    @Query("DELETE FROM SharedPetGallery c WHERE c.photoId = ?1")
    int deleteByPhotoId(Long photoId);

    //사용자에 속한 모든 사진 지우기(사용자 계정 탈퇴)
    @Modifying
    @Query("DELETE FROM SharedPetGallery c WHERE c.email = ?1")
    int deleteByEmail(String email);

    //사용자와 펫에 속한 모든 사진 지우기(양육자 삭제)
    @Modifying
    @Query("DELETE FROM SharedPetGallery c WHERE c.email = ?1 AND c.petId = ?2")
    int deletePostsByUserAndPetId(String email, Long petId);

    //해당 펫에 속한 모든 사진 지우기(펫 계정 지우기)
    @Modifying
    @Query("DELETE FROM SharedPetGallery c WHERE c.petId = ?1")
    int deleteByPetId(Long petId);


}
