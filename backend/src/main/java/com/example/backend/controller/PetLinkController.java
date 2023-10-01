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
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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

//PetLink 생성하기
    @PostMapping("/post/{email}/{inviter}/{petId}")
    public ResponseEntity<PetLink> CreateLinkPet(@PathVariable ("email") String owner,
                                           @PathVariable("inviter") String inviter,
                                           @PathVariable("petId") Long petId) {
        try {
            if (petLinkService.isAvailablePetLink(owner, inviter, petId)) {
                PetLink petLink = PetLink.builder()
                        //.user(user)
                        .owner(owner)
                        .inviter(inviter)
                        .petId(petId)
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

//owner 를 이용해서 특정 petLink 찾아오기
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

//owner 를 이용해서 모든 petLink 찾아오기
    @GetMapping("/getAll/{owner}")
    public ResponseEntity<List<PetLink>> ReadAllLinkedPet(@PathVariable ("owner") String owner) {

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

//특정 petLink 수정하기
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
// 특정 petLink 삭제하기
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

//해당 petLink 양육자 읽기
//    @GetMapping("/rearer")

//부양육자 읽기
    @GetMapping("/sub-rearer/{petId}/{inviter}")
    public ResponseEntity<List<PetLink>> ReadSubRearer(@PathVariable("petId") Long petId,
                                                       @PathVariable("owner") String owner,
                                                       @PathVariable("inviter") String inviter
                                                       ){
        List <PetLink> petLinks = petLinkRepository.findAllPetLinks();
        List <PetLink> subRearerLinks=new ArrayList<>();
        if(!petLinks.isEmpty()){
            for(PetLink p : petLinks){
                if(p.getPetId().equals(petId)){
                    if(!p.getOwner().equals(inviter)){
                        subRearerLinks.add(p);
                    }
                }
            }
            return ResponseEntity.ok(subRearerLinks);
        }
        return ResponseEntity.notFound().build();

    }
}
