package com.example.backend.controller;

import com.example.backend.dto.PetLinkRequest;
import com.example.backend.entity.PetLink;
import com.example.backend.users.entity.User;
import com.example.backend.repository.PetLinkRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.service.PetLinkService;
import com.example.backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/link", produces = MediaType.APPLICATION_JSON_VALUE)
public class PetLinkController {
    @Autowired
    private PetLinkRepository petLinkRepository;
    @Autowired
    private PetLinkService petLinkService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

//PetLink 생성하기
    @PostMapping("/post/{email}/{inviter}/{petId}")
    public ResponseEntity<PetLink> CreateLinkPet(@PathVariable ("email") String owner,
                                           @PathVariable("inviter") String inviter,
                                           @PathVariable("petId") Long petId) {
        Optional<User> optionalUser = userRepository.findByEmail(owner);
        String ownerName = optionalUser.get().getUserName();
        try {
            if (petLinkService.isAvailablePetLink(owner, inviter, petId)) {
                PetLink petLink = PetLink.builder()
                        //.user(user)
                        .owner(owner)
                        .inviter(inviter)
                        .petId(petId)
                        .ownerName(ownerName)
                        .build();

                PetLink savedPetLink = petLinkRepository.save(petLink);
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

//DB에 저장된 모든 petLink 찾아오기
    @GetMapping("/getAll")
   public ResponseEntity<List<PetLink>> ReadAllPetLinks(){
        List <PetLink> petLinks = petLinkRepository.findAllPetLinks();
        if(!petLinks.isEmpty()){
            return ResponseEntity.ok(petLinks);
        }
        return ResponseEntity.notFound().build();
    }

//사용자가 포함된 petLink 중 특정 petLink 찾아오기
    @GetMapping("/get/{owner}/{petId}")
    public ResponseEntity <PetLink> ReadLinkedPet(@PathVariable("owner") String owner,
                                                  @PathVariable("petId") Long petId){
        List<PetLink> petLinks = petLinkRepository.findAllLinkByOwner(owner);
        for(PetLink petLink : petLinks){
            if(petLink.getPetId().equals(petId)){
                return  ResponseEntity.ok(petLink);
            }
        }
        return ResponseEntity.notFound().build();
    }

//사용자가 포함된 모든 petLink 찾아오기
    @GetMapping("/getAll/{owner}")
    public ResponseEntity<List<PetLink>> ReadAllLinkedPet(@PathVariable ("owner") String owner) {

        List<PetLink> allPetLinkedByOwner = petLinkRepository.findLinkByOwner(owner);

        return ResponseEntity.ok(allPetLinkedByOwner);
    }

//해당 pet 에 속한 모든 petLink 찾아오기
    @GetMapping("/rearer/{petId}")
    public ResponseEntity<List<PetLink>> ReadAllRearer(@PathVariable("petId") Long petId){
        List <PetLink> petLinks = petLinkRepository.findAllPetLinks();
        List <PetLink> allRearerLinks = new ArrayList<>();

        if(!petLinks.isEmpty()){
            for(PetLink p : petLinks){
                if(p.getPetId().equals(petId)){
                    allRearerLinks.add(p);
                }
            }
            return ResponseEntity.ok(allRearerLinks);
        }
        return ResponseEntity.notFound().build();
    }
    //해당 pet 에 속한 모든 petLink 삭제하기
    @DeleteMapping("/deleteAll/{petId}")
    public ResponseEntity<Void> deleteAllLinkedPet(@PathVariable("petId") Long petId) {
        List<PetLink> petLinks = petLinkRepository.findAll();
        List<PetLink> allPetLinks = new ArrayList<>();

        for (PetLink p : petLinks) {
            if (p.getPetId().equals(petId)) {
                allPetLinks.add(p);
            }
        }

        if (allPetLinks.isEmpty()) {
            System.out.println("삭제할 펫 링크를 찾을 수 없습니다");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        try {
            for (PetLink p : allPetLinks) {
                if (!petLinkRepository.existsById(p.getLinkId())) {
                    System.out.println("펫 링크 삭제 실패");
                    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
                }
                petLinkRepository.deleteById(p.getLinkId());
                System.out.println("펫 링크 삭제 성공");
            }
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            System.out.println("펫 링크 삭제 실패");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    //특정 petLink 수정하기
    @PutMapping("/put/{linkId}")
    public ResponseEntity<PetLink> UpdateLinkedPet(@PathVariable("linkId") Long linkId,
                                                   @Valid @RequestBody PetLinkRequest updatedPetLinkData) {
        try {
            PetLink existingPetLink = petLinkRepository.findById(linkId).orElse(null);
            if (existingPetLink == null) {
                System.out.println(linkId+"펫 링크를 찾을 수 없습니다");
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            existingPetLink.setOwner(updatedPetLinkData.getOwner());
            existingPetLink.setInviter(updatedPetLinkData.getInviter());
            existingPetLink.setOwnerName(updatedPetLinkData.getOwnerName());

            PetLink updatedPetLink = petLinkRepository.save(existingPetLink);
            System.out.println(linkId+"펫 링크 수정 성공");

            return new ResponseEntity<>(updatedPetLink, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(linkId+"펫 링크 수정 실패");
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
