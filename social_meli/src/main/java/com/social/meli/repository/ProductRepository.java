package com.social.meli.repository;

import com.social.meli.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategory_Id(Long categoryId);
    List<Product> findByActiveTrue();
    List<Product> findByNameContainingIgnoreCase(String name);
}

