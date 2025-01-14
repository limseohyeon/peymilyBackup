package com.example.backend.entity;

import com.example.backend.users.entity.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.*;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(staticName = "build")
@Table(
        name = "PET_TBL",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"id"})
        }
)
@Data
@Builder
public class Pet implements Serializable {
    // Serializable은 객체를 전송또는 저장할 때 필요하다
    // 즉, 전송 또는 저장을 하지 않는 단순 출력문만 있는 경우에는 불필요하다
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName="userId") // 230709 긴급 수정
    @JsonBackReference
    private User user;
    @Column(unique = true)
    private String petCode;
    private String petName;
    private Integer petAge;
    private String detailInfo;
    private String gender;
    private String inviter;

    @OneToMany(mappedBy = "pet", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Schedule> schedules = new ArrayList<>();

    public static Pet build(User user, String petName, String petCode, Integer petAge, String detailInfo, String inviter, String gender) {
        return Pet.builder()
                .user(user)
                .petName(petName)
                .petCode(petCode)
                .petAge(petAge)
                .detailInfo(detailInfo)
                .inviter(inviter)
                .gender(gender)
                .build();
    }
}