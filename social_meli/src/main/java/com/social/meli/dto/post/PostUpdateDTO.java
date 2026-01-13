package com.social.meli.dto.post;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostUpdateDTO {
    private String title;
    private String description;
    private BigDecimal discount;
    private String imageUrl;
    private Boolean isPromo;
}
