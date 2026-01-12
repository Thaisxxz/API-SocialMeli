package com.social.meli.service;

import com.social.meli.dto.product.category.CategoryResponseDTO;
import com.social.meli.dto.product.category.CreateCategoryDTO;
import com.social.meli.exception.product.category.CategoryNotFoundException;
import com.social.meli.model.Category;
import com.social.meli.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryResponseDTO createCategory (CreateCategoryDTO createCategoryDTO) {
        Category category = new Category();
        category.setName(createCategoryDTO.getName());
        category.setDescription(createCategoryDTO.getDescription());
        category = categoryRepository.save(category);
        return CategoryResponseDTO.fromEntity(category);
    }

    public List<CategoryResponseDTO> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(CategoryResponseDTO::fromEntity)
                .toList();
    }

    public CategoryResponseDTO getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(()-> new CategoryNotFoundException("Categoria não encontrada com o ID:" + id));
        return CategoryResponseDTO.fromEntity(category);
    }

    public List<CategoryResponseDTO> findAllByName(String name) {
        Optional<Category> categoryList= categoryRepository.findByNameIgnoreCase(name);
        return categoryList.stream()
                .map(CategoryResponseDTO::fromEntity)
                .toList();
    }
}
