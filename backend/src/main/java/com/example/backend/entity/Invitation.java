package com.example.backend.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
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
