package com.example.backend.service;

import com.example.backend.entity.PetLink;
import com.example.backend.repository.PetLinkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PetLinkService {
    private final PetLinkRepository petLinkRepository;

    @Autowired
    public PetLinkService(PetLinkRepository petLinkRepository) {
        this.petLinkRepository = petLinkRepository;
    }

    public List<PetLink> findAllLinkByOwner(String owner) {
        return petLinkRepository.findLinkByOwner(owner);
    }

    public List<PetLink> findAllLinkByInviter(String inviter) {
        return petLinkRepository.findLinkByInviter(inviter);
    }

    public List<PetLink> findAllLinkByPetId(Long petId) {
        return petLinkRepository.findLinkByPetId(petId);
    }

    public boolean isAvailablePetLink(String owner, String inviter, Long petId) {
        List<PetLink> ownerLink = findAllLinkByOwner(owner);
        List<PetLink> inviterLink = findAllLinkByInviter(inviter);
        List<PetLink> petIdLink = findAllLinkByPetId(petId);

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

    public List<PetLink> findAllLinkByOwnerAndPetId(String owner, Long petId) {
        List<PetLink> petLinkFoundByOwner = findAllLinkByOwner(owner);
        List<PetLink> petLinkFoundByPetId = findAllLinkByPetId(petId);
        // 반드시 초기화를 해줘야 add할 때 에러 안남
        List<PetLink> petLinkFoundByOwnerAndPetId = new ArrayList<>();

        for (PetLink linkedByPerson : petLinkFoundByOwner) {
            for (PetLink linkedByPet : petLinkFoundByPetId) {
                if (linkedByPerson.getLinkId() == linkedByPet.getLinkId()) {
                    petLinkFoundByOwnerAndPetId.add(linkedByPerson);
                }
            }
        }

        return petLinkFoundByOwnerAndPetId;
    }
}
