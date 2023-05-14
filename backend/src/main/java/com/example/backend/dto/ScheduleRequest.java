package com.example.backend.dto;

import com.example.backend.entity.Pet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Table(name="SCHEDULE_TBL")
@Data
@AllArgsConstructor(staticName = "build")
@NoArgsConstructor
public class ScheduleRequest {
    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name="petName", referencedColumnName="petName"),
            @JoinColumn(name="inviter", referencedColumnName="inviter")
    })
    private Pet pet;
    private String schedule;
    private String date;
    private String hm;
    private Integer period;
    private Integer notice;
    private Integer isCompleted;
}