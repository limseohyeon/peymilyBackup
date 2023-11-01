package com.example.backend.repository;

import com.example.backend.entity.Comment;
import com.example.backend.entity.SharedPetGalleryComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SharedPetGalleryCommentRepository  extends JpaRepository<SharedPetGalleryComment, Long> {



    //게시글 기준 댓글 가져오기
    @Query("SELECT c FROM SharedPetGalleryComment c WHERE c.photoId = ?1")
    List<SharedPetGalleryComment> findAllCommentByPhotoId(Long photoId);

    //petId 기준 댓글 가져오기(펫계정 삭제)
    @Query("SELECT c FROM SharedPetGalleryComment c WHERE c.petId = ?1")
    List<SharedPetGalleryComment> findCommentByPetId(Long petId);

    //Email 기준 댓글 가져오기(계정 탈퇴)
    @Query("SELECT c FROM SharedPetGalleryComment c WHERE c.email = ?1")
    List<SharedPetGalleryComment> findCommentByEmail(String email);

    //양육자 기준 댓글 가져오기(양육자 삭제)
    @Query("SELECT c FROM SharedPetGalleryComment c WHERE c.email = ?1 AND c.petId = ?2 ")
    List<SharedPetGalleryComment> findCommentByEmailAndPetId(String email,Long petId);



//    //게시글 삭제시 해당 게시글 모든 댓글 삭제
//    @Modifying
//    @Query("DELETE FROM SharedPetGalleryComment c WHERE c.photoId = ?1")
//    List<SharedPetGalleryComment> deleteByPhotoId(Long photoId);
//
//    //펫계정 삭제시 해당 펫에 속해있던 케시글의 모든 댓글 삭제
//    @Modifying
//    @Query("DELETE FROM SharedPetGalleryComment c WHERE c.petId = ?1")
//    List<SharedPetGalleryComment> deleteByPetId(Long petId);
//
//    //사용자 탈퇴시 해당 사용자가 작성한 모든 댓글 삭제
//    @Modifying
//    @Query("DELETE FROM SharedPetGalleryComment c WHERE c.email = ?1 ")
//    List<SharedPetGalleryComment> deleteByEmail(String email);
//
//    //양육자 삭제시 해당 사용자가 작성한 모든 댓글 삭제
//    @Modifying
//    @Query("DELETE FROM SharedPetGalleryComment c WHERE c.email = ?1 AND c.petId = ?2")
//    List<SharedPetGalleryComment> deleteByEmailAndPetId(String email, Long petId);
}
