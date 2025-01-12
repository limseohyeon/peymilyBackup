package com.example.backend.entity;

import com.example.backend.entity.Pet;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;

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
    private Long scheduleId;
    @ManyToOne(fetch = FetchType.LAZY)
    // 2023-10-15 수정
    @JoinColumn(name = "petCode", referencedColumnName = "petCode")
    @JsonBackReference
    private Pet pet;
    private String schedule;
    private String date;
    private String hm;
    // 실행자 이메일
    private String executorEmail;
    // 실행자
    private String executor;
    private Integer period;
    private Integer repeatSchedule;
    private String memo;
    //private Integer isCompleted;
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
}
