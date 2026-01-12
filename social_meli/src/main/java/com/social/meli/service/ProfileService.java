package com.social.meli.service;

import com.social.meli.ENUM.ProfileType;
import com.social.meli.dto.profile.CreateProfileDTO;
import com.social.meli.dto.profile.ProfileResponseDTO;
import com.social.meli.exception.profile.ProfileNotFoundException;
import com.social.meli.model.Profile;
import com.social.meli.model.User;
import com.social.meli.permission.profile.ProfilePermission;
import com.social.meli.permission.profile.ProfilePermissionFactory;
import com.social.meli.repository.ProfileRepository;
import com.social.meli.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;
    private final ProfilePermissionFactory profilePermissionFactory;


    public ProfileResponseDTO createProfile(CreateProfileDTO createProfileDTO) {
        User user = userRepository.findById(createProfileDTO.getUserId())
                .orElseThrow(() -> new ProfileNotFoundException("Usuário não encontrado"));
        ProfileType type = createProfileDTO.getType();
        ProfilePermission permission = profilePermissionFactory.getProfile(type);

        Profile profile = new Profile();
        profile.setUser(user);
        profile.setType(type);
        profile.setActive(true);

        profile = profileRepository.save(profile);
        return ProfileResponseDTO.fromEntity(profile);
    }

    public List<ProfileResponseDTO> findByUserId(Long user_Id) {
        List<Profile> profiles = profileRepository.findByUserId(user_Id);
        return profiles.stream()
                .map(ProfileResponseDTO::fromEntity)
                .toList();
    }

    public List<ProfileResponseDTO> findByType(ProfileType type) {
        List<Profile> profiles = profileRepository.findByType(type);
        return profiles.stream()
                .map(ProfileResponseDTO::fromEntity)
                .toList();
    }

    public List<ProfileResponseDTO> findAllActive() {
        List<Profile> profiles = profileRepository.findByActiveTrue();
        return profiles.stream()
                .map(ProfileResponseDTO::fromEntity)
                .toList();
    }
}
