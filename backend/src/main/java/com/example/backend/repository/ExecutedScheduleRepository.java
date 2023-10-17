package com.example.backend.repository;

import com.example.backend.entity.Community;
import com.example.backend.entity.ExecutedSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ExecutedScheduleRepository  extends JpaRepository<ExecutedSchedule, Long> {
    List<ExecutedSchedule> findByScheduleId(Long scheduleId);
    void deleteByExecutedId(Long executedId);

    @Modifying
    @Query("SELECT es FROM ExecutedSchedule es WHERE es.scheduleId = :scheduleId AND es.date = :date")
    ExecutedSchedule findExecutedScheduleByScheduleIdAndDate(@Param("scheduleId") Long scheduleId,
                                                             @Param("date") String date);

}