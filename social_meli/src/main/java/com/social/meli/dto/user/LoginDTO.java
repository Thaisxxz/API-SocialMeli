package com.social.meli.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginDTO {

    @NotBlank
    @Size(min = 6)
    private String nickname;

    @NotBlank
    @Size(min = 6)
    private String password;
}
