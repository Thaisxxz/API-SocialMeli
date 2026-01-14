package com.social.meli.controller;

import com.social.meli.controller.docs.UserControllerDocs;
import com.social.meli.dto.user.LoginDTO;
import com.social.meli.dto.user.UserCreateDTO;
import com.social.meli.dto.user.UserResponseDTO;
import com.social.meli.service.UserService;
import jakarta.validation.Valid;
import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController implements UserControllerDocs {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserCreateDTO userCreateDTO) {
        UserResponseDTO userResponseDTO = userService.createUser(userCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(userResponseDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        UserResponseDTO userResponseDTO= userService.findById(id);
        return ResponseEntity.ok(userResponseDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponseDTO> login(@Valid @RequestBody LoginDTO loginDTO) {
        UserResponseDTO userResponseDTO= userService.login(loginDTO);
        return ResponseEntity.ok(userResponseDTO);
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<UserResponseDTO> userList = userService.findAll();
        return ResponseEntity.ok(userList);
    }
}
