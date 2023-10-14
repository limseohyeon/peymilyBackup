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
    /*
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name="petName", referencedColumnName="petName"),
            @JoinColumn(name="inviter", referencedColumnName="inviter")
    })
    private Pet pet;*/
    private String petName;
    private String inviter;
    private String schedule;
    private String date;
    private String hm;
    private String executor;
    private Integer period;
    private String complete;
    private Integer isCompleted;
}