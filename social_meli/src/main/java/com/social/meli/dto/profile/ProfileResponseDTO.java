package com.social.meli.dto.profile;

import com.social.meli.ENUM.ProfileType;
import com.social.meli.model.Profile;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfileResponseDTO {
    private Long id;
    private ProfileType type;
    private Boolean active;
    private Long  userId;
    private String nickname;

    public static ProfileResponseDTO fromEntity(Profile profile) {
        return new ProfileResponseDTO(profile.getId(),
                profile.getType(),
                profile.getActive(),
                profile.getUser().getId(),
                profile.getUser().getNickname()
        );
    }
}
