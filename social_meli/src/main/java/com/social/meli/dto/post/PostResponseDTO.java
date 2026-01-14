package com.social.meli.dto.post;

import com.social.meli.model.Post;
import com.social.meli.model.Product;
import com.social.meli.model.Profile;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    public static PostResponseDTO fromEntity(Post postDocument) {
        return new PostResponseDTO(
                postDocument.getId(),
                postDocument.getTitle(),
                postDocument.getDescription(),
                postDocument.getCreatedPostAt(),
                Optional.ofNullable(postDocument.getProfile())
                        .map(Profile::getId)
                        .orElse(null),
                Optional.ofNullable(postDocument.getProfile())
                        .map(profile -> profile.getUser() != null ? profile.getUser().getName() : null)
                        .orElse(null),
                postDocument.getIsPromo(),
                postDocument.getDiscount(),
                postDocument.getImageUrl(),
                Optional.ofNullable(postDocument.getProduct())
                        .map(Product::getId)
                        .orElse(null),
                Optional.ofNullable(postDocument.getProduct())
                        .map(Product::getName)
                        .orElse(null));
    }
}
