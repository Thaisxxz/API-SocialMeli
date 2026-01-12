package com.social.meli.dto.profile;

import com.social.meli.ENUM.ProfileType;
import lombok.Data;

@Data
public class CreateProfileDTO {
    private ProfileType type;
    private Long  userId;
}
