package com.example.backend.entity;

import com.example.backend.entity.Pet;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;


import javax.persistence.*;
import java.util.List;
import java.util.Objects;

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
    @ManyToOne(fetch = FetchType.LAZY)
    // 2023-10-15 수정
    @JoinColumn(name = "pet_id", referencedColumnName = "id")
    @JsonBackReference
    private Pet pet;
    private String schedule;
    private String date;
    private String hm;
    private String executor;
    private Integer period;
    private String complete;
    private Integer isCompleted;

    // EqualsAndHashCode 메서드 오버라이딩
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Schedule schedule = (Schedule) o;
        return Objects.equals(pet, schedule.pet);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pet);
    }

    public Schedule build(Pet pet, String schedule, String date, String hm, String executor, Integer period, String complete, Integer isCompleted) {
        return builder()
                .pet(pet)
                .schedule(schedule)
                .date(date)
                .hm(hm)
                .executor(executor)
                .period(period)
                .complete(complete)
                .isCompleted(isCompleted)
                .build();
    }
}
