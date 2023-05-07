package com.example.backend.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.ArrayList;

@Entity
@Getter
@Setter
@Table(name = "PET_TBL")
@Data
@AllArgsConstructor(staticName = "build")
@NoArgsConstructor
public class Pet {
    @Id
    @GeneratedValue
    private Long id;

    private String email;
    @Column(unique = true)
    private String petName;
    private Integer petAge;
    private String detailInfo;
    private String inviter;

    @OneToMany(mappedBy = "pet", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Schedule> schedules = new ArrayList<>();
}