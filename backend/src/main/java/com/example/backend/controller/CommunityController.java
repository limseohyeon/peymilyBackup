package com.example.backend.controller;

import com.example.backend.dto.CommunityRequest;
import com.example.backend.dto.ScheduleRequest;
import com.example.backend.entity.Community;
import com.example.backend.entity.Invitation;
import com.example.backend.service.CommunityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/community", produces = MediaType.APPLICATION_JSON_VALUE)
public class CommunityController {
    @Autowired
    private CommunityService communityService;

    @PostMapping("/post/{email}")
    public ResponseEntity<Community> PostCommunity(@PathVariable("email") String email,
                                                    @RequestBody @Valid CommunityRequest communityRequest) {
        try {
            communityService.saveCommunity(communityRequest, email);
        } catch(Exception e) {
            System.out.println("Can't save community request. Error : " + e);
        }

        return ResponseEntity.notFound().build();
    }

//    @GetMapping("/get/{email}")
//    public ResponseEntity<Community> GetCommunity() {
//
//    }
}
