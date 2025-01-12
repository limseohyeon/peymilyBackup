package com.example.backend.service;

import com.example.backend.dto.CommunityRequest;
import com.example.backend.entity.Community;
import com.example.backend.users.entity.User;
import com.example.backend.repository.CommunityRepository;
import jakarta.persistence.EntityNotFoundException;
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

    // 230925 기준
    // 잘못 저장하고 있음. 수정 필요.
    public Community updateCommunity(CommunityRequest communityRequest) {
        Community communityFound = communityRepository.findPostById(communityRequest.getCommunityId());

        communityFound.setLikes(communityRequest.getLikes());
        communityFound.setTitle(communityRequest.getTitle());
        communityFound.setWrote(communityRequest.getWrote());

        return communityRepository.save(communityFound);
    }


    public void deleteCommunity(Long communityId) {
        Community communityToDelete = communityRepository.findPostById(communityId);
        if (communityToDelete == null) {
            throw new EntityNotFoundException("Community with ID " + communityId + " not found");
        }
        communityRepository.deleteByCommunityId(communityId);
    }

}
