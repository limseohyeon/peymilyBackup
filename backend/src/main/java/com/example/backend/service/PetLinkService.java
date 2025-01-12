package com.example.backend.service;

import com.example.backend.dto.PetRequest;
import com.example.backend.entity.Pet;
import com.example.backend.entity.PetLink;
import com.example.backend.users.entity.User;
import com.example.backend.repository.PetLinkRepository;
import com.example.backend.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PetLinkService {
    private final PetLinkRepository petLinkRepository;

    @Autowired
    public PetLinkService(PetLinkRepository petLinkRepository) {
        this.petLinkRepository = petLinkRepository;
    }

    @Autowired
    private UserService userService;
    @Autowired
    private PetService petService;
    @Autowired
    private PetRepository petRepository;

    public PetLink savePetLink(PetRequest petRequest, String email, Long petId){

        Optional<User> optionalUser = userService.findByEmail(email);
        Optional<Pet> optionalPet = petService.getPetById(petId);

        String owner = email;
        String inviter = optionalPet.get().getInviter();
        String ownerName = optionalUser.get().getUserName();

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            PetLink pet = PetLink.builder()
                    //.user(user)
                    .owner(owner)
                    .inviter(inviter)
                    .petId(petId)
                    .ownerName(ownerName)
                    .build();

            return petLinkRepository.save(pet);
        } else {
            throw new UsernameNotFoundException("Invalid username or password");
        }
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
        List<PetLink> foundPetLinks = petLinkRepository.findSamePetLinks(owner, inviter, petId);

        if (foundPetLinks.size() > 0) {
            return false;
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
                if (linkedByPerson.getLinkId().equals(linkedByPet.getLinkId())) {
                    petLinkFoundByOwnerAndPetId.add(linkedByPerson);
                }
            }
        }

        return petLinkFoundByOwnerAndPetId;
    }


}
