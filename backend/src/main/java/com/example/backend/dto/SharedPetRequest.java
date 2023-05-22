package com.example.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@AllArgsConstructor(staticName = "build")
@NoArgsConstructor
public class SharedPetRequest {
    private Long sharedPetId;
    private String owner;
    private String petName;
    private String date;
    private String hm;
    private String memo;
    private Long likes;
}
