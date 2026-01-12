package com.social.meli.service;

import com.social.meli.dto.user.LoginDTO;
import com.social.meli.dto.user.UserCreateDTO;
import com.social.meli.dto.user.UserResponseDTO;
import com.social.meli.exception.user.EmailAlreadyExistsException;
import com.social.meli.exception.user.InvalidPasswordException;
import com.social.meli.exception.user.NicknameAlreadyExistsException;
import com.social.meli.exception.user.NicknameNotFoundException;
import com.social.meli.model.User;
import com.social.meli.repository.UserRepository;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponseDTO createUser(UserCreateDTO userCreateDTO) {


        userRepository.findByEmail(userCreateDTO.getEmail())
                .ifPresent(u -> {throw new EmailAlreadyExistsException("E-mail já cadastrado: " + userCreateDTO.getEmail());});

        userRepository.findByNickname(userCreateDTO.getNickname())
                .ifPresent(u -> {throw new NicknameAlreadyExistsException("Nickname já cadastrado: " + userCreateDTO.getNickname());});


        User user = new User();
        user.setEmail(userCreateDTO.getEmail());
        user.setNickname(userCreateDTO.getNickname());
        user.setPassword(passwordEncoder.encode(userCreateDTO.getPassword()));
        user.setPhone(userCreateDTO.getPhone());
        user.setName(userCreateDTO.getName());

        User saved = userRepository.save(user);
        return UserResponseDTO.fromEntity(saved);
    }

public UserResponseDTO findById(Long id) {
        User user= userRepository.findById(id)
    .orElseThrow(() -> new NoSuchElementException("Usuário não encontrado."));

    return UserResponseDTO.fromEntity(user);
}

 public UserResponseDTO login(LoginDTO loginDTO) {
        User user = userRepository.findByNickname(loginDTO.getNickname())
                .orElseThrow(() -> new NicknameNotFoundException(
                        "Usuário não encontrado com o nickname: " + loginDTO.getNickname()
                ));

        return Optional.of(user)
                .filter(u -> passwordEncoder.matches(loginDTO.getPassword(), u.getPassword()))
                .map(UserResponseDTO::fromEntity)
                .orElseThrow(() -> new InvalidPasswordException("Senha inválida"));
    }

 public List<UserResponseDTO> findAll() {
        List<User> userList = userRepository.findAll();
        List<UserResponseDTO> userResponseDTOList = userList.stream()
                .map(UserResponseDTO::fromEntity)
                .toList();
        return userResponseDTOList;
 }
}

