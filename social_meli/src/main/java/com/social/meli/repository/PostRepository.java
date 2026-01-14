package com.social.meli.repository;

import com.social.meli.model.Post;
import com.social.meli.model.Profile;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface PostRepository extends JpaRepository<Post, UUID> {

    List<Post> findByProfile(Profile profile, Sort sort);
    List<Post> findByProfileInAndCreatedPostAtAfter(List<Profile> profiles, LocalDateTime date, Sort finalSort);
    List<Post> findByProfileAndIsPromoTrue(Profile profile, Sort Sort);
}
