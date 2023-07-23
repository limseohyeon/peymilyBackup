package com.example.backend.controller;

import com.example.backend.dto.UserRequest;
import com.example.backend.entity.Pet;
import com.example.backend.entity.User;
import com.example.backend.repository.UserRepository;
import com.example.backend.service.AuthService;
import com.example.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import com.example.backend.service.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private AuthService authService;
    @Autowired
    private HttpServletRequest request;

    @GetMapping("/fetchAll")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

//    @GetMapping("/{userId}")
//    public ResponseEntity<User> getUser(@PathVariable String userId) {
//        return ResponseEntity.ok(userService.getUser(Long.parseLong(userId)));
//    }

    @GetMapping("/{email}")
    public ResponseEntity<User> getUser(@PathVariable("email") String email) {
        Optional<User> optionalUser = userService.findByEmail(email);
        String inviter = optionalUser.get().getInviter();

        if (optionalUser.isPresent()) {
            List<User> allUsers = userService.getAllUsers();

            for (User usr : allUsers) {
                if (usr.getEmail().equals(inviter)) {
                    return ResponseEntity.ok(usr);
                }
            }
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/current-user")
    public ResponseEntity<UserRequest> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            UserRequest userRequest = userService.getUserByUsername(username);
            return ResponseEntity.ok(userRequest);
        }
        return ResponseEntity.badRequest().build();
    }
}
