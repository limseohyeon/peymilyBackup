package com.example.backend.repository;

import com.example.backend.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findAll();

    @Query("SELECT s FROM Schedule s WHERE s.pet.id = :petId")
    List<Schedule> findSchedulesByPetId(@Param("petId") Long petId);
    //findByPetName (x) findByPetName (o)
    List<Schedule> findByPetPetName(String petName);
    @Transactional  // 모든 작업이 성공하거나 실패할 때까지 적용이 안됨! 원자성 만족
    @Modifying
    @Query("UPDATE Schedule s SET s.schedule = :newSchedule, s.date = :newDate, s.hm = :newHm, s.executorEmail = :newExecutorEmail, s.executor = :newExecutor, s.period = :newPeriod WHERE s.scheduleId = :scheduleId")
    int updateSchedule(@Param("scheduleId") Long scheduleId,
                       @Param("newSchedule") String newSchedule,
                       @Param("newDate") String newDate,
                       @Param("newHm") String newHm,
                       @Param("newExecutorEmail") String newExecutorEmail,
                       @Param("newExecutor") String newExecutor,
                       @Param("newPeriod") Integer newPeriod);

    @Transactional
    @Modifying
    @Query("UPDATE Schedule s SET s.pet.petName = :newPetName WHERE s.pet.petName = :oldPetName")
    int updateSchedulesWithNewPetName(@Param("oldPetName") String oldPetName, @Param("newPetName") String newPetName);

    @Modifying
    @Query("SELECT s FROM Schedule s WHERE s.scheduleId = :scheduleId")
    Schedule findByScheduleId(@Param("scheduleId") Long scheduleId);
}
