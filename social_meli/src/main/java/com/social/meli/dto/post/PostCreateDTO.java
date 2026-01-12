package com.social.meli.dto.post;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostCreateDTO {
    private String title;
    private String description;
    private Long profileId;
    private Boolean isPromo;
    private BigDecimal discount;
    private String imageUrl;
    private Long productId;
    private String productName;
}
