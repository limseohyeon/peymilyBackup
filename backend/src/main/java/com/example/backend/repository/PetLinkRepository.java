package com.example.backend.repository;

import com.example.backend.entity.PetLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PetLinkRepository extends JpaRepository<PetLink, Long> {
    //Optional<PetLink> findLinkByPetId(String petId);

    @Query("SELECT p FROM PetLink p WHERE p.owner = ?1")
    List<PetLink> findLinkByOwner(String owner);

    @Query("SELECT p FROM PetLink p WHERE p.inviter = ?1")
    List<PetLink> findLinkByInviter(String inviter);

    @Query("SELECT p FROM PetLink p WHERE p.petId = ?1")
    List<PetLink> findLinkByPetId(Long petId);

    //boolean isAvailablePetLink(String owner, String inviter);

    @Query("SELECT p FROM PetLink p WHERE p.owner = ?1")
    List<PetLink> findAllLinkByOwner(String owner);

    @Query("SELECT p FROM PetLink p WHERE p.owner = ?1 AND p.inviter = ?2 AND p.petId = ?3")
    List<PetLink> findSamePetLinks(String owner, String inviter, Long petId);

    //public List<PetLink> findAllLink(String owner, String inviter);
}