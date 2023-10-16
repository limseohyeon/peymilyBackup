package com.example.backend.entity;

import com.example.backend.entity.Pet;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;


import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Map;
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
    @JoinColumn(name = "petName", referencedColumnName = "petName")
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
    //private Integer isCompleted;

    // 날짜와 스케줄ID -> email
//    @ElementCollection
//    private Map<String, Map<Long, String>> complete;

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

//    public Schedule build(Long scheduleId,
//                          Pet pet,
//                          String schedule,
//                          String date,
//                          String hm,
//                          String executorEmail,
//                          String executor,
//                          Integer period) {
//        return builder()
//                .scheduleId(scheduleId)
//                .pet(pet)
//                .schedule(schedule)
//                .date(date)
//                .hm(hm)
//                .executorEmail(executorEmail)
//                .executor(executor)
//                .period(period)
//                .build();
//    }
}
