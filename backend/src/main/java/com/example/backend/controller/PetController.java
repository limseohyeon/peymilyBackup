package com.example.backend.controller;

import com.example.backend.dto.PetRequest;
import com.example.backend.dto.UserRequest;
import com.example.backend.entity.Pet;
import com.example.backend.entity.PetLink;
import com.example.backend.entity.Schedule;
import com.example.backend.entity.User;
import com.example.backend.repository.PetLinkRepository;
import com.example.backend.repository.PetRepository;
import com.example.backend.repository.ScheduleRepository;
import com.example.backend.service.PetLinkService;
import com.example.backend.service.PetService;
import com.example.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.modelmapper.ModelMapper;
import com.example.backend.repository.UserRepository;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/pet", produces = MediaType.APPLICATION_JSON_VALUE)
public class PetController {

    @Autowired
    private PetRepository petRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private PetService petService;
    @Autowired
    private PetLinkService petLinkService;
    @Autowired
    private PetLinkRepository petLinkRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @PostMapping("/add/{email}")
    public ResponseEntity<Pet> savePet(@RequestBody @Valid PetRequest petRequest,
                                       @PathVariable("email") String email) {
        try {
            Optional<User> user = userRepository.findByEmail(email);

            Pet savedPet = petService.savePet(petRequest, user.get().getInviter());


            return ResponseEntity.status(HttpStatus.CREATED).body(savedPet);
        } catch (UsernameNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error occurred while saving pet.");
        }
    }

    private String getCurrentUserInviter(HttpServletRequest request) {
        return request.getHeader("inviter");
    }
//      사용자가 속한 모든 펫계정 불러오기
    @GetMapping("/get-all/{email}")
    public ResponseEntity<List<Pet>> getAllPet(@PathVariable("email") String email) {
        //String currentUserEmail = authentication.getName(); // 현재 로그인한 사용자의 이메일
        Optional<User> optionalUser = userRepository.findByEmail(email);
        List<Pet> myPets = new ArrayList<>();

        if (optionalUser.isPresent()) {
                List<PetLink> petLinks = petLinkRepository.findAllLinkByOwner(email);
                System.out.println("petLinks : " + petLinks);

                for (PetLink petLink : petLinks) {
                    System.out.println("petLink : " + petLink);
                    myPets.add(petRepository.findByPetId(petLink.getPetId()));
                }

                if (myPets.equals(null)) {
                    System.out.println("펫을 찾을 수 없습니다.");
                    return ResponseEntity.notFound().build();
                }

                return ResponseEntity.ok(myPets);
        }
        return ResponseEntity.notFound().build();
    }

//    특정 펫 불러오기
    @GetMapping("/get-pet/{email}/{petId}")
    public ResponseEntity<Pet> getPet(@PathVariable("email") String email,
                                      @PathVariable("petId") Long petId) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if(optionalUser.isPresent()){
            List<PetLink> petLinks = petLinkRepository.findAllLinkByOwner(email);
            for(PetLink petLink : petLinks){
                if(petLink.getPetId().equals(petId)){
                    return  ResponseEntity.ok(petRepository.findByPetId(petId));
                }
            }
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.notFound().build();
    }

//      펫계정 수정
    @Transactional
    @PutMapping("/put-pet/{email}/{petId}")
    public ResponseEntity<Pet> updatePet(@PathVariable("email") String email,
                                         @PathVariable("petId") Long petId,
                                         @RequestBody Pet pet) {
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isPresent()) {
            Optional<Pet> optionalPet = petRepository.findById(petId);
            if (optionalPet.isPresent()) {
                Pet existingPet = optionalPet.get();

                // Pet 정보 업데이트
                existingPet.setPetName(pet.getPetName());
                existingPet.setPetAge(pet.getPetAge());
                existingPet.setDetailInfo(pet.getDetailInfo());

                // Schedules의 petName 업데이트
                List<Schedule> allSchedules = existingPet.getSchedules();
                if (allSchedules != null) {
                    for (Schedule sch : allSchedules) {
                        if (sch.getPet() != null) {
                            sch.getPet().setPetName(pet.getPetName());
                        }
                    }
                }

                // Pet와 Schedules 엔티티를 저장
                petRepository.save(existingPet);

                return ResponseEntity.ok(existingPet);
            }
        }
        return ResponseEntity.notFound().build();
    }


    @DeleteMapping("/delete-pet/{email}/{petId}")
    @Transactional
    public ResponseEntity<Pet> deletePet(@PathVariable("email") String email,
                                      @PathVariable("petId") Long petId) {

        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isPresent()) {
            List<PetLink> pets = petLinkRepository.findAllLinkByOwner(email);

            for (PetLink pet : pets) {
                if (pet.getPetId().equals(petId)) {
                    Optional<Pet> optionalPet = petRepository.findById(pet.getPetId());

                    Pet existingPet = optionalPet.get();
                    petRepository.deletePetByName(existingPet.getPetName());

                    return ResponseEntity.ok(existingPet);
                }
            }
            return ResponseEntity.notFound().build(); // 등록된 petName이 없는 경우 404 응답 반환
        }
        return ResponseEntity.notFound().build();
    }
}

