package com.social.meli.repository;

import com.social.meli.model.ProductProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductProfileRepository extends JpaRepository<ProductProfile, Long> {
    List<ProductProfile> findByProfile_Id(Long profileId);

    List<ProductProfile> findByProduct_Id(Long productId);

    List<ProductProfile> findByActiveTrue();

    List<ProductProfile> findByProfile_IdAndActiveTrue(Long profileId);
}
