package com.social.meli.service;

import com.social.meli.dto.follower.FollowerResponseDTO;
import com.social.meli.exception.follower.BusinessException;
import com.social.meli.exception.follower.FollowerNotFoundException;
import com.social.meli.exception.order.InvalidOrderException;
import com.social.meli.exception.profile.ProfileNotFoundException;
import com.social.meli.model.Follower;
import com.social.meli.model.Profile;
import com.social.meli.repository.FollowerRepository;
import com.social.meli.repository.ProfileRepository;
import com.social.meli.permission.profile.ProfilePermissionFactory;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class FollowerService {
    private final FollowerRepository followerRepository;
    private final ProfileRepository profileRepository;
    private final ProfilePermissionFactory profilePermissionFactory;
    private static final Set<String> NAME_ORDERS = Set.of("name_asc", "name_desc");

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

    private void validateNameOrder(String order) {
        if (order == null) return;

        if (!order.equalsIgnoreCase("name_asc") && !order.equalsIgnoreCase("name_desc")) {
            throw new InvalidOrderException("Order inválido: " + order + ". Use name_asc ou name_desc.");
        }
    }

    public List<FollowerResponseDTO> listFollowers(Long sellerProfileId, String order) {
        validateNameOrder(order);
        List<Follower> followers = followerRepository.findBySeller_Id(sellerProfileId,order);

        Comparator<Follower> byNickname =
                Comparator.comparing(f -> f.getFollower().getUser().getNickname(), String.CASE_INSENSITIVE_ORDER);

        Comparator<Follower> finalComparator =
                "name_desc".equalsIgnoreCase(order) ? byNickname.reversed() : byNickname; // default asc

        return followers.stream()
                .sorted(finalComparator)
                .map(FollowerResponseDTO::fromEntity)
                .toList();
    }

    public List<FollowerResponseDTO> listFollowing(Long buyerProfileId, String order) {
        validateNameOrder(order);
        List<Follower> following = followerRepository.findByFollower_Id(buyerProfileId,order);

        Comparator<Follower> byNickname =
                Comparator.comparing(f -> f.getSeller().getUser().getNickname(), String.CASE_INSENSITIVE_ORDER);

        Comparator<Follower> finalComparator =
                "name_desc".equalsIgnoreCase(order) ? byNickname.reversed() : byNickname; // default asc

        return following.stream()
                .sorted(finalComparator)
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
