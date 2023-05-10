package com.example.backend.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.ArrayList;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(staticName = "build")
@Table(name = "PET_TBL", uniqueConstraints = {@UniqueConstraint(columnNames = {"petName"})})
@Data
@Builder
public class Pet {
    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;
    @Column(unique = true)
    private String petName;
    private Integer petAge;
    private String detailInfo;
    private String inviter;

    @OneToMany(mappedBy = "pet", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Schedule> schedules = new ArrayList<>();

    public static Pet build(User user, String petName, Integer petAge, String detailInfo, String inviter) {
        return Pet.builder()
                .user(user)
                .petName(petName)
                .petAge(petAge)
                .detailInfo(detailInfo)
                .inviter(inviter)
                .build();
    }
}