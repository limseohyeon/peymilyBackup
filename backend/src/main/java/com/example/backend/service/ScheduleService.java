package com.example.backend.service;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder // ScheduleController.java에서 build()가 가능하게 해줌
public class ScheduleService {

    private Long id;
    private String schedule;
    private String date;
    private String hm;
    private Integer period;
    private Integer notice;
    private Integer isCompleted;

    public ScheduleService(String schedule, String date, String hm, Integer period, Integer notice, Integer isCompleted) {
        this.schedule = schedule;
        this.date = date;
        this.hm = hm;
        this.period = period;
        this.notice = notice;
        this.isCompleted = isCompleted;
    }
}
