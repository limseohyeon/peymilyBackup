package com.example.backend.controller;

import com.example.backend.entity.ExecutedSchedule;
import com.example.backend.service.ExecutedScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

// date랑 scheduleId가 들어가면 email이 나와야 함
@RestController
@RequestMapping(value = "/executed", produces = MediaType.APPLICATION_JSON_VALUE)
public class ExecutedScheduleController {
    @Autowired
    private ExecutedScheduleService executedScheduleService;

    @GetMapping("/{email}/{scheduleId}")
    public ResponseEntity<ExecutedSchedule> getEmail(@PathVariable("email") String email,
                                                     @PathVariable("scheduleId") Long scheduleId) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate = dateFormat.format(new Date());

        ExecutedSchedule existingExecutedSchedule =
                executedScheduleService.findExecutedScheduleByScheduleIdAndDate(scheduleId, currentDate);

        if (existingExecutedSchedule != null) {
            existingExecutedSchedule.setEmail(email);
        } else {
            ExecutedSchedule newExecutedSchedule = ExecutedSchedule.builder()
                    .scheduleId(scheduleId)
                    .date(currentDate)
                    .email(email)
                    .build();

            executedScheduleService.updateEmailByScheduleIdAndDate(scheduleId, currentDate, email);
            existingExecutedSchedule = newExecutedSchedule;
        }

        return ResponseEntity.ok(existingExecutedSchedule);
    }
}