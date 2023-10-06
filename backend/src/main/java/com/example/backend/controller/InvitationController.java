package com.example.backend.controller;

import com.example.backend.entity.Invitation;
import com.example.backend.entity.PetLink;
import com.example.backend.service.InvitationService;
import com.example.backend.service.PetLinkService;
import com.example.backend.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/invitation", produces = MediaType.APPLICATION_JSON_VALUE)
public class InvitationController {
    @Autowired
    private InvitationService invitationService;

    @Autowired
    private PetLinkService petLinkService;

    @PostMapping("/post/{inviter}/{receiver}/{petId}")
    public ResponseEntity<Invitation> PostInvitation(@PathVariable("inviter") String inviter,
                                           @PathVariable("receiver") String receiver,
                                           @PathVariable("petId") Long petId) {
        try {
            // 이미 초대장을 보낸 사람은 초대 불가
            List<Invitation> invitationFound = invitationService.findSameInvitation(inviter, receiver, petId);

            // invitationFound가 1 이상이면 이미 전에 보냈다는 소리이다
            if (invitationFound.size() < 1) {
                Invitation newInvitation = new Invitation();

                newInvitation.setInviter(inviter);
                newInvitation.setReceiver(receiver);
                newInvitation.setPetId(petId);

                Invitation savedInvitation = invitationService.saveInvitation(newInvitation);
                System.out.println("success");

                return new ResponseEntity<>(savedInvitation, HttpStatus.OK);
            } else {
                System.out.println("Already sent invitation");
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            System.out.println("unable to save invitation. Error : " + e);

            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get/{receiver}")
    public ResponseEntity<List<Invitation>> GetInvitation(@PathVariable("receiver") String receiver) {

        List<Invitation> allInvitation = invitationService.findAllInvitationByReceiver(receiver);

        return ResponseEntity.ok(allInvitation);
    }

    // putMapping은 생략

    // 받는 사람용. 추후 보내는 사람것도 필요하다면 추가
    @DeleteMapping("/delete/{inviter}/{receiver}/{petId}")
    public ResponseEntity<Invitation> DeleteInvitation(@PathVariable("inviter") String inviter,
                                                       @PathVariable("receiver") String receiver,
                                                       @PathVariable("petId") Long petId) {

        List<Invitation> invitationFound = invitationService.findAllInvitationByInviterAndReceiver(inviter, receiver);

        for (Invitation i : invitationFound) {
            if (i.getPetId().equals(petId)) {
                invitationService.deleteInvitation(i);

                return ResponseEntity.ok(i);
            }
        }

        return ResponseEntity.notFound().build();
    }
}
