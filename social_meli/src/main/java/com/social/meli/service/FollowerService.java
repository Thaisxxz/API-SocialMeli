package com.social.meli.service;

import com.social.meli.dto.follower.FollowerResponseDTO;
import com.social.meli.exception.follower.BusinessException;
import com.social.meli.exception.follower.FollowerNotFoundException;
import com.social.meli.exception.profile.ProfileNotFoundException;
import com.social.meli.model.Follower;
import com.social.meli.model.Profile;
import com.social.meli.repository.FollowerRepository;
import com.social.meli.repository.ProfileRepository;
import com.social.meli.permission.profile.ProfilePermissionFactory;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FollowerService {
    private final FollowerRepository followerRepository;
    private final ProfileRepository profileRepository;
    private final ProfilePermissionFactory profilePermissionFactory;


    // Métodos auxiliares (privados)
    private Profile getProfileOrThrow(Long profileId) {
        return profileRepository.findById(profileId)
                .orElseThrow(() -> new ProfileNotFoundException("Perfil não encontrado: " + profileId));
    }

    private void assertCanFollow(Profile profile) {
        profilePermissionFactory.getProfile(profile.getType()).ensureCanFollow();
    }

    private void assertCanBeFollowed(Profile profile) {
        profilePermissionFactory.getProfile(profile.getType()).ensureCanBeFollowed();
    }
    private void assertFollowing(Long buyerProfileId, Long sellerProfileId) {
        followerRepository.findByFollower_IdAndSeller_Id(buyerProfileId, sellerProfileId)
                .orElseThrow(()-> new FollowerNotFoundException("Esse comprador não segue esse vendedor"));
    }
    private void assertNotFollowing(Long buyerProfileId, Long sellerProfileId) {
        followerRepository.findByFollower_IdAndSeller_Id(buyerProfileId, sellerProfileId)
                .ifPresent(follower -> {
                    throw new BusinessException("Este comprador já segue este vendedor.");});
    }

    public boolean isFollowing(Long buyerProfileId, Long sellerProfileId) {
        return followerRepository.existsByFollower_IdAndSeller_Id(buyerProfileId, sellerProfileId);
    }

    public void follow(Long buyerProfileId, Long sellerProfileId) {
        Profile buyerProfile = getProfileOrThrow(buyerProfileId);
        Profile sellerProfile = getProfileOrThrow(sellerProfileId);

        assertCanFollow(buyerProfile);
        assertCanBeFollowed(sellerProfile);
        assertNotFollowing(buyerProfileId, sellerProfileId);

        Follower follower = Follower.builder()
                .follower(buyerProfile)
                .seller(sellerProfile)
                .build();
        followerRepository.save(follower);
    }

    @Transactional
    public void unfollow(Long buyerProfileId, Long sellerProfileId) {
        assertFollowing(buyerProfileId, sellerProfileId);
        followerRepository.deleteByFollower_IdAndSeller_Id(buyerProfileId, sellerProfileId);
    }

    public List<FollowerResponseDTO> listFollowers(Long sellerProfileId) {
        List<Follower> followers = followerRepository.findBySeller_Id(sellerProfileId);
        return followers.stream()
                .map(FollowerResponseDTO::fromEntity)
                .toList();
    }

    public List<FollowerResponseDTO> listFollowing(Long buyerProfileId) {
        List<Follower> following = followerRepository.findByFollower_Id(buyerProfileId);
        return following.stream()
                .map(FollowerResponseDTO::fromEntity)
                .toList();
    }
    public long countFollowers(Long sellerProfileId) {
        return followerRepository.countBySeller_Id(sellerProfileId);
    }

    public long countFollowing(Long buyerProfileId) {
        return followerRepository.countByFollower_Id(buyerProfileId);
    }

}
