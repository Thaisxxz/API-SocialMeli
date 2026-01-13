package com.social.meli.controller;

import com.social.meli.controller.docs.CategoryControllerDocs;
import com.social.meli.dto.product.category.CategoryResponseDTO;
import com.social.meli.dto.product.category.CreateCategoryDTO;
import com.social.meli.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController implements CategoryControllerDocs {
    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryResponseDTO> createCategory (@RequestBody CreateCategoryDTO createCategoryDTO) {
        CategoryResponseDTO createCategory = categoryService.createCategory(createCategoryDTO);
        return ResponseEntity.status(201).body(createCategory);
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponseDTO>> findAll() {
        List<CategoryResponseDTO> categoryList = categoryService.getAllCategories();
        return ResponseEntity.ok(categoryList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> getCategoryById(@PathVariable Long id) {
    CategoryResponseDTO categoryAll = categoryService.getCategoryById(id);
        return ResponseEntity.status(200).body(categoryAll);
    }

    @GetMapping("/search")
    public ResponseEntity<List<CategoryResponseDTO>> findAllByName(@RequestParam String name) {
        List<CategoryResponseDTO> categoryNameList = categoryService.findAllByName(name);
        return ResponseEntity.ok(categoryNameList);
    }
}
