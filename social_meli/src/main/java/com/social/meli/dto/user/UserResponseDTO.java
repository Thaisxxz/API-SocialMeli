package com.social.meli.dto.user;

import com.social.meli.model.User;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO {
    private Long id;
    private String nickname;
    private String email;
    private String phone;


    public static UserResponseDTO fromEntity(User user) {
        return new UserResponseDTO(user.getId(), user.getNickname(), user.getEmail(), user.getPhone());
    }
}
