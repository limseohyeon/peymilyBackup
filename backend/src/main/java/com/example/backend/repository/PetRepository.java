package com.example.backend.repository;

import com.example.backend.entity.Pet;
import com.example.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {
    Optional<Pet> findByPetName(String petName);

    @Query("SELECT p.petName FROM Pet p")
    List<String> findAllPetNames();

    List<Pet> findByInviter(String inviter);

    @Query("SELECT p FROM Pet p WHERE p.id = ?1")
    Pet findByPetId(Long petId);

    @Transactional
    @Modifying
    @Query("UPDATE Pet p SET p.petName = :newPetName WHERE p.petName = :oldPetName")
    int updatePetName(@Param("oldPetName") String oldPetName, @Param("newPetName") String newPetName);

    @Modifying
    @Query("DELETE FROM Pet p WHERE p.petName = :petName")
    void deletePetByName(@Param("petName") String petName);
}