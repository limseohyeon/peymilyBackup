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
    @Query("SELECT s FROM Schedule s WHERE s.pet.id = :petId")
    List<Schedule> findSchedulesByPetId(@Param("petId") Long petId);

    List<Schedule> findByPetPetName(String petName);

    @Transactional
    @Modifying
    @Query("UPDATE Schedule s SET s.pet.petName = :newPetName WHERE s.pet.petName = :oldPetName")
    int updateSchedulesWithNewPetName(@Param("oldPetName") String oldPetName, @Param("newPetName") String newPetName);

    @Query("SELECT s FROM Schedule s WHERE s.scheduleId = :scheduleId")
    Schedule findByScheduleId(@Param("scheduleId") Long scheduleId);

    @Modifying
    @Query("DELETE FROM Schedule s WHERE s.scheduleId = :scheduleId")
    void deleteScheduleById(@Param("scheduleId") Long scheduleId);
}
