package com.example.backend.controller;

import com.example.backend.dto.ScheduleRequest;
import com.example.backend.entity.Pet;
import com.example.backend.entity.PetLink;
import com.example.backend.entity.Schedule;
import com.example.backend.entity.User;
import com.example.backend.repository.PetLinkRepository;
import com.example.backend.repository.PetRepository;
import com.example.backend.repository.ScheduleRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.modelmapper.ModelMapper;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.hibernate.sql.InFragment.NULL;

@RestController
@RequestMapping("/schedule/{email}")
public class ScheduleController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PetRepository petRepository;
    @Autowired
    private ScheduleService scheduleService;
    @Autowired
    private ScheduleRepository scheduleRepository;
    //수정
    @Autowired
    private PetLinkRepository petLinkRepository;

    @Autowired
    private ModelMapper modelMapper;

    @PostMapping("/add/{petId}")
    public ResponseEntity<Schedule> createSchedule(@PathVariable("petId") Long petId,
                                                   @PathVariable("email") String owner,
                                                   @RequestBody @Valid ScheduleRequest scheduleRequest) {
        try {
            Optional<PetLink> optionalPet = petLinkRepository.findLinkByOwner(owner);

            if (optionalPet.get().getPetId().equals(petId) && optionalPet.get().getOwner().equals(owner)) {
                Schedule savedSchedule = scheduleService.saveSchedule(scheduleRequest, petId);
                //Schedule schedule = modelMapper.map(scheduleService, Schedule.class);
                //schedule.setPet(pet);
                //Schedule savedSchedule = scheduleRepository.save(schedule);
                //ScheduleService savedScheduleService = modelMapper.map(savedSchedule, ScheduleService.class);

                return ResponseEntity.status(HttpStatus.CREATED).body(savedSchedule);
            }
        } catch (UsernameNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error occurred while saving pet.");
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/get-all/{petId}")
    public ResponseEntity<List<Schedule>> getSchedulesByPetId(@PathVariable("petId") Long petId,
                                                              @PathVariable("email") String owner) {
        Optional<Pet> optionalPet = petRepository.findById(petId);
        try {
            if (optionalPet != null && optionalPet.isPresent() && owner != null) {
                List<Schedule> schedules = scheduleRepository.findByPetId(petId);
                /*List<ScheduleService> scheduleServices = schedules.stream()
                        .map(schedule -> modelMapper.map(schedule, ScheduleService.class))
                        .collect(Collectors.toList());
                 */
                return ResponseEntity.ok(schedules);
            }
            return ResponseEntity.notFound().build();
        } catch (UsernameNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error occurred while saving pet.");
        }
    }

    @PutMapping("/get-schedule/{petId}/{id}")
    public ResponseEntity<ScheduleService> updateSchedule(@PathVariable("petId") Long petId,
                                                          @PathVariable("id") Long id,
                                                          @RequestBody ScheduleService scheduleService) {
        try {
            List<Schedule> schedules = scheduleRepository.findByPetId(petId);
            for (Schedule sch : schedules) {
                if (sch.getId() == id) {
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
        } catch (UsernameNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error occurred while saving pet.");
        }
        // 해당 id에 대한 스케줄이 없는 경우 Not Found 반환
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/delete/{petId}/{id}")
    public ResponseEntity<Schedule> deleteSchedule(@PathVariable("petId") Long petId,
                                                   @PathVariable("id") Long id) {
        try {
            List<Schedule> schedules = scheduleRepository.findByPetId(petId);
            for (Schedule sch : schedules) {
                if (sch.getId() == id) {
                    Schedule deletedSchedule = sch;
                    scheduleRepository.deleteById(sch.getId());

                    return ResponseEntity.ok(deletedSchedule);
                }
            }
        } catch (UsernameNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error occurred while saving pet.");
        }

        // 해당 id에 대한 스케줄이 없는 경우 Not Found 반환
        return ResponseEntity.notFound().build();
    }
}
