package com.example.backend.service;

import com.example.backend.entity.PetLink;
import com.example.backend.repository.PetLinkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PetLinkService {
    private final PetLinkRepository petLinkRepository;

    @Autowired
    public PetLinkService(PetLinkRepository petLinkRepository) {
        this.petLinkRepository = petLinkRepository;
    }

    public Optional<PetLink> findLinkByOwner(String owner) {
        return petLinkRepository.findLinkByOwner(owner);
    }

    public List<PetLink> findLinkByInviter(String owner, String inviter) {
        return petLinkRepository.findLinkByInviter(owner, inviter);
    }
}
