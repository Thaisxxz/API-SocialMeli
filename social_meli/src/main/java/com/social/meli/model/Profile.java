package com.social.meli.model;
import com.social.meli.ENUM.ProfileType;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProfileType type;

    @Column(nullable = false)
    private Boolean active= true;

    @ManyToOne
    @JoinColumn(name= "user_id", nullable = false)
    private User user;
}
