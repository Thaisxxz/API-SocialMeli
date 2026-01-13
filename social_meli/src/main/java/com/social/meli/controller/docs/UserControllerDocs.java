package com.social.meli.controller.docs;

import com.social.meli.dto.user.LoginDTO;
import com.social.meli.dto.user.UserCreateDTO;
import com.social.meli.dto.user.UserResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Usuários", description = "Operações relacionadas a usuários")
public interface UserControllerDocs {

    @Operation(summary = "Cria um novo usuário")
    @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos para criação de usuário")
    @PostMapping
    ResponseEntity<UserResponseDTO> createUser(
            @Parameter(description = "Dados para criação do usuário", required = true)
            @RequestBody UserCreateDTO userCreateDTO
    );

    @Operation(summary = "Busca usuário por ID")
    @ApiResponse(responseCode = "200", description = "Usuário encontrado")
    @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    @ApiResponse(responseCode = "400", description = "ID inválido")
    @GetMapping("/{id}")
    ResponseEntity<UserResponseDTO> getUserById(
            @Parameter(description = "ID do usuário", required = true)
            @PathVariable Long id
    );

    @Operation(summary = "Login de usuário")
    @ApiResponse(responseCode = "200", description = "Login realizado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados de login inválidos")
    @ApiResponse(responseCode = "404", description = "Usuário não encontrado ou senha inválida")
    @PostMapping("/login")
    ResponseEntity<UserResponseDTO> login(
            @Parameter(description = "Dados para login", required = true)
            @RequestBody LoginDTO loginDTO
    );

    @Operation(summary = "Lista todos os usuários")
    @ApiResponse(responseCode = "200", description = "Lista de usuários retornada")
    @GetMapping
    ResponseEntity<List<UserResponseDTO>> getAllUsers();
}