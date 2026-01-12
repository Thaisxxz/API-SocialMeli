package com.social.meli.repository;

import com.social.meli.ENUM.ProfileType;
import com.social.meli.model.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile,Long> {

    List<Profile> findByUserId(Long userId);

    List<Profile> findByType(ProfileType type);

    List<Profile> findByActiveTrue();

//    Optional<Profile> findByIdAndActiveTrue(Long id);
}
