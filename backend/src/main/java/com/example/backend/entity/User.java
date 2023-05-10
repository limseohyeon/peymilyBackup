package com.example.backend.entity;

import com.example.backend.dto.UserRequest;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "USERS_TBL", uniqueConstraints = {@UniqueConstraint(columnNames = {"email", "userName"})})
@Builder
public class User {
    @Id
    @GeneratedValue
    @Column(unique = true)
    private Long userId;
    @Column(unique = true)
    private String email;
    private String password;
    private String userName;
    private String phoneNumber;
    private String inviter;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Pet> pets = new ArrayList<>();

    public static User build(String email, String password, String userName, String phoneNumber, String inviter) {
        return User.builder()
                .email(email)
                .password(password)
                .userName(userName)
                .phoneNumber(phoneNumber)
                .inviter(inviter)
                .build();
    }

    public UserRequest toUserRequest() {
        return UserRequest.builder()
                .userId(userId)
                .email(email)
                .password(password)
                .userName(userName)
                .phoneNumber(phoneNumber)
                .inviter(inviter)
                .build();
    }
}
