package com.example.backend.entity;
import com.example.backend.users.entity.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;


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

    @OneToMany(mappedBy = "pet", cascade = CascadeType.ALL, orphanRemoval = true)

    public static PetLink build(User user, String owner, String inviter, Long petId, String ownerName) {
        return PetLink.builder()
                .owner(owner)
                .inviter(inviter)
                .petId(petId)
                .ownerName(ownerName)
                .build();
    }
}