package com.example.backend.dto;

import com.example.backend.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//@Entity
@Data
@AllArgsConstructor(staticName = "build")
@NoArgsConstructor
public class PetRequest {
    private Long userId; // User 객체를 참조하는 필드
    private String petName;
    private Integer petAge;
    private String detailInfo;
    private String inviter;
}