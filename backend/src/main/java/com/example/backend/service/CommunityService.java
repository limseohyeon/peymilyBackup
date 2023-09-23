package com.example.backend.service;

import com.example.backend.dto.CommunityRequest;
import com.example.backend.entity.Community;
import com.example.backend.entity.Schedule;
import com.example.backend.entity.User;
import com.example.backend.repository.CommunityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommunityService {
    @Autowired
    private final CommunityRepository communityRepository;
    @Autowired
    private final UserService userService;

    public Community findPostById(Long communityId) {
        return communityRepository.findPostById(communityId);
    }
    public Community findPostByEmail(String email) {
        return communityRepository.findPostByEmail(email);
    }
    public List<Community> findAllPost() { return communityRepository.findAll(); }

    public Community saveCommunity(CommunityRequest communityRequest, String email) {
        Optional<User> optionalUser = userService.findByEmail(email);

        if (optionalUser.isPresent()) {
            Community community = Community.builder()
                    .email(email)
                    .likes(communityRequest.getLikes())
                    .title(communityRequest.getTitle())
                    .wrote(communityRequest.getWrote())
                    .date(communityRequest.getDate())
                    .build();

            return communityRepository.save(community);
        } else {
            throw new UsernameNotFoundException("Invalid community request");
        }
    }

    public Community updateCommunity(CommunityRequest communityRequest) {
        Community communityFound = communityRepository.findPostById(communityRequest.getCommunityId());
        Optional<Community> optionalCommunity = Optional.of(communityFound);

        if (!optionalCommunity.isEmpty()) {
            return communityRepository.save(communityFound);
        } else {
            throw new UsernameNotFoundException("Invalid community request");
        }
    }

    public Community deleteCommunity(Long communityId) {
        Community communityToDelete = findPostById(communityId);

        communityRepository.deleteByCommunityId(communityId);

        return communityToDelete;
    }
}
