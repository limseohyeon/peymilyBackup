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
    public ResponseEntity<Pet> savePet(@RequestBody @Valid PetRequest petRequest, @PathVariable("inviter") String email) {
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

    @GetMapping("/{email}")
    public ResponseEntity<List<PetService>> getPet(@PathVariable("email") String email, Authentication authentication) {
        String currentUserEmail = authentication.getName(); // 현재 로그인한 사용자의 이메일
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            String inviter = user.getInviter();
            if (inviter.equals(currentUserEmail)) { // 현재 로그인한 사용자와 inviter가 같은 경우에만 조회
                List<Pet> pets = petRepository.findByInviter(inviter);
                List<PetService> petServices = pets.stream()
                        .map(pet -> modelMapper.map(pet, PetService.class))
                        .collect(Collectors.toList());
                return ResponseEntity.ok(petServices);
            }
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<PetService>> getPets() {
        List<Pet> pets = petRepository.findAll();
        List<PetService> petServices = pets.stream()
                .map(pet -> modelMapper.map(pet, PetService.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(petServices);
    }

    @PutMapping("/{petId}")
    public ResponseEntity<PetService> updatePet(@PathVariable("petId") Long petId, @RequestBody PetService petService) {
        Optional<Pet> optionalPet = petRepository.findById(petId);
        if (optionalPet.isPresent()) {
            Pet pet = optionalPet.get();
            modelMapper.map(petService, pet);
            Pet updatedPet = petRepository.save(pet);
            PetService updatedPetService = modelMapper.map(updatedPet, PetService.class);
            return ResponseEntity.ok(updatedPetService);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{petId}")
    public ResponseEntity<Void> deletePet(@PathVariable("petId") Long petId) {
        petRepository.deleteById(petId);
        return ResponseEntity.noContent().build();
    }
}

