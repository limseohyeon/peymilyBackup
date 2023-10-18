package com.example.backend.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(staticName = "build")
@Table(name = "GALLERY_COMMENT_TBL")
@Data
@Builder
public class SharedPetGalleryComment implements Serializable {
    @Id
    @GeneratedValue
    private Long commentId;
    private Long photoId;
    private Long petId;
    private String email;
    private String commentInfo;
    private String date;
}
