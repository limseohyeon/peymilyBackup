package com.example.backend.controller;

import com.example.backend.dto.PetRequest;
import com.example.backend.dto.UserRequest;
import com.example.backend.entity.Pet;
import com.example.backend.entity.User;
import com.example.backend.repository.PetRepository;
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

    @PostMapping("/add/{inviter}")
    public ResponseEntity<Pet> savePet(@RequestBody @Valid PetRequest petRequest,
                                       @PathVariable("inviter") String email) {
        try {
            Pet savedPet = petService.savePet(petRequest, email);
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

    @GetMapping("/get-all/{email}")
    public ResponseEntity<List<Pet>> getPet(@PathVariable("email") String email) {
        //String currentUserEmail = authentication.getName(); // 현재 로그인한 사용자의 이메일
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            String inviter = user.getInviter();
            if (inviter.equals(email)) { // 현재 로그인한 사용자와 inviter가 같은 경우에만 조회
                List<Pet> pets = petRepository.findByInviter(inviter);

                return ResponseEntity.ok(pets);
            }
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/get-pet/{email}/{petName}")
    public ResponseEntity<Pet> getPet(@PathVariable("email") String email,
                                      @PathVariable("petName") String petName) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            String inviter = user.getInviter();
            if (inviter.equals(email)) {
                List<Pet> pets = petRepository.findByInviter(inviter);
                for (Pet pet : pets) {
                    if (pet.getPetName().equals(petName)) {
                        return ResponseEntity.ok(pet);
                    }
                }
                return ResponseEntity.notFound().build(); // 등록된 petName이 없는 경우 404 응답 반환
            }
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/put-pet/{email}/{petName}")
    public ResponseEntity<Pet> updatePet(@PathVariable("email") String email,
                                         @PathVariable("petName") String petName,
                                         @RequestBody Pet pet) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            List<Pet> pets = optionalUser.get().getPets();
            Optional<Pet> optionalPet = pets.stream().filter(p -> p.getPetName().equals(petName)).findFirst();
            if (optionalPet.isPresent()) {
                Pet existingPet = optionalPet.get();
                existingPet.setPetName(pet.getPetName());
                existingPet.setPetAge(pet.getPetAge());
                existingPet.setDetailInfo(pet.getDetailInfo());
                Pet updatedPet = petRepository.save(existingPet);
                return ResponseEntity.ok(updatedPet);
            }
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/delete-pet/{email}/{petName}")
    @Transactional
    public ResponseEntity<Pet> deletePet(@PathVariable("email") String email,
                                      @PathVariable("petName") String petName) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            String inviter = user.getInviter();
            if (inviter.equals(email)) {
                List<Pet> pets = petRepository.findByInviter(inviter);
                for (Pet pet : pets) {
                    if (pet.getPetName().equals(petName)) {
                        petRepository.deletePetByName(petName);
                        return ResponseEntity.ok(pet);
                    }
                }
                return ResponseEntity.notFound().build(); // 등록된 petName이 없는 경우 404 응답 반환
            }
        }
        return ResponseEntity.notFound().build();
    }
}

