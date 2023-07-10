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
    @JoinColumns({
            @JoinColumn(name="petName", referencedColumnName="petName"),
            @JoinColumn(name="inviter", referencedColumnName="inviter")
    })
    @JsonBackReference
    private Pet pet;
    private String schedule;
    private String date;
    private String hm;
    private String executer;
    private Integer period;
    private Integer notice;
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

    public Schedule build(Pet pet, String schedule, String date, String hm, String executer, Integer period, Integer notice, Integer isCompleted) {
        return builder()
                .pet(pet)
                .schedule(schedule)
                .date(date)
                .hm(hm)
                .executer(executer)
                .period(period)
                .notice(notice)
                .isCompleted(isCompleted)
                .build();
    }
}
