package com.example.backend.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
@Component
public class JwtFilter extends OncePerRequestFilter {

    private String secretKey = "CL2xeP3cZ0MDZQDmuWeHPajwAJSPwtBk0JI5t6KCdGnK6ckXxx";

    private final JwtConfig jwtConfig; // JwtConfig 주입
    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // "Bearer " 이후의 토큰 문자열만 추출
        }
        return null;
    }

    private String extractEmailFromToken(String token) {
        try {
            // 토큰에서 클레임 정보 추출
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            Claims claims = claimsJws.getBody();

            return claims.getSubject();
        } catch (Exception e) {
            System.out.println("failed to extract email. Error : " + e);

            return null;
        }
    }

    public boolean isValidToken(String token) {
        try {
            // 토큰 디코딩 및 유효성 검사
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            Date expirationDate = claims.getExpiration();
            Date now = new Date();
            if (expirationDate != null && expirationDate.before(now)) {
                System.out.println("Token Expired");
                return false;
            }

            return true;
        } catch (Exception e) {
            // 토큰 디코딩에 실패하거나 유효하지 않은 경우 예외 발생
            System.out.println("Token decoding failed or token is null. Error : " + e.getMessage());
            return false;
        }
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = extractToken(request);

        List<String> excludedPaths = Arrays.asList("/auth/", "/image-uploads/", "/profile/");
        if (token == null || excludedPaths.stream().anyMatch(path -> request.getRequestURI().equals(path))) {
            filterChain.doFilter(request, response);
            return;
        }

        if (token == null || token.isEmpty() || !isValidToken(token)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String email = extractEmailFromToken(token);

        // 권한 부여
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(email, null, List.of(new SimpleGrantedAuthority("USER")));
        // Detail을 넣어줍니다
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);
    }

}
