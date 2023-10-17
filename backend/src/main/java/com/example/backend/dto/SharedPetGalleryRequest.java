package com.example.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor(staticName = "build")
@NoArgsConstructor
@Builder
public class SharedPetGalleryRequest {
    private Long photoId;
    private String email;
    private Long likes;
    private String title;
    private String wrote;
    private String date;
}
