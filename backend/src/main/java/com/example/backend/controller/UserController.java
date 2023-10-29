package com.example.backend.controller;

import com.example.backend.dto.UserRequest;
import com.example.backend.entity.Pet;
import com.example.backend.entity.User;
import com.example.backend.repository.UserRepository;
import com.example.backend.service.AuthService;
import com.example.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import com.example.backend.service.UserService;
import org.springframework.web.server.ResponseStatusException;

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

    @PutMapping("/update")
    public ResponseEntity<User> updateUser(@RequestBody @Valid UserRequest userRequest) {
        // email로 유저를 찾음
        Optional<User> userFound = userService.findByEmail(userRequest.getEmail());

        if (userFound.isEmpty()) {
            System.out.println("유저 정보를 찾을 수 없습니다.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        userService.updateUser(userRequest.getUserId(), userRequest.getUserName(), userRequest.getPhoneNumber());

        Optional<User> userUpdated = userService.findByEmail(userRequest.getEmail());

        if (userUpdated.isPresent()) {
            return ResponseEntity.ok(userUpdated.get());
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/delete/{email}")
    public ResponseEntity<User> deleteUser(@PathVariable("email") String email) {
        Optional<User> userToDelete = userService.findByEmail(email);

        userService.deleteUser(email);

        return ResponseEntity.status(HttpStatus.OK).body(userToDelete.get());
    }

}
