package com.example.backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SharedPet {
    @Id
    @GeneratedValue
    private Long sharedPetId;
    private String owner;
    private String petName;
    private String date;
    private String hm;
    private String memo;
    private Long likes;

    public static SharedPet build(Long sharedPetId, String owner, String petName, String date,
                                  String hm, String memo, Long likes) {
        return SharedPet.builder()
                .sharedPetId(sharedPetId)
                .owner(owner)
                .petName(petName)
                .date(date)
                .hm(hm)
                .memo(memo)
                .likes(likes)
                .build();
    }
}
