package com.example.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Entity
@Table(name="USERS_TBL")
@Data
@AllArgsConstructor(staticName = "build")
@NoArgsConstructor
public class UserRequest {
    @Id
//    @NotNull
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
}
