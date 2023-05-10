package com.example.backend.controller;

import com.example.backend.entity.Pet;
import com.example.backend.entity.Schedule;
import com.example.backend.entity.User;
import com.example.backend.repository.PetRepository;
import com.example.backend.repository.ScheduleRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.modelmapper.ModelMapper;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.hibernate.sql.InFragment.NULL;

@RestController
@RequestMapping("/schedule/{inviter}")
public class ScheduleController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PetRepository petRepository;
    @Autowired
    private ScheduleRepository scheduleRepository;
    @Autowired
    private ModelMapper modelMapper;

    @PostMapping("/{petName}")
    public ResponseEntity<ScheduleService> createSchedule(@PathVariable("petName") String petName, @PathVariable("inviter") String email, @RequestBody ScheduleService scheduleService) {
        Optional<Pet> optionalPet = petRepository.findByPetName(petName);

        if (optionalPet != null && optionalPet.isPresent() && email != null) {
            Pet pet = optionalPet.get();
            Schedule schedule = modelMapper.map(scheduleService, Schedule.class);
            schedule.setPet(pet);
            Schedule savedSchedule = scheduleRepository.save(schedule);
            ScheduleService savedScheduleService = modelMapper.map(savedSchedule, ScheduleService.class);
            return ResponseEntity.ok(savedScheduleService);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{petName}")
    public ResponseEntity<List<ScheduleService>> getSchedulesByPetId(@PathVariable("petName") String petName, @PathVariable("inviter") String email) {
        Optional<Pet> optionalPet = petRepository.findByPetName(petName);

        if (optionalPet != null && optionalPet.isPresent() && email != null) {
            List<Schedule> schedules = scheduleRepository.findByPetPetName(petName);
            List<ScheduleService> scheduleServices = schedules.stream()
                    .map(schedule -> modelMapper.map(schedule, ScheduleService.class))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(scheduleServices);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{scheduleId}")
    public ResponseEntity<ScheduleService> updateSchedule(@PathVariable("scheduleId") Long scheduleId, @RequestBody ScheduleService scheduleService) {
        Optional<Schedule> optionalSchedule = scheduleRepository.findById(scheduleId);
        if (optionalSchedule.isPresent()) {
            Schedule schedule = optionalSchedule.get();
            modelMapper.map(scheduleService, schedule);
            Schedule updatedSchedule = scheduleRepository.save(schedule);
            ScheduleService updatedScheduleService = modelMapper.map(updatedSchedule, ScheduleService.class);
            return ResponseEntity.ok(updatedScheduleService);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{scheduleId}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable("scheduleId") Long scheduleId) {
        scheduleRepository.deleteById(scheduleId);
        return ResponseEntity.noContent().build();
    }
}
