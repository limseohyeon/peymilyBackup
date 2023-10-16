package com.example.backend.repository;

import com.example.backend.entity.Community;
import com.example.backend.entity.ExecutedSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExecutedScheduleRepository  extends JpaRepository<ExecutedSchedule, Long> {
    List<ExecutedSchedule> findByScheduleId(Long scheduleId);
    void deleteByExecutedId(Long executedId);
}
