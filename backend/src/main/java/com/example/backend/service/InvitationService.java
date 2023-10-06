package com.example.backend.service;

import com.example.backend.entity.Invitation;
import com.example.backend.repository.InvitationRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class InvitationService {

    private final InvitationRepository invitationRepository;

    public InvitationService(InvitationRepository invitationRepository) {
        this.invitationRepository = invitationRepository;
    }

    public List<Invitation> findAllInvitationByInviter(String inviter) {
        return invitationRepository.findAllInvitationByInviter(inviter);
    }

    public List<Invitation> findAllInvitationByReceiver(String receiver) {
        return invitationRepository.findAllInvitationByReceiver(receiver);
    }

    public List<Invitation> findAllInvitationByPetId(Long petId) {
        return invitationRepository.findAllInvitationByPetId(petId);
    }

    public Invitation saveInvitation(Invitation invitation) {
        return invitationRepository.save(invitation);
    }

    public List<Invitation> findSameInvitation(String inviter, String receiver, Long petId) {
        return invitationRepository.findSameInvitation(inviter, receiver, petId);
    }

    public List<Invitation> findAllInvitationByInviterAndReceiver(String inviter, String receiver) {
        List<Invitation> invitationFoundByInviter = findAllInvitationByInviter(inviter);
        List<Invitation> invitationFoundByReceiver = findAllInvitationByReceiver(receiver);
        // 반드시 초기화를 해줘야 add할 때 에러 안남
        List<Invitation> invitationFoundByInviterAndReceiver = new ArrayList<>();

        for (Invitation invitedPerson : invitationFoundByInviter) {
            for (Invitation receivedPerson : invitationFoundByReceiver) {
                if (invitedPerson.getInvitationId().equals(receivedPerson.getInvitationId())) {
                    invitationFoundByInviterAndReceiver.add(invitedPerson);
                }
            }
        }

        return invitationFoundByInviterAndReceiver;
    }

    public Invitation deleteInvitation(Invitation invitation) {
        Invitation toDelete = invitation;

        invitationRepository.deleteById(toDelete.getInvitationId());

        return toDelete;
    }
}