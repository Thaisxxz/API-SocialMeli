package com.social.meli.dto.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductUpdateDTO {

    private String name;
    private String type;
    private String description;
    private BigDecimal price;
    private Long categoryId;
}
