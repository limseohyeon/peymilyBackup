package com.example.backend.service;

import com.example.backend.entity.PetLink;
import com.example.backend.repository.PetLinkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class PetLinkService {
    private final PetLinkRepository petLinkRepository;

    @Autowired
    public PetLinkService(PetLinkRepository petLinkRepository) {
        this.petLinkRepository = petLinkRepository;
    }

    public List<PetLink> findLinkByOwner(String owner) {
        return petLinkRepository.findLinkByOwner(owner);
    }

    public List<PetLink> findLinkByInviter(String inviter) {
        return petLinkRepository.findLinkByInviter(inviter);
    }

    public List<PetLink> findLinkByPetId(Long petId) {
        return petLinkRepository.findLinkByPetId(petId);
    }

    public boolean isAvailablePetLink(String owner, String inviter, Long petId) {
        List<PetLink> ownerLink = findLinkByOwner(owner);
        List<PetLink> inviterLink = findLinkByInviter(inviter);
        List<PetLink> petIdLink = findLinkByPetId(petId);

        if (petIdLink.isEmpty()) {
            System.out.println("new pet saved");
            return true;
        }

        Set<PetLink> petIdSet = new HashSet<>(petIdLink);

        for (PetLink ownerPetLink : ownerLink) {
            if (petIdSet.contains(ownerPetLink)) {
                return false;
            }
        }

        for (PetLink inviterPetLink : inviterLink) {
            if (petIdSet.contains(inviterPetLink)) {
                return false;
            }
        }

        return true;
    }
}
