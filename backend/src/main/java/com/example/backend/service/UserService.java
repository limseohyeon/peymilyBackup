package com.example.backend.service;

import com.example.backend.dto.UserRequest;
import com.example.backend.entity.User;
import com.example.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    public User saveUser(UserRequest userRequest) {
        User user = User.build(0,
                userRequest.getEmail(),
                userRequest.getPassword(),
                userRequest.getUserName(),
                userRequest.getPhoneNumber(),
                userRequest.getInviter());

        return repository.save(user);
    }

    public List<User> getAllUsers() {
        return repository.findAll();
    }

    public User getUser(String userId) {
        return repository.findByUserId(userId);
    }
}
