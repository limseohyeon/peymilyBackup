package com.example.backend.entity;

import com.example.backend.entity.Pet;
import lombok.*;


import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(staticName = "build")
@Table(name = "SCHEDULE_TBL")
@Builder
public class Schedule {
    @Id
    @GeneratedValue
    private Long id;

    private String schedule;
    private String date;
    private String hm;
    private Integer period;
    private Integer notice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id")
    private Pet pet;
}