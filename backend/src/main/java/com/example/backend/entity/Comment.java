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

        // 이하 삭제
//        @ManyToOne(fetch = FetchType.LAZY)
//        @JoinColumn(name = "shared_pet_id")
//        @JsonBackReference // 순환 참조 방지용 코드
//        private SharedPet sharedPet;
        private Long communityId;
        private String userName;
        private String commentInfo;
}
