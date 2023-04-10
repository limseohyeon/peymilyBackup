package com.example.backend.service;

import com.example.backend.dto.UserRequest;
import com.example.backend.entity.User;
import com.example.backend.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    private static final String SECRET_KEY = "secret";

    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username);
    }

    private String createToken(Map<String, Object> claims, String subject) {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + 1000 * 60 * 60 * 10); // 10 hours
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

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
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }

    public Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public User saveUser(UserRequest userRequest) {
        User user = User.build(0L,
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

    //public User getInviter(Long inviterId) { return repository.findByInviterId(inviterId); }
}
