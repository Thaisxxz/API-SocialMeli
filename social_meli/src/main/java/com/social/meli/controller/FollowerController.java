package com.social.meli.controller;

import com.social.meli.controller.docs.FollowerControllerDocs;
import com.social.meli.dto.follower.FollowerResponseDTO;
import com.social.meli.service.FollowerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/followers")
public class FollowerController implements FollowerControllerDocs {
    private final FollowerService followerService;

    @GetMapping("/{sellerProfileId}/followers/count")
    public ResponseEntity<Long> countFollowers(@PathVariable Long sellerProfileId) {
        long followerCount = followerService.countFollowers(sellerProfileId);
        return ResponseEntity.ok(followerCount);
    }

    @GetMapping("/{buyerProfileId}/following/count")
    public ResponseEntity<Long> countFollowing(@PathVariable Long buyerProfileId) {
        long followingCount = followerService.countFollowing(buyerProfileId);
        return ResponseEntity.ok(followingCount);
    }

    @GetMapping("/{buyerProfileId}/following/{sellerProfileId}")
    public ResponseEntity<Boolean> isFollowing(@PathVariable Long buyerProfileId, @PathVariable Long sellerProfileId) {
        boolean following = followerService.isFollowing(buyerProfileId, sellerProfileId);
        return ResponseEntity.ok(following);
    }

    @PostMapping("/{buyerProfileId}/following/{sellerProfileId}")
    public ResponseEntity<Void> follow(@PathVariable Long buyerProfileId, @PathVariable Long sellerProfileId) {
        followerService.follow(buyerProfileId, sellerProfileId);
        return ResponseEntity.status(201).build();

    }
    @DeleteMapping("/{buyerProfileId}/following/{sellerProfileId}")
    public ResponseEntity<Void> unfollow(@PathVariable Long buyerProfileId, @PathVariable Long sellerProfileId) {
        followerService.unfollow(buyerProfileId, sellerProfileId);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/{sellerProfileId}/followers")
    public ResponseEntity<List<FollowerResponseDTO>> listFollowers(@PathVariable Long sellerProfileId) {
        return ResponseEntity.ok(followerService.listFollowers(sellerProfileId));
    }
    @GetMapping("/{buyerProfileId}/following")
    public ResponseEntity<List<FollowerResponseDTO>> listFollowing(@PathVariable Long buyerProfileId) {
        return ResponseEntity.ok(followerService.listFollowing(buyerProfileId));
    }
}
