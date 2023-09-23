package com.example.backend.service;

import com.example.backend.entity.User;
import com.example.backend.repository.UserRepository;
import com.example.backend.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.AuthenticationException;

import javax.crypto.SecretKey;
import java.util.*;
import java.util.function.Function;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Value("${jwt.secret}")
    private String secretKey;
    private Long expiredMs = 1000 * 60 * 60L;

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    private Set<String> tokenBlacklist = new HashSet<>();
    public String login(String email, String password) throws AuthenticationException {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (!user.getPassword().equals(password)) {
                throw new BadCredentialsException("Invalid email or password");
            }
            Map<String, Object> claims = new HashMap<>();
            return JwtUtil.createJwt(email, secretKey, expiredMs);
        }
        throw new BadCredentialsException("Invalid email or password");
    }

    public void logout(String token) throws AuthenticationException {
        // Remove the "Bearer " prefix from the token
        String authToken = token.replace("Bearer ", "");

        // Check if the token is valid and not already blacklisted
        if (isValidToken(authToken) && !tokenBlacklist.contains(authToken)) {
            // Blacklist the token to invalidate it
            tokenBlacklist.add(authToken);
        } else {
            throw new AuthenticationException("Invalid token or already logged out") {
            };
        }
    }

    private boolean isValidToken(String token) {
        // Check if the token is not expired and not in the blacklist
        Date expirationDate = getExpirationDateFromToken(token);
        return expirationDate != null && !tokenBlacklist.contains(token);
    }

    private String createToken(String email) {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + 1000 * 60 * 60 * 10); // 10 hours
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public String generateToken(String userName) {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + 1000 * 60 * 60 * 10); // 10 hours
        return Jwts.builder()
                .setSubject(userName)
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public String getSecretKey() {
        return secretKey;
    }

    public String getEmailFromToken(String token) {
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
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    }
}
