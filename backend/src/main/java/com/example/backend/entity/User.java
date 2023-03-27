package com.example.backend.entity;


import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "USERS_TBL")
@Data
@AllArgsConstructor(staticName = "build")
@NoArgsConstructor
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
    // 같은 inviter끼리 펫 정보 공유
    private String inviter;
}
