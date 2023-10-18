package com.example.backend.dto;


import com.example.backend.entity.SharedPetGalleryComment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
public class SharedPetGalleryCommentRequest {
    private Long photoId;
    private Long commentId;
    private String email;
    private String commentInfo;
    private String date;
    private Long petId;

}
