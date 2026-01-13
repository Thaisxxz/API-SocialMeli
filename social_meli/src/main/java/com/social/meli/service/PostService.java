package com.social.meli.service;

import com.social.meli.dto.post.PostCreateDTO;
import com.social.meli.dto.post.PostResponseDTO;
import com.social.meli.dto.post.PostUpdateDTO;
import com.social.meli.exception.post.PostNotFoundException;
import com.social.meli.exception.product.ProductNotFoundException;
import com.social.meli.exception.profile.ProfileNotFoundException;
import com.social.meli.model.Follower;
import com.social.meli.model.Post;
import com.social.meli.model.Product;
import com.social.meli.model.Profile;
import com.social.meli.permission.profile.ProfilePermissionFactory;
import com.social.meli.repository.FollowerRepository;
import com.social.meli.repository.PostRepository;
import com.social.meli.repository.ProductRepository;
import com.social.meli.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final ProfileRepository profileRepository;
    private final ProductRepository productRepository;
    private final ProfilePermissionFactory profilePermissionFactory;
    private final FollowerRepository followerRepository;

    private Profile getProfileOrThrow(Long profileId) {
        return profileRepository.findById(profileId)
                .orElseThrow(() -> new ProfileNotFoundException("Perfil não encontrado: " + profileId));
    }

    private void assertIsSeller(Profile profile) {
        profilePermissionFactory.getProfile(profile.getType()).ensureCanPost();
    }

    private Product getProductOrThrow(Long productId) {
        return Optional.ofNullable(productId)
                .flatMap(productRepository::findById)
                .orElseThrow(() -> new ProductNotFoundException("Produto não encontrado com o id: " + productId));
    }

    private Post getPostOrThrow(UUID id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException("Post não encontrado, ou excluído."));
    }

    public PostResponseDTO postCreate(PostCreateDTO postCreateDTO) {
        Profile postProfile = getProfileOrThrow(postCreateDTO.getProfileId());
        assertIsSeller(postProfile);

        Product postProduct = getProductOrThrow(postCreateDTO.getProductId());
        Post post = Post.builder()
                .title(postCreateDTO.getTitle())
                .description(postCreateDTO.getDescription())
                .profile(postProfile)
                .isPromo(postCreateDTO.getIsPromo())
                .discount(postCreateDTO.getDiscount())
                .imageUrl(postCreateDTO.getImageUrl())
                .product(postProduct)
                .build();

        post = postRepository.save(post);
        return PostResponseDTO.fromEntity(post);
    }

    public PostResponseDTO findPostById(UUID id) {
        Post post = getPostOrThrow(id);
        return PostResponseDTO.fromEntity(post);
    }

    public List<PostResponseDTO> findAllByProfile(Long profileId) {
        Profile profile = getProfileOrThrow(profileId);
        return postRepository.findAllByProfileOrderByCreatedPostAtDesc(profile)
                .stream()
                .map(PostResponseDTO::fromEntity)
                .toList();
    }

    public List<PostResponseDTO> findAllByIsPromo(Long profileId) {
        Profile profile = getProfileOrThrow(profileId);
        return postRepository.findByProfileAndIsPromoTrueOrderByCreatedPostAtDesc(profile)
                .stream()
                .map(PostResponseDTO::fromEntity)
                .toList();
    }
    public PostResponseDTO updatePost(UUID id, PostUpdateDTO postUpdateDTO) {
        Post post = getPostOrThrow(id);
        Optional.ofNullable(postUpdateDTO.getTitle()).ifPresent(post::setTitle);
        Optional.ofNullable(postUpdateDTO.getDescription()).ifPresent(post::setDescription);
        Optional.ofNullable(postUpdateDTO.getDiscount()).ifPresent(post::setDiscount);
        Optional.ofNullable(postUpdateDTO.getImageUrl()).ifPresent(post::setImageUrl);
        Optional.ofNullable(postUpdateDTO.getIsPromo()).ifPresent(post::setIsPromo);

        post = postRepository.save(post);
        return PostResponseDTO.fromEntity(post);
    }

    public List<PostResponseDTO> timeline(Long buyerprofileId) {
        List<Profile> sellers = followerRepository.findByFollower_Id(buyerprofileId)
                .stream()
                .map(Follower::getSeller)
                .toList();

        LocalDateTime dateMax = LocalDateTime.now().minusWeeks(2);

        return postRepository.findByProfileInAndCreatedPostAtAfterOrderByCreatedPostAtDesc(sellers, dateMax)
                .stream()
                .map(PostResponseDTO::fromEntity)
                .toList();
    }
    public void deletePost(UUID id) {
        Post post = getPostOrThrow(id);
        postRepository.delete(post);
    }

    public void inactivatePost(UUID id) {
        Post post = getPostOrThrow(id);
        post.setIsPromo(false);
        postRepository.save(post);
    }
}

