package com.example.backend.service;

import com.example.backend.dto.PetRequest;
import com.example.backend.dto.ScheduleRequest;
import com.example.backend.entity.Pet;
import com.example.backend.entity.PetLink;
import com.example.backend.entity.Schedule;
import com.example.backend.entity.User;
import com.example.backend.repository.PetLinkRepository;
import com.example.backend.repository.PetRepository;
import com.example.backend.repository.ScheduleRepository;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.Optional;

@Service
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder // ScheduleController.java에서 build()가 가능하게 해줌
public class ScheduleService {
    @Autowired
    private PetService petService;
    @Autowired
    private PetRepository petRepository;
    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private PetLinkRepository petLinkRepository;

    public Schedule findByScheduleId(Long scheduleId) {
        return findByScheduleId(scheduleId);
    }

    public Schedule save(ScheduleRequest scheduleRequest) {
        Schedule schedule = Schedule.builder()
                .pet(scheduleRequest.getPet())
                .schedule(scheduleRequest.getSchedule())
                .date(scheduleRequest.getDate())
                .hm(scheduleRequest.getHm())
                .executorEmail(scheduleRequest.getExecutorEmail())
                .executor(scheduleRequest.getExecutor())
                .period(scheduleRequest.getPeriod())
                .build();

        return scheduleRepository.save(schedule);
    }
    public Schedule saveSchedule(ScheduleRequest schedulerequest, Long petId) {
        Optional<Pet> optionalPet = petRepository.findById(petId);

        if (optionalPet.isPresent()) {
            Schedule schedule = Schedule.builder()
                    .pet(optionalPet.get())
                    .schedule(schedulerequest.getSchedule())
                    .date(schedulerequest.getDate())
                    .hm(schedulerequest.getHm())
                    .executorEmail(schedulerequest.getExecutorEmail())
                    .executor(schedulerequest.getExecutor())
                    .period(schedulerequest.getPeriod())
                    .build();

            return scheduleRepository.save(schedule);
        } else {
            throw new UsernameNotFoundException("Invalid pet");
        }
    }
}
