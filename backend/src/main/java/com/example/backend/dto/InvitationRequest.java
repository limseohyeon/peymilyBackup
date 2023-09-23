package com.example.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor(staticName = "build")
@NoArgsConstructor
public class InvitationRequest {
    private Long invitationId;
    private String inviter;
    private String receiver;
    private Long petId;
}
