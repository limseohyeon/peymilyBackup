package com.example.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor(staticName = "build")
@NoArgsConstructor
public class ExecutedScheduleRequest {
    private Long executedId;
    private Long scheduleId;
    private String date;
    private String email;
}
