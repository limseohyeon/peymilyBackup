package com.example.backend.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.ArrayList;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(staticName = "build")
@Table(name = "PET_TBL")
@Data
@Builder
public class Pet {
    @Id
    @GeneratedValue
    private Long id;
    private Long userId; // User 객체의 userId 필드 값을 저장할 필드
    private String email;
    @Column(unique = true)
    private String petName;
    private Integer petAge;
    private String detailInfo;
    private String inviter;

    @OneToMany(mappedBy = "pet", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Schedule> schedules = new ArrayList<>();

    public static Pet build(Long userId, String email, String petName, Integer petAge, String detailInfo, String inviter) {
        return Pet.builder()
                .userId(userId)
                .email(email)
                .petName(petName)
                .petAge(petAge)
                .detailInfo(detailInfo)
                .inviter(inviter)
                .build();
    }
}