package com.example.backend.repository;

import com.example.backend.entity.SharedPet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SharedPetRepository extends JpaRepository<SharedPet, Long> {

}
