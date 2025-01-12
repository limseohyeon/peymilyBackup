package com.example.backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

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
    private String email;
}
