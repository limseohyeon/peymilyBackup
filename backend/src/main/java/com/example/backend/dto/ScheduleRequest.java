package com.example.backend.dto;

import com.example.backend.entity.Pet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.internal.Pair;

import javax.persistence.*;
import java.util.Date;
import java.util.Map;

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
    private String executor;
    private Integer period;
    private Boolean repeat;
//    private String complete;
//    private Integer isCompleted;
}