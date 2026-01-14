package com.social.meli.repository;

import com.social.meli.ENUM.ProfileType;
import com.social.meli.model.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProfileRepository extends JpaRepository<Profile,Long> {

    List<Profile> findByUserId(Long user_Id);

    List<Profile> findByType(ProfileType type);

    List<Profile> findByActiveTrue();
}
