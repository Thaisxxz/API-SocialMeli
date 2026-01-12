package com.social.meli.dto.product.category;

import com.social.meli.model.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryResponseDTO {
    private Long id;
    private String name;
    private String description;

    public static CategoryResponseDTO fromEntity(Category category) {
        return new CategoryResponseDTO(category.getId(),
                category.getName(),
                category.getDescription());
    }
}
