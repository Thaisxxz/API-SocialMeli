package com.social.meli.dto.user;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@RequiredArgsConstructor

public class UserCreateDTO {
    @NotBlank @Email
    private String email;

    @NotBlank
    private String name;

    @NotBlank(message = "o nickname é obrigatório")
    @Size(min = 6, max = 18, message = "O nickname deve possuir de 06 a 18 caracteres.")
    private String nickname;

    @NotBlank(message = "O telefone é obrigatório")
    @Pattern(
            regexp = "^\\(?\\d{2}\\)?[\\s-]?\\d{4,5}-?\\d{4}$",
            message = "O telefone deve estar em um formato válido"
    )
    private String phone;

    @NotBlank @Size(min = 6)
    @Size(min = 6, max = 18, message = "A senha deve possuir de 06 a 18 caracteres.")
    private String password;

    @NotBlank @Size(min = 6)
    private String confirmPassword;



}
