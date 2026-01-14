package com.social.meli.repository;

import com.social.meli.model.Follower;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FollowerRepository extends JpaRepository<Follower, Long> {
    List<Follower> findBySeller_Id(Long sellerId, String order);

    boolean existsByFollower_IdAndSeller_Id(Long followerId, Long sellerId);

    void deleteByFollower_IdAndSeller_Id(Long followerId, Long sellerId);

    Optional<Follower> findByFollower_IdAndSeller_Id(Long followerProfileId, Long sellerProfileId);

    long countBySeller_Id(Long sellerProfileId);

    long countByFollower_Id(Long buyerProfileId);

    List<Follower> findByFollower_Id(Long buyerProfileId, String order);
}
