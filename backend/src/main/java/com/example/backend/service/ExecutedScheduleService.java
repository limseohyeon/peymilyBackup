package com.example.backend.service;

import com.example.backend.entity.ExecutedSchedule;
import com.example.backend.repository.ExecutedScheduleRepository;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import org.springframework.transaction.annotation.Transactional;

@Service
@Builder
public class ExecutedScheduleService {
    private final ExecutedScheduleRepository executedScheduleRepository;

    @Autowired
    public ExecutedScheduleService(ExecutedScheduleRepository executedScheduleRepository) {
        this.executedScheduleRepository = executedScheduleRepository;
    }

    public List<ExecutedSchedule> findByScheduleId(Long scheduleId) {
        return executedScheduleRepository.findByScheduleId(scheduleId);
    }

    public void save(ExecutedSchedule executedSchedule) {
        executedScheduleRepository.save(executedSchedule);
    }

    @Transactional
    public void deleteByExecutedId(Long executedId) {
        executedScheduleRepository.deleteByExecutedId(executedId);
    }

    public ExecutedSchedule findExecutedScheduleByScheduleIdAndDate(Long scheduleId, String date) {
        return executedScheduleRepository.findExecutedScheduleByScheduleIdAndDate(scheduleId, date);
    }

    @Transactional
    public void updateEmailByScheduleIdAndDate(Long scheduleId, String date, String email) {
        executedScheduleRepository.updateEmailByScheduleIdAndDate(scheduleId, date, email);
    }

    public List<ExecutedSchedule> getAll() {
       return executedScheduleRepository.getAll();
    }
}
