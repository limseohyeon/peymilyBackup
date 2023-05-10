package com.example.backend.service;

import com.example.backend.dto.UserRequest;
import com.example.backend.entity.User;
import com.example.backend.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;

@Service
public class UserService {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository repository;

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(authService.getSecretKey()).parseClaimsJws(token).getBody();
    }

    public Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public User saveUser(UserRequest userRequest) {
        User user = User.builder()
                .email(userRequest.getEmail())
                .password(userRequest.getPassword())
                .userName(userRequest.getUserName())
                .phoneNumber(userRequest.getPhoneNumber())
                .inviter(userRequest.getInviter())
                // .token(authService.generateToken(userRequest.getUserName()))
                .build();

        return repository.save(user);
    }

    public List<User> getAllUsers() {
        return repository.findAll();
    }

    public User getUser(Long userId) {
        return repository.findByUserId(userId);
    }

    public UserRequest getUserByUsername(String userName) {
        User user = repository.findByUserName(userName);
        if (user == null) {
            throw new UsernameNotFoundException("Invalid username or password");
        }
        return UserRequest.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .userName(user.getUserName())
                .phoneNumber(user.getPhoneNumber())
                .inviter(user.getInviter())
                .build();
    }

    public Optional<User> findByEmail(String email) { return repository.findByEmail(email); }

    //public User getInviter(Long inviterId) { return repository.findByInviterId(inviterId); }
}
