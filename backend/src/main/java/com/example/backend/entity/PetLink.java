package com.example.backend.entity;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "PET_LINK_TBL")
@Data
@NoArgsConstructor // 기본 생성자 생성 - builder랑 호환 안되고 builder 없애야 함
public class PetLink implements Serializable {
    @Id
    @GeneratedValue
    private Long linkId;
    private String owner;
    private String inviter;
    private Long petId;
}