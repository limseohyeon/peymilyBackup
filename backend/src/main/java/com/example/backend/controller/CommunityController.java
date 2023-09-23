package com.example.backend.controller;

import com.example.backend.dto.CommunityRequest;
import com.example.backend.dto.ScheduleRequest;
import com.example.backend.entity.Community;
import com.example.backend.entity.Invitation;
import com.example.backend.entity.PetLink;
import com.example.backend.service.CommunityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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

    @GetMapping("/get/{communityId}")
    public ResponseEntity<Community> GetCommunity(@PathVariable("communityId") Long communityId) {
        Community communityPosted = communityService.findPostById(communityId);

        if (communityPosted.equals(null)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(communityPosted);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<Community>> GetAllCommunity() {
        List<Community> allOfCommunityPosted = communityService.findAllPost();

        if (allOfCommunityPosted.size() == 0) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(allOfCommunityPosted);
    }

    @PutMapping("/update")
    public ResponseEntity<Community> GetCommunity(@Valid @RequestBody CommunityRequest updatedCommunityData) {
        try {
            Community existingCommunity = communityService.findPostById(updatedCommunityData.getCommunityId());

            if (existingCommunity == null) {
                System.out.println("수정할 게시글을 찾을 수 없습니다");
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            Community newCommunity = communityService.updateCommunity(updatedCommunityData);
            System.out.println("게시글 수정 성공");

            return new ResponseEntity<>(newCommunity, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println("게시글 수정 실패");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/{communityId}")
    public ResponseEntity<Community> DeleteCommunity(@PathVariable("communityId") Long communityId) {
        Community communityToDelete = communityService.findPostById(communityId);

        communityService.deleteCommunity(communityId);

        return ResponseEntity.ok(communityToDelete);
    }
}
