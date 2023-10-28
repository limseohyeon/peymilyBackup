package com.example.backend.controller;

import com.example.backend.dto.ScheduleRequest;
import com.example.backend.entity.*;
import com.example.backend.repository.PetLinkRepository;
import com.example.backend.repository.PetRepository;
import com.example.backend.repository.ScheduleRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.service.ExecutedScheduleService;
import com.example.backend.service.ScheduleService;
import com.example.backend.service.UserService;
import com.mysql.cj.conf.ConnectionUrlParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.modelmapper.ModelMapper;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static org.hibernate.sql.InFragment.NULL;
import org.modelmapper.internal.Pair;
@RestController
@RequestMapping("/schedule/{email}")
public class ScheduleController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
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

    @Autowired
    private ExecutedScheduleService executedScheduleService;

//일정 생성
    @PostMapping("/add/{petId}")
    public ResponseEntity<Schedule> createSchedule(@PathVariable("petId") Long petId,
                                                   @PathVariable("email") String owner,
                                                   @RequestBody @Valid ScheduleRequest scheduleRequest) {
        try {
//            Optional<PetLink> optionalPet = petLinkRepository.findLinkByOwner(owner);
            List<PetLink> petLinks = petLinkRepository.findLinkByOwner(owner);

           for(PetLink p : petLinks){
               if(p.getPetId().equals(petId)){
                   Schedule savedSchedule = scheduleService.saveSchedule(scheduleRequest, petId);
                   return ResponseEntity.status(HttpStatus.CREATED).body(savedSchedule);
               }
           }

//기존 코드(petLinkX)
//            if (optionalPet.get().getPetId().equals(petId) && optionalPet.get().getOwner().equals(owner)) {
//                Schedule savedSchedule = scheduleService.saveSchedule(scheduleRequest, petId);
//                //Schedule schedule = modelMapper.map(scheduleService, Schedule.class);
//                //schedule.setPet(pet);
//                //Schedule savedSchedule = scheduleRepository.save(schedule);
//                //ScheduleService savedScheduleService = modelMapper.map(savedSchedule, ScheduleService.class);
//
//                return ResponseEntity.status(HttpStatus.CREATED).body(savedSchedule);
//            }
        } catch (UsernameNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error occurred while saving pet.");
        }
        return ResponseEntity.notFound().build();
    }


// 모든 일정 불러오기
    @GetMapping("/get-all/{petId}")
    public ResponseEntity<List<Schedule>> getAllSchedulesByPetId(@PathVariable("petId") Long petId,
                                                              @PathVariable("email") String owner) {
        Optional<Pet> optionalPet = petRepository.findById(petId);
        try {
            if (optionalPet != null && optionalPet.isPresent() && owner != null) {
                List<Schedule> schedules = scheduleRepository.findSchedulesByPetId(petId);
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
//특정 일정 불러오기
@GetMapping("/get/{petId}/{id}")
public ResponseEntity<Schedule> getSchedulesByPetId(@PathVariable("petId") Long petId,
                                                          @PathVariable("id") Long id) {

    Optional<Pet> optionalPet = petRepository.findById(petId);

    try {
        if (optionalPet != null && optionalPet.isPresent()) {
            List<Schedule> schedules = scheduleRepository.findSchedulesByPetId(petId);
            for(Schedule s : schedules){
                if(s.getScheduleId().equals(id)) return ResponseEntity.ok(s);
            }
        }
        return ResponseEntity.notFound().build();
    } catch (UsernameNotFoundException e) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
    } catch (Exception e) {
        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error occurred while saving pet.");
    }
}
    //일정 수정
    @PutMapping("/update/{petId}/{id}")
    public ResponseEntity<Schedule> updateSchedule(@PathVariable("petId") Long petId,
                                                   @PathVariable("id") Long id,
                                                   @RequestBody Schedule scheduleBody) {
        try {
            List<Schedule> schedules = scheduleRepository.findSchedulesByPetId(petId);
            for (Schedule sch : schedules) {
                if (sch.getScheduleId().equals(id)) {
                    // 스케줄 엔티티 필드값 변경
                    sch.setSchedule(scheduleBody.getSchedule());
                    sch.setDate(scheduleBody.getDate());
                    sch.setHm(scheduleBody.getHm());
                    sch.setExecutorEmail(scheduleBody.getExecutorEmail());
                    sch.setExecutor(scheduleBody.getExecutor());
                    sch.setPeriod(scheduleBody.getPeriod());

                    scheduleRepository.save(sch);
                    System.out.println("Schedule entity updated: " + sch);

                    return ResponseEntity.ok(sch);
                }
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error occurred while updating the schedule.");
        }
        return ResponseEntity.notFound().build();
    }

    //일정 삭제
    @DeleteMapping("/delete/{petId}/{id}")
    public ResponseEntity<String> deleteSchedule(@PathVariable("petId") Long petId,
                                                   @PathVariable("id") Long id) {
        try {
            List<Schedule> schedules = scheduleRepository.findSchedulesByPetId(petId);

            for (Schedule sch : schedules) {
                if (sch.getScheduleId().equals(id)) {
                    Schedule deletedSchedule = sch;
                    scheduleRepository.deleteById(id);

                    return ResponseEntity.ok("deleted successfully");
                }
            }
        } catch (UsernameNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error occurred while deleting pet.");
        }

        // 해당 id에 대한 스케줄이 없는 경우 Not Found 반환
        return ResponseEntity.notFound().build();
    }
    //일정 수행
    @PutMapping("/complete/{id}")
    public ResponseEntity<String> completeSchedule(
            @PathVariable("email") String email,
            @PathVariable("id") Long id, // 스케줄 아이디
            @RequestBody ScheduleRequest scheduleRequest) {
        try {
            Optional<User> user = userService.getUserByEmail(email);
            if (!user.isPresent()) {
                return ResponseEntity.notFound().build();
            }

            Schedule schedule = scheduleService.findByScheduleId(id);
            if (schedule == null) {
                return ResponseEntity.notFound().build();
            }

            // 현재 날짜 가져오기
            LocalDate currentDate = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String formattedDate = currentDate.format(formatter);

            List<ExecutedSchedule> executedSchedules = executedScheduleService.findByScheduleId(id);
            Long newValue = id;

            // 추가만 하는 이유는 이미 돼있는 상태에서 다시 누를 경우 삭제 코드가 실행되게 하려고
            ExecutedSchedule executedSchedule = new ExecutedSchedule();
            executedSchedule.setScheduleId(id);
            executedSchedule.setDate(formattedDate);
            executedScheduleService.save(executedSchedule);

            return ResponseEntity.ok(email);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while completing schedule.");
        }
    }
//    //수행자 통계
//    @GetMapping("/get-by-date-range-with-executor-count/{petId}")
//    public ResponseEntity<Map<String, Integer>> getSchedulesByDateRangeWithExecutorCount(
//            @PathVariable("petId") Long petId,
//            @RequestParam("startDate") String startDate,
//            @RequestParam("endDate") String endDate) {
//
//        try {
//            List<Schedule> schedules = scheduleRepository.findSchedulesByPetId(petId);
//
//            // 날짜 범위를 지정한 일정을 추출
//            List<Schedule> filteredSchedules = schedules.stream()
//                    .filter(schedule -> isDateInRange(schedule.getDate(), startDate, endDate))
//                    .collect(Collectors.toList());
//
//            if (!filteredSchedules.isEmpty()) {
//                // 실행자별 횟수 계산
//                Map<String, Integer> executorCountMap = calculateExecutorCounts(filteredSchedules);
//
//                return ResponseEntity.ok(executorCountMap);
//            } else {
//                return ResponseEntity.notFound().build();
//            }
//        } catch (Exception e) {
//            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error occurred while fetching schedules by date range.");
//        }
//    }
//
//    //날짜 형식 변경
//    private boolean isDateInRange(String date, String startDate, String endDate) {
//        try {
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy년 MM월 dd일");
//            Date scheduleDate = sdf.parse(date);
//            Date start = sdf.parse(startDate);
//            Date end = sdf.parse(endDate);
//
//            return scheduleDate.compareTo(start) >= 0 && scheduleDate.compareTo(end) <= 0;
//        } catch (ParseException e) {
//            // 날짜 형식이 잘못된 경우에 대한 예외 처리
//            return false;
//        }
//    }
//    // 실행 횟수 계산
//    private Map<String, Integer> calculateExecutorCounts(List<Schedule> schedules) {
//        Map<String, Integer> executorCountMap = new HashMap<>();
//
//        for (Schedule schedule : schedules) {
//            String executor = schedule.getExecutor();
//            executorCountMap.put(executor, executorCountMap.getOrDefault(executor, 0) + 1);
//        }
//
//        return executorCountMap;
//    }

//일정별 통계

}

