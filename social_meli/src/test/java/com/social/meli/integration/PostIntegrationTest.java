package com.social.meli.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.social.meli.ENUM.ProfileType;
import com.social.meli.dto.post.PostCreateDTO;
import com.social.meli.model.Category;
import com.social.meli.model.Post;
import com.social.meli.model.Product;
import com.social.meli.model.Profile;
import com.social.meli.model.User;
import com.social.meli.repository.CategoryRepository;
import com.social.meli.repository.FollowerRepository;
import com.social.meli.repository.PostRepository;
import com.social.meli.repository.ProductRepository;
import com.social.meli.repository.ProfileRepository;
import com.social.meli.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJson;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
        import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
        import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJson
@ActiveProfiles("test")
public class PostIntegrationTest {

    @Autowired MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    @Autowired UserRepository userRepository;
    @Autowired ProfileRepository profileRepository;
    @Autowired CategoryRepository categoryRepository;
    @Autowired ProductRepository productRepository;
    @Autowired PostRepository postRepository;
    @Autowired FollowerRepository followerRepository;

    private Profile sellerProfile;
    private Profile buyerProfile;
    private Product product;

    @BeforeEach
    void setup() {
        postRepository.deleteAll();
        followerRepository.deleteAll();
        productRepository.deleteAll();
        categoryRepository.deleteAll();
        profileRepository.deleteAll();
        userRepository.deleteAll();

        // category + product (obrigatório pro post)
        Category cat = categoryRepository.save(new Category(null, "Cat1", "Desc", true, null));
        product = productRepository.save(Product.builder()
                .name("Product 1")
                .type("Type")
                .description("Desc")
                .price(new BigDecimal("10.00"))
                .category(cat)
                .active(true)
                .build());

        // seller
        User sellerUser = userRepository.save(User.builder()
                .nickname("seller01")
                .password("x")
                .name("Seller User")
                .email("seller01@mail.com")
                .phone("11 99999-0002")
                .build());

        sellerProfile = profileRepository.save(Profile.builder()
                .type(ProfileType.SELLER)
                .active(true)
                .user(sellerUser)
                .build());

        // buyer
        User buyerUser = userRepository.save(User.builder()
                .nickname("buyer01")
                .password("x")
                .name("Buyer User")
                .email("buyer01@mail.com")
                .phone("11 99999-0001")
                .build());

        buyerProfile = profileRepository.save(Profile.builder()
                .type(ProfileType.BUYER)
                .active(true)
                .user(buyerUser)
                .build());

        // buyer follows seller (para timeline)
        // usa endpoint? aqui vamos direto no repository pela fixture rápida
        // (se preferir, eu mudo pra chamar POST /followers)
        followerRepository.save(com.social.meli.model.Follower.builder()
                .follower(buyerProfile)
                .seller(sellerProfile)
                .build());
    }

    @Test
    void US0005_createPost_shouldReturn201AndBody() throws Exception {
        PostCreateDTO body = new PostCreateDTO();
        body.setTitle("Title");
        body.setDescription("Desc");
        body.setProfileId(sellerProfile.getId());
        body.setIsPromo(true);
        body.setDiscount(new BigDecimal("10.00"));
        body.setImageUrl("http://img");
        body.setProductId(product.getId());

        mockMvc.perform(post("/posts")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith("application/json"))
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.title").value("Title"))
                .andExpect(jsonPath("$.description").value("Desc"))
                .andExpect(jsonPath("$.profileId").value(sellerProfile.getId()))
                .andExpect(jsonPath("$.sellerName").value("Seller User"))
                .andExpect(jsonPath("$.isPromo").value(true))
                .andExpect(jsonPath("$.discount").value(10.00))
                .andExpect(jsonPath("$.imageUrl").value("http://img"))
                .andExpect(jsonPath("$.productId").value(product.getId()))
                .andExpect(jsonPath("$.productName").value("Product 1"))
                .andExpect(jsonPath("$.createdPostAt").isNotEmpty());
    }

    @Test
    void createPost_shouldReturn401WhenProfileIsBuyer() throws Exception {
        PostCreateDTO body = new PostCreateDTO();
        body.setTitle("Title");
        body.setDescription("Desc");
        body.setProfileId(buyerProfile.getId()); // buyer não pode postar
        body.setProductId(product.getId());

        mockMvc.perform(post("/posts")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isUnauthorized());
    }
    @Test
    void T0005_invalidDateOrder_shouldReturn400_onTimeline() throws Exception {
        mockMvc.perform(get("/posts/timeline/{buyerProfileId}", buyerProfile.getId())
                        .param("order", "invalid_order"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Order inválido")));
    }
    @Test
    void T0005_invalidDateOrder_shouldReturn400_onPostsByProfile() throws Exception {
        mockMvc.perform(get("/posts/profile/{profileId}", sellerProfile.getId())
                        .param("order", "invalid_order"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Order inválido")));
    }
    @Test
    void createPost_shouldReturn404WhenProductNotFound() throws Exception {
        PostCreateDTO body = new PostCreateDTO();
        body.setTitle("Title");
        body.setDescription("Desc");
        body.setProfileId(sellerProfile.getId());
        body.setProductId(999999L);

        mockMvc.perform(post("/posts")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isNotFound());
    }
    @Test
    void US0006_timeline_shouldReturnOnlyLastTwoWeeksOrderedDesc() throws Exception {
        // Criar 3 posts do seller, 2 dentro de 2 semanas e 1 mais antigo
        Post p1 = postRepository.save(Post.builder()
                .title("newest")
                .description("d")
                .profile(sellerProfile)
                .product(product)
                .isPromo(false)
                .build());
        // atualiza data para agora
        p1.setCreatedPostAt(LocalDateTime.now().minusDays(1));
        postRepository.save(p1);

        Post p2 = postRepository.save(Post.builder()
                .title("middle")
                .description("d")
                .profile(sellerProfile)
                .product(product)
                .isPromo(false)
                .build());
        p2.setCreatedPostAt(LocalDateTime.now().minusDays(10));
        postRepository.save(p2);

        Post old = postRepository.save(Post.builder()
                .title("old")
                .description("d")
                .profile(sellerProfile)
                .product(product)
                .isPromo(false)
                .build());
        old.setCreatedPostAt(LocalDateTime.now().minusDays(30));
        postRepository.save(old);

        mockMvc.perform(get("/posts/timeline/{buyerProfileId}", buyerProfile.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/json"))
                .andExpect(jsonPath("$.length()").value(2))
                // ordenação desc: newest primeiro
                .andExpect(jsonPath("$[0].title").value("newest"))
                .andExpect(jsonPath("$[1].title").value("middle"))
                // não traz "old"
                .andExpect(jsonPath("$[*].title", not(hasItem("old"))));
    }

    @Test
    void findById_shouldReturn404WhenPostNotFound() throws Exception {
        mockMvc.perform(get("/posts/{id}", UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }
    @Test
    void US0009_promoByProfile_shouldSupportDateAscAndDesc() throws Exception {
        // cria 2 posts promo com datas diferentes
        Post p1 = postRepository.save(Post.builder()
                .title("olderPromo")
                .description("d")
                .profile(sellerProfile)
                .product(product)
                .isPromo(true)
                .build());
        p1.setCreatedPostAt(LocalDateTime.now().minusDays(10));
        postRepository.save(p1);

        Post p2 = postRepository.save(Post.builder()
                .title("newerPromo")
                .description("d")
                .profile(sellerProfile)
                .product(product)
                .isPromo(true)
                .build());
        p2.setCreatedPostAt(LocalDateTime.now().minusDays(1));
        postRepository.save(p2);

        // date_desc (default)
        mockMvc.perform(get("/posts/profile/{profileId}/promo", sellerProfile.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("newerPromo"))
                .andExpect(jsonPath("$[1].title").value("olderPromo"));

        // date_asc
        mockMvc.perform(get("/posts/profile/{profileId}/promo", sellerProfile.getId())
                        .param("order", "date_asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("olderPromo"))
                .andExpect(jsonPath("$[1].title").value("newerPromo"));
    }
}