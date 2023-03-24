package com.example.backend.service;

import com.example.backend.dto.PetRequest;
import com.example.backend.dto.UserRequest;
import com.example.backend.entity.Pet;
import com.example.backend.entity.User;
import com.example.backend.repository.PetRepository;
import com.example.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PetService {
    @Autowired
    private PetRepository repository;

    public Pet savePet(PetRequest petRequest) {
        Pet pet = Pet.build(
                petRequest.getUserId(),
                petRequest.getEmail(),
                petRequest.getPetName(),
                petRequest.getPetAge(),
                petRequest.getDetailInfo());

        return repository.save(pet);
    }

    public List<Pet> getAllPet() {
        return repository.findAll();
    }

    public Pet getPet(String petName) {
        return repository.findByPetName(petName);
    }

//    public Pet getPetByInviter(Long inviter) {
//        return repository.findByInviterId(inviter);
//    }
}
