package com.example.backend.repository;

import com.example.backend.entity.Invitation;
import com.example.backend.entity.PetLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface InvitationRepository extends JpaRepository<Invitation, Long> {

    @Query("SELECT i FROM Invitation i WHERE i.inviter = ?1")
    List<Invitation> findAllInvitationByInviter(String inviter);

    @Query("SELECT i FROM Invitation i WHERE i.receiver = ?1")
    List<Invitation> findAllInvitationByReceiver(String receiver);

    @Query("SELECT i FROM Invitation i WHERE i.petId = ?1")
    List<Invitation> findAllInvitationByPetId(Long PetId);
}
