package com.social.meli.integration;

import com.social.meli.ENUM.ProfileType;
import com.social.meli.model.Profile;
import com.social.meli.model.User;
import com.social.meli.repository.CategoryRepository;
import com.social.meli.repository.FollowerRepository;
import com.social.meli.repository.ProfileRepository;
import com.social.meli.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
        import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")

public class FollowerIntegrationTest {

    @Autowired MockMvc mockMvc;

    @Autowired UserRepository userRepository;
    @Autowired ProfileRepository profileRepository;
    @Autowired FollowerRepository followerRepository;
    @Autowired CategoryRepository categoryRepository;

    private Profile buyerProfile;
    private Profile sellerProfile;

    @BeforeEach
    void setup() {
        followerRepository.deleteAll();
        profileRepository.deleteAll();
        userRepository.deleteAll();
        categoryRepository.deleteAll();

        // buyer user + profile
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

        // seller user + profile
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
    }

    @Test
    void US0001_followSeller_shouldReturn201_andIsFollowingTrue_andCountUpdates() throws Exception {
        // follow
        mockMvc.perform(post("/followers/{buyerProfileId}/following/{sellerProfileId}", buyerProfile.getId(), sellerProfile.getId()))
                .andExpect(status().isCreated());

        // isFollowing true
        mockMvc.perform(get("/followers/{buyerProfileId}/following/{sellerProfileId}", buyerProfile.getId(), sellerProfile.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        assertTrue(followerRepository.existsByFollower_IdAndSeller_Id(buyerProfile.getId(), sellerProfile.getId()));

        // count followers
        mockMvc.perform(get("/followers/{sellerProfileId}/followers/count", sellerProfile.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));

        // count following
        mockMvc.perform(get("/followers/{buyerProfileId}/following/count", buyerProfile.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));
    }
    @Test
    void T0001_follow_shouldReturn404WhenSellerDoesNotExist() throws Exception {
        mockMvc.perform(post("/followers/{buyerProfileId}/following/{sellerProfileId}",
                        buyerProfile.getId(), 999999L))
                .andExpect(status().isNotFound());
    }

    @Test
    void T0001_follow_shouldReturn404WhenBuyerDoesNotExist() throws Exception {
        mockMvc.perform(post("/followers/{buyerProfileId}/following/{sellerProfileId}",
                        999999L, sellerProfile.getId()))
                .andExpect(status().isNotFound());
    }
    @Test
    void T0003_unfollow_shouldReturn404WhenNotFollowing() throws Exception {
        // sem dar follow antes
        mockMvc.perform(delete("/followers/{buyerProfileId}/following/{sellerProfileId}",
                        buyerProfile.getId(), sellerProfile.getId()))
                .andExpect(status().isNotFound());
    }
    @Test
    void T0003_invalidNameOrder_shouldReturn400_listFollowers() throws Exception {
        mockMvc.perform(get("/followers/{sellerProfileId}/followers", sellerProfile.getId())
                        .param("order", "abc"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Order inválido")));
    }

    @Test
    void T0003_invalidNameOrder_shouldReturn400_listFollowing() throws Exception {
        mockMvc.perform(get("/followers/{buyerProfileId}/following", buyerProfile.getId())
                        .param("order", "abc"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Order inválido")));
    }

    @Test
    void US0007_unfollow_shouldReturn204_andIsFollowingFalse_andCountUpdates() throws Exception {
        // follow first
        mockMvc.perform(post("/followers/{buyerProfileId}/following/{sellerProfileId}", buyerProfile.getId(), sellerProfile.getId()))
                .andExpect(status().isCreated());

        // unfollow
        mockMvc.perform(delete("/followers/{buyerProfileId}/following/{sellerProfileId}", buyerProfile.getId(), sellerProfile.getId()))
                .andExpect(status().isNoContent());

        // isFollowing false
        mockMvc.perform(get("/followers/{buyerProfileId}/following/{sellerProfileId}", buyerProfile.getId(), sellerProfile.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));

        assertFalse(followerRepository.existsByFollower_IdAndSeller_Id(buyerProfile.getId(), sellerProfile.getId()));

        // counts 0
        mockMvc.perform(get("/followers/{sellerProfileId}/followers/count", sellerProfile.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string("0"));
    }

    @Test
    void US0002_US0003_listFollowers_shouldReturnList() throws Exception {
        // follow
        mockMvc.perform(post("/followers/{buyerProfileId}/following/{sellerProfileId}", buyerProfile.getId(), sellerProfile.getId()))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/followers/{sellerProfileId}/followers", sellerProfile.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/json"))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].follower.id").value(buyerProfile.getId()))
                .andExpect(jsonPath("$[0].seller.id").value(sellerProfile.getId()))
                .andExpect(jsonPath("$[0].followedAt").isNotEmpty());
    }

    @Test
    void US0004_listFollowing_shouldReturnList() throws Exception {
        // follow
        mockMvc.perform(post("/followers/{buyerProfileId}/following/{sellerProfileId}", buyerProfile.getId(), sellerProfile.getId()))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/followers/{buyerProfileId}/following", buyerProfile.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].seller.id").value(sellerProfile.getId()))
                .andExpect(jsonPath("$[0].follower.id").value(buyerProfile.getId()));
    }

    @Test
    void follow_shouldReturn208WhenAlreadyFollowing() throws Exception {
        mockMvc.perform(post("/followers/{buyerProfileId}/following/{sellerProfileId}", buyerProfile.getId(), sellerProfile.getId()))
                .andExpect(status().isCreated());

        // segunda tentativa -> BusinessException -> 208
        mockMvc.perform(post("/followers/{buyerProfileId}/following/{sellerProfileId}", buyerProfile.getId(), sellerProfile.getId()))
                .andExpect(status().isAlreadyReported());
    }

    @Test
    void follow_shouldReturn401WhenBuyerProfileIsSellerType() throws Exception {
        // seller tentando seguir alguém (SELLER não pode seguir)
        mockMvc.perform(post("/followers/{buyerProfileId}/following/{sellerProfileId}", sellerProfile.getId(), sellerProfile.getId()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void follow_shouldReturn401WhenSellerIsBuyerType() throws Exception {
        // criar outro buyer e tentar ser "seguido" (BUYER não pode ser seguido)
        User buyer2 = userRepository.save(User.builder()
                .nickname("buyer02")
                .password("x")
                .name("Buyer 2")
                .email("buyer02@mail.com")
                .phone("11 99999-0003")
                .build());

        Profile buyer2Profile = profileRepository.save(Profile.builder()
                .type(ProfileType.BUYER)
                .active(true)
                .user(buyer2)
                .build());

        mockMvc.perform(post("/followers/{buyerProfileId}/following/{sellerProfileId}", buyerProfile.getId(), buyer2Profile.getId()))
                .andExpect(status().isUnauthorized());
    }
    @Test
    void US0008_listFollowers_shouldSupportNameAscAndDesc() throws Exception {
        // cria mais um buyer com nickname menor que buyer01
        User buyer0 = userRepository.save(User.builder()
                .nickname("buyer00")
                .password("x")
                .name("Buyer 0")
                .email("buyer00@mail.com")
                .phone("11 99999-0099")
                .build());

        Profile buyer0Profile = profileRepository.save(Profile.builder()
                .type(ProfileType.BUYER)
                .active(true)
                .user(buyer0)
                .build());

        // buyer01 segue seller
        mockMvc.perform(post("/followers/{buyerProfileId}/following/{sellerProfileId}",
                        buyerProfile.getId(), sellerProfile.getId()))
                .andExpect(status().isCreated());

        // buyer00 segue seller
        mockMvc.perform(post("/followers/{buyerProfileId}/following/{sellerProfileId}",
                        buyer0Profile.getId(), sellerProfile.getId()))
                .andExpect(status().isCreated());

        // name_asc => buyer00, buyer01
        mockMvc.perform(get("/followers/{sellerProfileId}/followers", sellerProfile.getId())
                        .param("order", "name_asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].follower.nickname").value("buyer00"))
                .andExpect(jsonPath("$[1].follower.nickname").value("buyer01"));

        // name_desc => buyer01, buyer00
        mockMvc.perform(get("/followers/{sellerProfileId}/followers", sellerProfile.getId())
                        .param("order", "name_desc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].follower.nickname").value("buyer01"))
                .andExpect(jsonPath("$[1].follower.nickname").value("buyer00"));
    }
    @Test
    void US0008_listFollowing_shouldSupportNameAscAndDesc() throws Exception {
        // cria outro seller com nickname "seller00" (menor que seller01)
        User seller0 = userRepository.save(User.builder()
                .nickname("seller00")
                .password("x")
                .name("Seller 0")
                .email("seller00@mail.com")
                .phone("11 99999-0088")
                .build());

        Profile seller0Profile = profileRepository.save(Profile.builder()
                .type(ProfileType.SELLER)
                .active(true)
                .user(seller0)
                .build());

        // buyer segue seller01 (já existe no setup)
        mockMvc.perform(post("/followers/{buyerProfileId}/following/{sellerProfileId}",
                        buyerProfile.getId(), sellerProfile.getId()))
                .andExpect(status().isCreated());

        // buyer segue seller00
        mockMvc.perform(post("/followers/{buyerProfileId}/following/{sellerProfileId}",
                        buyerProfile.getId(), seller0Profile.getId()))
                .andExpect(status().isCreated());

        // name_asc => seller00, seller01
        mockMvc.perform(get("/followers/{buyerProfileId}/following", buyerProfile.getId())
                        .param("order", "name_asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].seller.nickname").value("seller00"))
                .andExpect(jsonPath("$[1].seller.nickname").value("seller01"));

        // name_desc => seller01, seller00
        mockMvc.perform(get("/followers/{buyerProfileId}/following", buyerProfile.getId())
                        .param("order", "name_desc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].seller.nickname").value("seller01"))
                .andExpect(jsonPath("$[1].seller.nickname").value("seller00"));
    }
}