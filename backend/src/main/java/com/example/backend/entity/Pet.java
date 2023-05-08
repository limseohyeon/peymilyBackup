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
public class Pet {
    @Id
    @GeneratedValue
    private Long id; // pet_id -> id로 수정

    private String email;
    @Column(unique = true)
    private String petName;
    private Integer petAge;
    private String detailInfo;
    private String inviter;

    @OneToMany(mappedBy = "pet", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Schedule> schedules = new ArrayList<>();
}