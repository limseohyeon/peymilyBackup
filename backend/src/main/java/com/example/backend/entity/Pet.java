package com.example.backend.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@Table(name = "pet_tbl")
public class Pet {
    @Id
    @GeneratedValue
    private Integer userId;
    private String email;
    private String petName;
    private Integer petAge;
    private String detailInfo;
}
