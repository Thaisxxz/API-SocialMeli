package com.social.meli.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Follower {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn (name="follower_id", nullable = false)
    private Profile follower;

    @ManyToOne
    @JoinColumn (name="seller_id", nullable = false)
    private Profile seller;

    @Column(nullable = false)
    private LocalDateTime followedAt;

    @PrePersist
    public void prePersist() {
        this.followedAt = LocalDateTime.now();
    }
}
