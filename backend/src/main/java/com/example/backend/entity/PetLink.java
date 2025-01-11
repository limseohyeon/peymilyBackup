package com.example.backend.entity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

//@Entity
//@Table(name = "PET_LINK_TBL")
//@Data
//@NoArgsConstructor // 기본 생성자 생성 - builder랑 호환 안되고 builder 없애야 함
//public class PetLink implements Serializable {
//    @Id
//    @GeneratedValue
//    private Long linkId;
//    private String owner;
//    private String inviter;
//    private Long petId;
//}


//


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(staticName = "build")
@Table(name = "PET_LINK_TBL")
@Data
@Builder
public class PetLink implements Serializable {
    // Serializable은 객체를 전송또는 저장할 때 필요하다
    // 즉, 전송 또는 저장을 하지 않는 단순 출력문만 있는 경우에는 불필요하다
//    @Id
//    @GeneratedValue
//    private Long id;
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id", referencedColumnName="userId") // 230709 긴급 수정
//    @JsonBackReference
//    private User user;
//    @Column(unique = true)

    @Id
    @GeneratedValue
    private Long linkId;
    private String owner;
    private String inviter;
    private Long petId;
    private String ownerName;

    @OneToMany(mappedBy = "pet", cascade = CascadeType.ALL, orphanRemoval = true)

    public static PetLink build(User user, String owner, String inviter, Long petId, String ownerName) {
        return PetLink.builder()
                .owner(owner)
                .inviter(inviter)
                .petId(petId)
                .ownerName(ownerName)
                .build();
    }
}