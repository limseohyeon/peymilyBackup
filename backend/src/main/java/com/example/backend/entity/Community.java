package com.example.backend.entity;

import com.example.backend.dto.CommunityRequest;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

import static java.util.stream.IntStream.builder;

@Entity
@Table(name = "COMMUNITY_TBL")
@Data
@AllArgsConstructor(staticName = "build")
@NoArgsConstructor
@Builder
public class Community implements Serializable {
    @Id
    @GeneratedValue
    private Long communityId;
    private String email;
    private Long likes;
    private String title;
    private String wrote;
    private String date;

    @ElementCollection
    private List<String> likedBy;
}