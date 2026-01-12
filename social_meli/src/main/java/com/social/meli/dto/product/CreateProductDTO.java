package com.social.meli.dto.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateProductDTO {

    private String name;
    private String type;
    private String description;
    private BigDecimal price;
    private Long categoryId;
}
