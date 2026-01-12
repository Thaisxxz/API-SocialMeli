package com.social.meli.dto.product;

import com.social.meli.model.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponseDTO {
    private Long id;
    private String name;
    private String description;
    private String type;
    private BigDecimal price;
    private LocalDateTime createdAt;

    public static ProductResponseDTO fromEntity(Product product) {
        return new ProductResponseDTO(product.getId(),
               product.getName(),
                product.getDescription(),
                product.getType(),
                product.getPrice(),
                product.getCreatedAt()
        );
    }
}
