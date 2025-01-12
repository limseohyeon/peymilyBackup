package com.example.backend.users.entity;

import com.example.backend.dto.UserRequest;
import com.example.backend.entity.Pet;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "USERS_TBL", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"email"}),
        @UniqueConstraint(columnNames = {"userName"})
})
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
