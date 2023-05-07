package com.example.backend.repository;

import com.example.backend.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findAll();
    List<Schedule> findByPetId(Long petId);
    List<Schedule> findByPetName(String petName);
}
