package com.example.backend.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleService {

    private Long id;
    private String schedule;
    private String date;
    private String hm;
    private Integer period;
    private Integer notice;

    public ScheduleService(String schedule, String date, String hm, Integer period, Integer notice) {
        this.schedule = schedule;
        this.date = date;
        this.hm = hm;
        this.period = period;
        this.notice = notice;
    }
}
