package com.example.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;


@Entity
@Table(name = "SHARED_PET_GALLERY_TBL")
@Data
@AllArgsConstructor(staticName = "build")
@NoArgsConstructor
@Builder
public class SharedPetGallery {

    @Id
    @GeneratedValue
    private Long photoId;
    private Long petId;
    private String email;
    private Long likes;
    private String title;
    private String wrote;
    private String date;

    @ElementCollection
    private List<String> likedBy;
}