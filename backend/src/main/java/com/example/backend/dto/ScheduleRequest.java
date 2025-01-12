package com.example.backend.dto;

import com.example.backend.entity.Pet;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name="SCHEDULE_TBL")
@Data
@AllArgsConstructor(staticName = "build")
@NoArgsConstructor
public class ScheduleRequest {
    @Id
    @GeneratedValue
    private Long scheduleId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name="userId", referencedColumnName="userId"),
    })
    private Pet pet;
//    private String petName;
//    private String inviter;
    private String schedule;
    private String date;
    private String hm;
    private String executorEmail;
    private Integer repeatSchedule;
    private String executor;
    private Integer period;
    private String memo;
//    private String complete;
//    private Integer isCompleted;
}