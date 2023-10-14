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

import java.util.Optional;

@Service
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder // ScheduleController.java에서 build()가 가능하게 해줌
public class ScheduleService {

    private Long id;
    private String schedule;
    private String date;
    private String hm;
    private String executor;
    private Integer period;
    private String complete;
    private Integer isCompleted;
    @Autowired
    private PetService petService;
    @Autowired
    private PetRepository petRepository;
    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private PetLinkRepository petLinkRepository;

    public Schedule saveSchedule(ScheduleRequest schedulerequest, Long petId) {
        Optional<Pet> optionalPet = petRepository.findById(petId);

        if (optionalPet.isPresent()) {
            Schedule schedule = Schedule.builder()
                    .pet(optionalPet.get())
                    .schedule(schedulerequest.getSchedule())
                    .date(schedulerequest.getDate())
                    .hm(schedulerequest.getHm())
                    .executor(schedulerequest.getExecutor())
                    .period(schedulerequest.getPeriod())
                    .complete(schedulerequest.getComplete())
                    .isCompleted(schedulerequest.getIsCompleted())
                    .build();

            return scheduleRepository.save(schedule);
        } else {
            throw new UsernameNotFoundException("Invalid pet");
        }
    }
}
