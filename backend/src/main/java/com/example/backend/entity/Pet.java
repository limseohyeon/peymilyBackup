package com.example.backend.entity;

import lombok.*;

import javax.persistence.*;

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
    private Long petId;
    private String email;
    @Column(unique = true)
    private String petName;
    private Integer petAge;
    private String detailInfo;
    private String inviter;
}
