package com.social.meli.service;

import com.social.meli.dto.post.PostCreateDTO;
import com.social.meli.dto.post.PostResponseDTO;
import com.social.meli.dto.post.PostUpdateDTO;
import com.social.meli.exception.order.InvalidOrderException;
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
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final ProfileRepository profileRepository;
    private final ProductRepository productRepository;
    private final ProfilePermissionFactory profilePermissionFactory;
    private final FollowerRepository followerRepository;
    private static final Set<String> DATE_ORDERS = Set.of("name_asc", "name_desc");


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
    private void validateDateOrder(String order) {
        if (order == null) return;
        if (!order.equalsIgnoreCase("date_asc") && !order.equalsIgnoreCase("date_desc")) {
            throw new InvalidOrderException("Order inválido: " + order + ". Use date_asc ou date_desc.");
        }
    }
    public PostResponseDTO postCreate(PostCreateDTO postCreateDTO) {
        Profile postProfile = getProfileOrThrow(postCreateDTO.getProfileId());
        assertIsSeller(postProfile);

        Product postProduct = getProductOrThrow(postCreateDTO.getProductId());
        Post postDocument = Post.builder()
                .title(postCreateDTO.getTitle())
                .description(postCreateDTO.getDescription())
                .profile(postProfile)
                .isPromo(postCreateDTO.getIsPromo())
                .discount(postCreateDTO.getDiscount())
                .imageUrl(postCreateDTO.getImageUrl())
                .product(postProduct)
                .build();

        postDocument = postRepository.save(postDocument);
        return PostResponseDTO.fromEntity(postDocument);
    }

    public PostResponseDTO findPostById(UUID id) {
        Post postDocument = getPostOrThrow(id);
        return PostResponseDTO.fromEntity(postDocument);
    }

    public List<PostResponseDTO> findAllByProfile(Long profileId, String order) {
        Profile profile = getProfileOrThrow(profileId);
        validateDateOrder(order);

        Sort base = Sort.by("createdPostAt");
        Sort sort = "date_asc".equalsIgnoreCase(order) ? base.ascending() : base.descending();

        return postRepository.findByProfile(profile,sort)
                .stream()
                .map(PostResponseDTO::fromEntity)
                .toList();
    }

    public List<PostResponseDTO> findAllByIsPromo(Long profileId,String order) {
        Profile profile = getProfileOrThrow(profileId);
        validateDateOrder(order);
        Sort base = Sort.by("createdPostAt");
        Sort sort = "date_asc".equalsIgnoreCase(order) ? base.ascending() : base.descending();

        return postRepository.findByProfileAndIsPromoTrue(profile,sort)
                .stream()
                .map(PostResponseDTO::fromEntity)
                .toList();
    }

    public PostResponseDTO updatePost(UUID id, PostUpdateDTO postUpdateDTO) {
        Post postDocument = getPostOrThrow(id);
        Optional.ofNullable(postUpdateDTO.getTitle()).ifPresent(postDocument::setTitle);
        Optional.ofNullable(postUpdateDTO.getDescription()).ifPresent(postDocument::setDescription);
        Optional.ofNullable(postUpdateDTO.getDiscount()).ifPresent(postDocument::setDiscount);
        Optional.ofNullable(postUpdateDTO.getImageUrl()).ifPresent(postDocument::setImageUrl);
        Optional.ofNullable(postUpdateDTO.getIsPromo()).ifPresent(postDocument::setIsPromo);

        postDocument = postRepository.save(postDocument);
        return PostResponseDTO.fromEntity(postDocument);
    }

    public List<PostResponseDTO> timeline(Long buyerProfileId,String order) {
        validateDateOrder(order);
        List<Profile> sellers = followerRepository.findByFollower_Id(buyerProfileId,order)
                .stream()
                .map(Follower::getSeller)
                .toList();

        LocalDateTime dateMax = LocalDateTime.now().minusWeeks(2);

        Sort sort = Sort.by("createdPostAt");
        Sort finalSort = "date_asc".equalsIgnoreCase(order) ? sort.ascending() : sort.descending();

        return postRepository.findByProfileInAndCreatedPostAtAfter(sellers, dateMax, finalSort)
                .stream()
                .map(PostResponseDTO::fromEntity)
                .toList();
    }
    public void deletePost(UUID id) {
        Post postDocument = getPostOrThrow(id);
        postRepository.delete(postDocument);
    }

    public void inactivatePost(UUID id) {
        Post postDocument = getPostOrThrow(id);
        postDocument.setIsPromo(false);
        postRepository.save(postDocument);
    }
}

