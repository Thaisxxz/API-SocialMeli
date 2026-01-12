package com.social.meli.controller;

import com.social.meli.dto.product.CreateProductDTO;
import com.social.meli.dto.product.ProductResponseDTO;

import com.social.meli.dto.product.ProductUpdateDTO;
import com.social.meli.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductResponseDTO> createProduct(@RequestBody @Valid CreateProductDTO createProductDTO) {
        ProductResponseDTO productResponseDTO = productService.createProduct(createProductDTO);
        return ResponseEntity.status(201).body(productResponseDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> findById(@PathVariable Long id) {
        ProductResponseDTO productResponseDTO = productService.findById(id);
        return ResponseEntity.status(200).body(productResponseDTO);
    }

    @GetMapping("/active")
    public ResponseEntity<List<ProductResponseDTO>> findAllActive() {
        List<ProductResponseDTO> productActiveList = productService.findAllActive();
        return ResponseEntity.ok(productActiveList);
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<ProductResponseDTO>> findAllByCategory(@PathVariable Long categoryId) {
        List<ProductResponseDTO> productCategoryList = productService.findAllByCategory(categoryId);
        return ResponseEntity.ok(productCategoryList);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProductResponseDTO>> findAllByName(@RequestParam String name) {
        List<ProductResponseDTO> productNameList = productService.findAllByName(name);
        return ResponseEntity.ok(productNameList);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> updateProduct(@PathVariable Long id,
                                                            @RequestBody @Valid ProductUpdateDTO productUpdateDTO
    ) {
        ProductResponseDTO updateProduct = productService.updateProduct(id, productUpdateDTO);
        return ResponseEntity.status(200).body(updateProduct);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.inactivateProduct(id);
        return ResponseEntity.noContent().build();
    }
}
