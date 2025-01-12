package com.example.backend.controller;

import com.example.backend.dto.UserRequest;
import com.example.backend.users.entity.User;
import com.example.backend.service.AuthService;
import com.example.backend.service.AuthenticationResponse;
import com.example.backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.AuthenticationException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<User> saveUser(@RequestBody @Valid UserRequest userRequest) {
        User savedUser = userService.saveUser(userRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody Map<String, String> request) {
        try {
            String email = request.get("email");
            String password = request.get("password");
            String token = authService.login(email, password);
            AuthenticationResponse response = new AuthenticationResponse(token);
            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(@RequestHeader("Authorization") String token) {
        try {
            // Remove the "Bearer " prefix from the token
            String authToken = token.replace("Bearer ", "");
            authService.logout(authToken); // Implement logout logic in the AuthService
            Map<String, String> response = new HashMap<>();
            response.put("message", "Logout successful");
            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    //이메일 중복 확인
    @GetMapping("/{email}")
    public ResponseEntity<Boolean> overlapCheck(@PathVariable("email") String email) {

        List<User> allUsers = userService.getAllUsers();
        for(User u : allUsers){
            if(u.getEmail().equals(email)){
                return ResponseEntity.ok(false);
            }
        }
        return ResponseEntity.ok(true);
    }
}