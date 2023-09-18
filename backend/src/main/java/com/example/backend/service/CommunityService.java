package com.example.backend.service;

import com.example.backend.dto.CommunityRequest;
import com.example.backend.entity.Community;
import com.example.backend.entity.Schedule;
import com.example.backend.entity.User;
import com.example.backend.repository.CommunityRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CommunityService {
    private final CommunityRepository communityRepository;
    private final UserService userService;

    public CommunityService(CommunityRepository communityRepository, UserService userService) {
        this.communityRepository = communityRepository;
        this.userService = userService;
    }

    public Community findPostById(Long communityId) {
        return communityRepository.findPostById(communityId);
    }
    public Community findPostByEmail(String email) {
        return communityRepository.findPostByEmail(email);
    }

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
}
