package com.example.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor(staticName = "build")
@NoArgsConstructor
public class PetRequest {
    @NotNull
    private Long userId;
    @Email
    private String email;
    @NotNull(message = "펫 이름을 입력하세요")
    private String petName;
    private Integer petAge;
    private String detailInfo;
}
