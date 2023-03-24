package com.example.backend.repository;

import com.example.backend.entity.Pet;
import com.example.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetRepository extends JpaRepository<Pet, Long> {
    Pet findByPetName(String petName);
    //Pet findByInviterId(Long inviter);
}
