package com.social.meli.service;


import com.social.meli.dto.product.CreateProductDTO;
import com.social.meli.dto.product.ProductResponseDTO;
import com.social.meli.dto.product.ProductUpdateDTO;
import com.social.meli.exception.product.ProductNotFoundException;
import com.social.meli.exception.product.category.CategoryNotFoundException;
import com.social.meli.model.Category;
import com.social.meli.model.Product;
import com.social.meli.repository.CategoryRepository;
import com.social.meli.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductResponseDTO createProduct(CreateProductDTO createProductDTO) {

        Category category = categoryRepository.findById(createProductDTO.getCategoryId())
                .orElseThrow(() -> new CategoryNotFoundException("Categoria não encontrada para o Id: " + createProductDTO.getCategoryId()));

        Product product = Product.builder()
                .name(createProductDTO.getName())
                .type(createProductDTO.getType())
                .description(createProductDTO.getDescription())
                .price(createProductDTO.getPrice())
                .category(category)
                .active(true)
                .build();
        product = productRepository.save(product);

        return ProductResponseDTO.fromEntity(product);
    }

    public ProductResponseDTO findById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Produto não encontrado para o id informado!"));

    return ProductResponseDTO.fromEntity(product);
    }

    public List<ProductResponseDTO> findAllActive() {
        List<Product> productsActive = productRepository.findByActiveTrue();
        return productsActive.stream()
                .map(ProductResponseDTO::fromEntity)
                .toList();
    }

    public List<ProductResponseDTO> findAllByCategory(Long categoryId) {
        List<Product> productsCategory = productRepository.findByCategory_Id(categoryId);
        return productsCategory.stream()
                .map(ProductResponseDTO::fromEntity)
                .toList();
    }

    public List<ProductResponseDTO> findAllByName(String name) {
        List<Product> products= productRepository.findByNameContainingIgnoreCase(name);
        return products.stream()
                .map(ProductResponseDTO::fromEntity)
                .toList();
    }

    @Transactional
    public ProductResponseDTO updateProduct(Long id, ProductUpdateDTO productUpdateDTO) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Produto não encontrado para o id: " + id));

        product.setName(productUpdateDTO.getName());
        product.setType(productUpdateDTO.getType());
        product.setDescription(productUpdateDTO.getDescription());
        product.setPrice(productUpdateDTO.getPrice());

        Optional.ofNullable(productUpdateDTO.getCategoryId())
                .filter(newCategoryId -> !newCategoryId.equals(product.getCategory().getId()))
                .map(newCategoryId -> categoryRepository.findById(newCategoryId)
                                .orElseThrow(() -> new CategoryNotFoundException("Categoria não encontrada")))
                .ifPresent(product::setCategory);

        productRepository.save(product);
        return ProductResponseDTO.fromEntity(product);
    }

    @Transactional
    public void inactivateProduct(Long id) {
        Product product = productRepository.findById(id)
                        .orElseThrow(() -> new ProductNotFoundException("Produto não encontrado com o id:" + id));

        product.setActive(false);
        productRepository.save(product);
    }
}
