package com.example.backend.entity;

import com.example.backend.dto.UserRequest;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "USERS_TBL")
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
    private String token; // 새로운 필드 추가

    public static User build(Long userId, String email, String password, String userName, String phoneNumber, String inviter) {
        return User.builder()
                .userId(userId)
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
