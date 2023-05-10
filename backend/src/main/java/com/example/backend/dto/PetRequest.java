package com.example.backend.dto;

import com.example.backend.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

//@Entity
@Table(name="PET_TBL")
@Data
@AllArgsConstructor(staticName = "build")
@NoArgsConstructor
public class PetRequest {
    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private Long userId; // User 객체를 참조하는 필드
    @Column(unique = true)
    private String petName;
    private Integer petAge;
    private String detailInfo;
    private String inviter;
}
