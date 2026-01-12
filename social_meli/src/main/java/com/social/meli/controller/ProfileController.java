package com.social.meli.controller;

import com.social.meli.ENUM.ProfileType;
import com.social.meli.dto.profile.CreateProfileDTO;
import com.social.meli.dto.profile.ProfileResponseDTO;
import com.social.meli.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/profiles")
public class ProfileController {
    private final ProfileService profileService;

    @PostMapping
    public ResponseEntity<ProfileResponseDTO> createProfile(@RequestBody CreateProfileDTO profileCreateDTO) {
        ProfileResponseDTO profileResponseDTO = profileService.createProfile(profileCreateDTO);
        return ResponseEntity.status(201).body(profileResponseDTO);
    }
    @GetMapping("user/{user_id}")
    public ResponseEntity<List<ProfileResponseDTO>> listByUser(@PathVariable Long userId) {
        List<ProfileResponseDTO> list = profileService.findByUserId(userId);
        return ResponseEntity.ok(list);
    }
    @GetMapping("/type/{type}")
    public ResponseEntity<List<ProfileResponseDTO>> getByType(@PathVariable ProfileType type) {
        List<ProfileResponseDTO> list = profileService.findByType(type);
        return ResponseEntity.ok(list);
    }
    @GetMapping
    public ResponseEntity<List<ProfileResponseDTO>> allActive() {
        List<ProfileResponseDTO> list = profileService.findAllActive();
        return  ResponseEntity.ok(list);
    }

}
