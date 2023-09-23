package com.example.backend.entity;

import com.example.backend.dto.CommunityRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

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
}