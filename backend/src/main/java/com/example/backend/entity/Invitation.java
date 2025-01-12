package com.example.backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Table(name = "INVITATION_TBL")
@Data
@NoArgsConstructor // 기본 생성자 생성 - builder랑 호환 안되고 builder 없애야 함
public class Invitation implements Serializable {
    @Id
    @GeneratedValue
    private Long invitationId;
    private String inviter;
    private String receiver;
    private Long petId;
}
