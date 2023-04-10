package com.example.backend.service;

import com.example.backend.entity.User;
import com.example.backend.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.AuthenticationException;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;
    private final String secretKey;

    // 보안 키 (230410 추가)
    private static final String SECRET_KEY = "CL2xeP3cZ0MDZQDmuWeHPajwAJSPwtBk0JI5t6KCdGnK6ckXxx";

    public String login(String email, String password) throws AuthenticationException {
        User user = userRepository.findByEmail(email);
        if (user == null || !user.getPassword().equals(password)) {
            throw new BadCredentialsException("Invalid email or password");
        }
        Map<String, Object> claims = new HashMap<>();
        return createToken(email);
    }

    private String createToken(String email) {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + 1000 * 60 * 60 * 10); // 10 hours
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    // 230410 추가
    public AuthService() {
        // Generate a new secret key for JWT signing using HS256 algorithm
        SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        this.secretKey = Base64.getEncoder().encodeToString(key.getEncoded());
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
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }

    public Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }
}