package com.example.backend.controller;

import com.example.backend.dto.SharedPetRequest;
import com.example.backend.entity.SharedPet;
import com.example.backend.users.entity.User;
import com.example.backend.repository.SharedPetRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.service.SharedPetService;
import com.example.backend.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/shared", produces = MediaType.APPLICATION_JSON_VALUE)
public class SharedPetController {

    @Autowired
    private SharedPetRepository sharedPetRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private HttpServletRequest request;

    @Autowired
    private SharedPetService sharedPetService;

    @PostMapping("/add/{email}/{petName}")
    public ResponseEntity<SharedPet> savePet(@RequestBody @Valid SharedPetRequest sharedPetRequest,
                                             @PathVariable("email") String email,
                                             @PathVariable("petName") String petName) {
        try {
            String inviter = userRepository.findByEmail(email).get().getInviter();

            SharedPet sharedPet = sharedPetService.sharePet(sharedPetRequest, inviter, petName);
            return ResponseEntity.status(HttpStatus.CREATED).body(sharedPet);
        } catch (UsernameNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error occurred while saving pet.");
        }
    }

    @GetMapping("/get-all/{email}")
    public ResponseEntity<List<SharedPet>> getAllSharedPet(@PathVariable("email") String email) {

        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            String inviter = user.getInviter();
            List<SharedPet> sharedPets = sharedPetRepository.findByOwner(inviter);

            return ResponseEntity.ok(sharedPets);
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/get-N/{email}/{number}")
    public ResponseEntity<List<SharedPet>> getNSharedPet(@PathVariable("email") String email,
                                                         @PathVariable("number") Integer number) {

        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            // owner는 inviter 기준
            String inviter = user.getInviter();
            List<SharedPet> sharedPets = sharedPetService.getNSharedPet(inviter, number);

            return ResponseEntity.ok(sharedPets);
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/delete-shared/{email}/{sharedPetId}")
    @Transactional
    public ResponseEntity<SharedPet> deletePet(@PathVariable("email") String email,
                                         @PathVariable("sharedPetId") Long sharedPetId) {

        Optional<User> optionalUser = userRepository.findByEmail(email);
        List<SharedPet> listSharedPet = sharedPetRepository.findByOwner(optionalUser.get().getInviter());

        if (optionalUser.isPresent()) {
            for (SharedPet sharedPet : listSharedPet) {
                if (sharedPet.getSharedPetId().equals(sharedPetId)) {
                    sharedPetRepository.deleteSharedPetById(sharedPetId);
                    return ResponseEntity.ok(sharedPet);
                }
            }
            return ResponseEntity.notFound().build(); // 등록된 sharedPetId가 없는 경우 404 응답 반환

        }
        return ResponseEntity.notFound().build();
    }
}
