package com.example.backend.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(staticName = "build")
@Table(name = "EXECUTED_TBL")
@Builder
public class ExecutedSchedule {
    @Id
    @GeneratedValue
    private Long executedId;
    private Long scheduleId;
    private String date;
}
