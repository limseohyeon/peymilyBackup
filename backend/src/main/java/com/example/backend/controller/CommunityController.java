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
            Community newPost = communityService.saveCommunity(communityRequest, email);

            return ResponseEntity.ok(newPost);
        } catch(Exception e) {
            System.out.println("Can't save community request. Error : " + e);
            return ResponseEntity.notFound().build();
        }
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
    public ResponseEntity<Community> UpdateCommunity(@Valid @RequestBody CommunityRequest updatedCommunityData) {
        try {
            Community existingCommunity = communityService.findPostById(updatedCommunityData.getCommunityId());

            if (existingCommunity.equals(null)) {
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

    @PutMapping("/updateLikes/{email}")
    public ResponseEntity<Community> UpdateLikes(@Valid @RequestBody CommunityRequest updatedCommunityData,
                                                 @PathVariable("email") String email) {
        try {
            Community communityFound = communityService.findPostById(updatedCommunityData.getCommunityId());

            if (!communityFound.getLikedBy().contains(email)) {
                communityFound.getLikedBy().add(email);
                communityFound.setLikes(communityFound.getLikes() + 1);
            } else {
                communityFound.getLikedBy().remove(email);
                communityFound.setLikes(communityFound.getLikes() - 1);
            }

            Community newCommunity = communityService.updateCommunity(updatedCommunityData);
            System.out.println("좋아요 수정 성공");

            return new ResponseEntity<>(newCommunity, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println("좋아요 수정 실패");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/{communityId}")
    public ResponseEntity<Void> deleteCommunity(@PathVariable("communityId") Long communityId) {
        Community deletedCommunity = communityService.deleteCommunity(communityId);
        if (deletedCommunity == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

}
