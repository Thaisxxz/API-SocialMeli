package com.social.meli.dto.post;

import com.social.meli.model.Post;
import com.social.meli.model.Product;
import com.social.meli.model.Profile;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostResponseDTO {

    private UUID id;
    private String title;
    private String description;
    private LocalDateTime createdPostAt;
    private Long profileId;
    private String sellerName;
    private Boolean isPromo;
    private BigDecimal discount;
    private String imageUrl;
    private Long productId;
    private String productName;

    public static PostResponseDTO fromEntity(Post post) {
        return new PostResponseDTO(
                post.getId(),
                post.getTitle(),
                post.getDescription(),
                post.getCreatedPostAt(),
                Optional.ofNullable(post.getProfile())
                        .map(Profile::getId)
                        .orElse(null),
                Optional.ofNullable(post.getProfile())
                        .map(profile -> profile.getUser() != null ? profile.getUser().getName() : null)
                        .orElse(null),
                post.getIsPromo(),
                post.getDiscount(),
                post.getImageUrl(),
                Optional.ofNullable(post.getProduct())
                        .map(Product::getId)
                        .orElse(null),
                Optional.ofNullable(post.getProduct())
                        .map(Product::getName)
                        .orElse(null));
    }
}
