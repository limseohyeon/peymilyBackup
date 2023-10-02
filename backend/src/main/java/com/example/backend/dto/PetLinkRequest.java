package com.example.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor(staticName = "build")
@NoArgsConstructor
public class PetLinkRequest {
    private Long linkId;
    private String owner;
    private String inviter;
    private Long petId;
    private String ownerName;
}