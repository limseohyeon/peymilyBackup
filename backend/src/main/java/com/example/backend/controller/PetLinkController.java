package com.example.backend.controller;

import com.example.backend.dto.PetLinkRequest;
import com.example.backend.dto.PetRequest;
import com.example.backend.entity.PetLink;
import com.example.backend.repository.PetLinkRepository;
import com.example.backend.service.PetLinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/link", produces = MediaType.APPLICATION_JSON_VALUE)
public class PetLinkController {
    @Autowired
    private PetLinkRepository petLinkRepository;

    @Autowired
    private PetLinkService petLinkService;

    @PostMapping("/post/{owner}/{inviter}/{id}")
    public ResponseEntity<PetLink> LinkPet(@PathVariable ("owner") String owner,
                                           @PathVariable("inviter") String inviter,
                                           @PathVariable("id") Long id) {
        try {
            if (petLinkService.isAvailablePetLink(owner, inviter, id)) {
                PetLink newPetLink = new PetLink();

                newPetLink.setOwner(owner);
                newPetLink.setInviter(inviter);
                newPetLink.setPetId(id);

                PetLink savedPetLink = petLinkRepository.save(newPetLink);
                System.out.println("success");

                return new ResponseEntity<>(savedPetLink, HttpStatus.OK);
            } else {
                System.out.println("pet already exists");
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            System.out.println("unable to save pet. Error : " + e);

            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get/{owner}")
    public ResponseEntity<List<PetLink>> GetLinkedPet(@PathVariable ("owner") String owner) {

        List<PetLink> allPetLinked = new ArrayList<>();
        List<PetLink> allPetLinkedByOwner = petLinkRepository.findLinkByOwner(owner);
        List<PetLink> allPetLinkedByInviter = petLinkRepository.findLinkByInviter(owner);

        for (PetLink petLink : allPetLinkedByOwner) {
            if (petLink.getOwner().equals(owner)) {
                allPetLinked.add(petLink);
            }
        }

        for (PetLink petLink : allPetLinkedByInviter) {
            if (petLink.getInviter().equals(owner)) {
                allPetLinked.add(petLink);
            }
        }

        return ResponseEntity.ok(allPetLinked);
    }

    @PutMapping("/put/{linkId}")
    public ResponseEntity<PetLink> UpdateLinkedPet(@PathVariable("linkId") Long linkId,
                                                   @Valid @RequestBody PetLinkRequest updatedPetLinkData) {
        try {
            PetLink existingPetLink = petLinkRepository.findById(linkId).orElse(null);
            if (existingPetLink == null) {
                System.out.println("수정할 펫 링크를 찾을 수 없습니다");
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            existingPetLink.setOwner(updatedPetLinkData.getOwner());
            existingPetLink.setInviter(updatedPetLinkData.getInviter());

            PetLink updatedPetLink = petLinkRepository.save(existingPetLink);
            System.out.println("펫 링크 수정 성공");

            return new ResponseEntity<>(updatedPetLink, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println("펫 링크 수정 실패");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/{linkId}")
    public ResponseEntity<Void> DeleteLinkedPet(@PathVariable("linkId") Long linkId) {
        try {
            if (!petLinkRepository.existsById(linkId)) {
                System.out.println("삭제할 펫 링크를 찾을 수 없습니다");
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            petLinkRepository.deleteById(linkId);
            System.out.println("펫 링크 삭제 성공");

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            System.out.println("펫 링크 삭제 실패");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
