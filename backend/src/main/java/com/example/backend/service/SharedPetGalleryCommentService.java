package com.example.backend.service;

import com.example.backend.dto.SharedPetGalleryCommentRequest;
import com.example.backend.dto.SharedPetGalleryRequest;
import com.example.backend.entity.Community;
import com.example.backend.entity.SharedPetGalleryComment;
import com.example.backend.repository.SharedPetGalleryCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class SharedPetGalleryCommentService {
    @Autowired
    private final SharedPetGalleryCommentRepository sharedPetGalleryCommentRepository;

    //댓글 저장
    public SharedPetGalleryComment saveComment(SharedPetGalleryCommentRequest sharedPetGalleryCommentRequest) {
        SharedPetGalleryComment comment = SharedPetGalleryComment.builder()
                .photoId(sharedPetGalleryCommentRequest.getPhotoId())
                .email(sharedPetGalleryCommentRequest.getEmail())
                .commentInfo(sharedPetGalleryCommentRequest.getCommentInfo())
                .date(sharedPetGalleryCommentRequest.getDate())
                .petId(sharedPetGalleryCommentRequest.getPetId())
                .build();

        return sharedPetGalleryCommentRepository.save(comment);
    }

    public SharedPetGalleryComment findCommentById(Long commentId) {
        return sharedPetGalleryCommentRepository.findCommentByCommentId(commentId);
    }

    // photoId를 바탕으로 커뮤니티 게시글의 댓글을 불러옴(해당 게시글 모든 댓글 가져오기)
    public List<SharedPetGalleryComment> findAllCommentByPhotoId(Long photoId) {
        return sharedPetGalleryCommentRepository.findAllCommentByPhotoId(photoId);
    }
    // Email 을 기준으로 댓글 가져옴

    public List<SharedPetGalleryComment> findAllCommentByEmail(String email) {
        return sharedPetGalleryCommentRepository.findCommentByEmail(email);
    }
    // 양육자 기준으로 댓글 가져옴
    public List<SharedPetGalleryComment> findAllCommentByEmailAndPetId(String email,Long petId) {
        return sharedPetGalleryCommentRepository.findCommentByEmailAndPetId(email, petId);
    }

    //petId 기준으로 댓글 가져옴
    public List<SharedPetGalleryComment> findAllCommentByPetId(Long petId) {
        return sharedPetGalleryCommentRepository.findCommentByPetId(petId);
    }

    // 댓글 내용 수정
    public SharedPetGalleryComment updateCommentByPhotoId(SharedPetGalleryCommentRequest sharedPetGalleryCommentRequest, Long photoId) {
        SharedPetGalleryComment commentToChange = findCommentById(photoId);
        commentToChange.setCommentInfo(sharedPetGalleryCommentRequest.getCommentInfo());
        if (commentToChange.getCommentInfo().length() == 0) {
            System.out.println("내용을 입력하세요!");
            return null;
        }
        return sharedPetGalleryCommentRepository.save(commentToChange);
    }
    //특정 댓글 삭제
    public SharedPetGalleryComment deleteCommentById(Long commentId) {
        SharedPetGalleryComment commentToDelete = findCommentById(commentId);
        sharedPetGalleryCommentRepository.deleteByCommentId(commentId);
        return commentToDelete;
    }

    //펫 계정 삭제시 해당 펫에 속한 모든 댓글 삭제
    public int deleteCommentByPetId(Long petId) {
        sharedPetGalleryCommentRepository.deleteByPetId(petId);
        return 0;
    }
    //게시글 삭제시 해당 게시글에 속한 모든 댓글 삭제
    public List<SharedPetGalleryComment> deleteCommentByPhotoId(Long photoId) {
        List<SharedPetGalleryComment> commentToDelete = findAllCommentByPhotoId(photoId);
        sharedPetGalleryCommentRepository.deleteByPhotoId(photoId);
        return commentToDelete;
    }
    //양육자 삭제시 해당 사용자가 작성한 모든 댓글 삭제
    public List<SharedPetGalleryComment> deleteCommentByEmailAndPetId(String email, Long petId) {
        List<SharedPetGalleryComment> commentToDelete = findAllCommentByEmailAndPetId(email,petId);
        sharedPetGalleryCommentRepository.deleteByEmailAndPetId(email,petId);
        return commentToDelete;
    }
    //사용자 탈퇴시 해당 사용자가 작성한 모든 댓글 삭제
    public List<SharedPetGalleryComment> deleteCommentByEmail(String email) {
        List<SharedPetGalleryComment> commentToDelete = findAllCommentByEmail(email);
        sharedPetGalleryCommentRepository.deleteByEmail(email);
        return commentToDelete;
    }
}
