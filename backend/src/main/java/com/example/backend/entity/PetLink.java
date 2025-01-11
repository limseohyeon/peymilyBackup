package com.example.backend.entity;
import lombok.*;
import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(staticName = "build")
@Table(name = "PET_LINK_TBL")
@Data
@Builder
public class PetLink implements Serializable {

    @Id
    @GeneratedValue
    private Long linkId;
    private String owner;
    private String inviter;
    private Long petId;
    private String ownerName;

    public static PetLink build(User user, String owner, String inviter, Long petId, String ownerName) {
        return PetLink.builder()
                .owner(owner)
                .inviter(inviter)
                .petId(petId)
                .ownerName(ownerName)
                .build();
    }
}