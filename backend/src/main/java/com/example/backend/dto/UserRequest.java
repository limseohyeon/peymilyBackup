package com.example.backend.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name="USERS_TBL")
@Data
@AllArgsConstructor(staticName = "build")
@NoArgsConstructor
@Builder
public class UserRequest {
//    @NotNull
    @Id
    private Long userId;
    @Email
    @NotNull(message = "invalid email address")
    private String email;
    private String password;
    @NotNull(message = "please input name")
    private String userName;
    @Pattern(regexp = "^\\d{11}$")
    private String phoneNumber;
    // 같은 inviter끼리 펫 정보 공유
    private String inviter;
    // private String token;
}
