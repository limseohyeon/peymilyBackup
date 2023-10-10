package com.example.backend.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(staticName = "build")
@Table(name = "COMMENTS_TBL")
@Data
@Builder
public class Comment implements Serializable {
        @Id
        @GeneratedValue
        private Long commentId;
        private Long communityId;
        private String email;
        private String commentInfo;
        private String date;
}
