package com.social.meli.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private Long sellerUserId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @ManyToOne
    @JoinColumn(name = "profile_id", nullable = false)
    private Profile profile;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(nullable = false)
    private LocalDateTime createdPostAt;

    @Column(nullable = false)
    @Builder.Default
    private Boolean isPromo= false;

    @Column
    private BigDecimal discount;

    @Column
    private String imageUrl;

    @PrePersist
    public void prePersist() {
        this.createdPostAt = LocalDateTime.now();
    }
}
