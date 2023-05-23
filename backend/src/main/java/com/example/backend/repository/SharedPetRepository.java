package com.example.backend.repository;

import com.example.backend.entity.SharedPet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SharedPetRepository extends JpaRepository<SharedPet, Long> {
    List<SharedPet> findByInviter(String inviter);

    @Modifying
    @Query("DELETE FROM SharedPet sp WHERE sp.sharedPetId = :sharedPetId")
    void deleteSharedPetById(@Param("sharedPetId") Long sharedPetId);
}
