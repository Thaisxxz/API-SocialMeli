package com.social.meli.dto.follower;

import com.social.meli.dto.profile.ProfileResponseDTO;
import com.social.meli.model.Follower;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FollowerResponseDTO {
    private Long id;
    private ProfileResponseDTO follower;
    private ProfileResponseDTO seller;
    private LocalDateTime followedAt;


    public static FollowerResponseDTO fromEntity(Follower follower) {
        return new FollowerResponseDTO(follower.getId(),
                ProfileResponseDTO.fromEntity(follower.getFollower()),
                ProfileResponseDTO.fromEntity(follower.getSeller()),
                follower.getFollowedAt()
        );
    }
}