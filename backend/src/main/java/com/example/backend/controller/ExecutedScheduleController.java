package com.example.backend.controller;

import com.example.backend.entity.ExecutedSchedule;
import com.example.backend.service.ExecutedScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// date랑 scheduleId가 들어가면 email이 나와야 함
@RestController
@RequestMapping(value = "/executed", produces = MediaType.APPLICATION_JSON_VALUE)
public class ExecutedScheduleController {
    @Autowired
    private ExecutedScheduleService executedScheduleService;

    @GetMapping("/getAll")
    public List<ExecutedSchedule> getAll() {
        return executedScheduleService.getAll();
    }

    @GetMapping("/{email}/{scheduleId}/{date}")
    public ResponseEntity<ExecutedSchedule> getEmail(@PathVariable("email") String email,
                                                     @PathVariable("scheduleId") Long scheduleId,
                                                     @PathVariable("date") String date) {

        ExecutedSchedule existingExecutedSchedule =
                executedScheduleService.findExecutedScheduleByScheduleIdAndDate(scheduleId, date);

        if (existingExecutedSchedule != null) {
            existingExecutedSchedule.setEmail(email);
        } else {
            ExecutedSchedule newExecutedSchedule = ExecutedSchedule.builder()
                    .scheduleId(scheduleId)
                    .date(date)
                    .email(email)
                    .build();

            executedScheduleService.save(newExecutedSchedule);
            existingExecutedSchedule = newExecutedSchedule;
        }

        return ResponseEntity.ok(existingExecutedSchedule);
    }

    @DeleteMapping("/delete/{scheduleId}/{date}")
    public ResponseEntity<ExecutedSchedule> deleteExecution(@PathVariable("scheduleId") Long scheduleId,
                                                            @PathVariable("date") String date) {
        try {
            ExecutedSchedule toDelete =
                    executedScheduleService.findExecutedScheduleByScheduleIdAndDate(scheduleId, date);

            executedScheduleService.deleteByExecutedId(toDelete.getExecutedId());

            return ResponseEntity.ok(toDelete);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}