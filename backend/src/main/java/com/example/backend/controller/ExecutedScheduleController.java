package com.example.backend.controller;

import com.example.backend.entity.ExecutedSchedule;
import com.example.backend.service.ExecutedScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// date랑 scheduleId가 들어가면 email이 나와야 함
@RestController
@RequestMapping(value = "/executed", produces = MediaType.APPLICATION_JSON_VALUE)
public class ExecutedScheduleController {
    @Autowired
    private ExecutedScheduleService executedScheduleService;

    @GetMapping("/{email}/{scheduleId}/{date}")
    public ResponseEntity<ExecutedSchedule> getEmail(@PathVariable("email") String email,
                                   @PathVariable("scheduleId") Long scheduleId,
                                   @PathVariable("date") String date) {

        List<ExecutedSchedule> executedSchedule = executedScheduleService.findByScheduleId(scheduleId);

        if (executedSchedule.size() == 0) {
            ExecutedSchedule newExecutedSchedule =
                    ExecutedSchedule.builder()
                    .scheduleId(scheduleId)
                    .date(date)
                    .build();

            executedScheduleService.save(newExecutedSchedule);

            return ResponseEntity.ok(newExecutedSchedule);
        }

        for (ExecutedSchedule ex : executedSchedule) {
            if (ex.getDate().equals(date) && ex.getScheduleId().equals(scheduleId)) {
                ex.setEmail(email);
                break;
            }
        }

        ExecutedSchedule retEs = executedScheduleService.findExecutedScheduleByScheduleIdAndDate(scheduleId, date);

        return ResponseEntity.ok(retEs);
    }
}
