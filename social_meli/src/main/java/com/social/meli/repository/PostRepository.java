package com.social.meli.repository;

import com.social.meli.model.Post;
import com.social.meli.model.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.awt.print.Pageable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface PostRepository extends JpaRepository<Post, UUID> {

    List<Post> findAllByProfileOrderByCreatedPostAtDesc(Profile profile);
    List<Post> findByProfileInAndCreatedPostAtAfterOrderByCreatedPostAtDesc(List<Profile> profiles, LocalDateTime date);

    List<Post> findByProfileAndIsPromoTrueOrderByCreatedPostAtDesc(Profile profile);
}
