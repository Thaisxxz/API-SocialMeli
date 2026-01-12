package com.social.meli.controller;


import com.social.meli.dto.user.LoginDTO;
import com.social.meli.dto.user.UserCreateDTO;
import com.social.meli.dto.user.UserResponseDTO;
import com.social.meli.service.UserService;
import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

//    Criar usuário: POST /users
    @PostMapping
public ResponseEntity<UserResponseDTO> createUser(@RequestBody UserCreateDTO userCreateDTO) {
       UserResponseDTO userResponseDTO = userService.createUser(userCreateDTO);
       return ResponseEntity.status(HttpStatus.CREATED).body(userResponseDTO);
    }
//    Buscar por ID: GET /users/{id}
    @GetMapping("/{id}")
public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        UserResponseDTO userResponseDTO= userService.findById(id);
        return ResponseEntity.ok(userResponseDTO);
    }
//    Login: POST /users/login
    @PostMapping("/login")
    public ResponseEntity<UserResponseDTO> login(@RequestBody LoginDTO loginDTO) {
        UserResponseDTO userResponseDTO= userService.login(loginDTO);
        return ResponseEntity.ok(userResponseDTO);
    }
//    Listar tosdos os usuarios
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<UserResponseDTO> userList = userService.findAll();
        return ResponseEntity.ok(userList);
    }

}
