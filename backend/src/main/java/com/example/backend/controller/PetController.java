package com.example.backend.controller;

import com.example.backend.dto.PetRequest;
import com.example.backend.entity.Pet;
import com.example.backend.entity.User;
import com.example.backend.repository.PetRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.service.PetService;
import com.example.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/pet")
@RequiredArgsConstructor
public class PetController {
    @Autowired
    private UserService userService;
    @Autowired
    private PetService petService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PetRepository petRepository;

    @PostMapping("/info")
    public ResponseEntity<Pet> saveUser(@RequestBody @Valid PetRequest petRequest) {
        return new ResponseEntity<>(petService.savePet(petRequest), HttpStatus.CREATED);
    }

//    @GetMapping("/all/{id}")
//    public ResponseEntity<Pet> getPetById(@PathVariable("id") Long id, @RequestParam("inviter") Long inviter) {
//        //User user = userService.getUser(id);
//        Pet pet = petService.getPetByInviter(inviter);
//        if (pet == null) {
//            return ResponseEntity.notFound().build();
//        }
//
//        if (!pet.getUserId().equals(inviter)) {
//            return ResponseEntity.badRequest().build();
//        }
//
//        return ResponseEntity.ok(pet);
//    }
}
