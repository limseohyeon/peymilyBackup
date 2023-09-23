package com.example.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor(staticName = "build")
@NoArgsConstructor
@Builder
public class CommunityRequest {
    private Long communityId;
    private String email;
    private Long likes;
    private String title;
    private String wrote;
    private String date;
}
