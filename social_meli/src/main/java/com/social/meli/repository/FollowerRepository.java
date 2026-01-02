package com.social.meli.repository;

import com.social.meli.model.Follower;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FollowerRepository extends JpaRepository<Follower, Long> {
    List<Follower> findByFollower_Id(Long followerId);

    List<Follower> findBySeller_Id(Long sellerId);

    boolean existsByFollower_IdAndSeller_Id(Long followerId, Long sellerId);

    void deleteByFollower_IdAndSeller_Id(Long followerId, Long sellerId);
}
