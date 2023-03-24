package com.example.backend.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@Table(name = "PET_TBL")
@Data
@AllArgsConstructor(staticName = "build")
@NoArgsConstructor
public class Pet {
    @Id
    @GeneratedValue
    private Long userId;
    private String email;
    private String petName;
    private Integer petAge;
    private String detailInfo;
}
