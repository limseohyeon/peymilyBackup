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
    public ResponseEntity<Schedule> createSchedule(@PathVariable("petName") String petName,
                                                   @PathVariable("inviter") String email,
                                                   @RequestBody ScheduleService scheduleService) {
        Optional<Pet> optionalPet = petRepository.findByPetName(petName);

        if (optionalPet.get().getPetName().equals(petName) && optionalPet.get().getInviter().equals(email)) {
            Pet pet = optionalPet.get();
            Schedule schedule = modelMapper.map(scheduleService, Schedule.class);
            schedule.setPet(pet);
            Schedule savedSchedule = scheduleRepository.save(schedule);
            //ScheduleService savedScheduleService = modelMapper.map(savedSchedule, ScheduleService.class);
            return ResponseEntity.ok(savedSchedule);
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

    @PutMapping("/put-pet/{petName}/{id}")
    public ResponseEntity<ScheduleService> updateSchedule(@PathVariable("petName") String petName,
                                                          @PathVariable("id") Integer id,
                                                          @RequestBody ScheduleService scheduleService) {
        List<Schedule> schedules = scheduleRepository.findByPetPetName(petName);
        for (Schedule sch : schedules) {
            if (sch.getId().equals(id)) {
                // 스케줄 엔티티 필드값 변경
                sch.setSchedule(scheduleService.getSchedule());
                sch.setDate(scheduleService.getDate());
                sch.setHm(scheduleService.getHm());
                sch.setPeriod(scheduleService.getPeriod());
                sch.setNotice(scheduleService.getNotice());
                sch.setIsCompleted(scheduleService.getIsCompleted());

                // 변경된 스케줄 엔티티 저장
                scheduleRepository.save(sch);

                // 변경된 스케줄 정보를 반환
                ScheduleService updatedScheduleService = ScheduleService.builder()
                        .schedule(sch.getSchedule())
                        .date(sch.getDate())
                        .hm(sch.getHm())
                        .period(sch.getPeriod())
                        .notice(sch.getNotice())
                        .isCompleted(sch.getIsCompleted())
                        .build();

                return ResponseEntity.ok(updatedScheduleService);
            }
        }

        // 해당 id에 대한 스케줄이 없는 경우 Not Found 반환
        return ResponseEntity.notFound().build();
    }


    // delete 미완
    @DeleteMapping("/{scheduleId}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable("scheduleId") Long scheduleId) {
        scheduleRepository.deleteById(scheduleId);
        return ResponseEntity.noContent().build();
    }
}
