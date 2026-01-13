package com.social.meli.controller.docs;

import com.social.meli.ENUM.ProfileType;
import com.social.meli.dto.profile.CreateProfileDTO;
import com.social.meli.dto.profile.ProfileResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Perfis", description = "Operações relacionadas aos perfis dos usuários")
public interface ProfileControllerDocs {

    @Operation(summary = "Cria um novo perfil")
    @ApiResponse(responseCode = "201", description = "Perfil criado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos para criação do perfil")
    @PostMapping
    ResponseEntity<ProfileResponseDTO> createProfile(
            @Parameter(description = "Dados para criação do perfil", required = true)
            @RequestBody CreateProfileDTO profileCreateDTO
    );

    @Operation(summary = "Lista todos os perfis de um usuário")
    @ApiResponse(responseCode = "200", description = "Perfis do usuário retornados")
    @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    @ApiResponse(responseCode = "400", description = "Parâmetro do usuário inválido")
    @GetMapping("user/{user_id}")
    ResponseEntity<List<ProfileResponseDTO>> listByUser(
            @Parameter(description = "ID do usuário", required = true)
            @PathVariable("user_id") Long userId
    );

    @Operation(summary = "Lista perfis por tipo")
    @ApiResponse(responseCode = "200", description = "Perfis filtrados por tipo")
    @ApiResponse(responseCode = "400", description = "Tipo de perfil inválido")
    @GetMapping("/type/{type}")
    ResponseEntity<List<ProfileResponseDTO>> getByType(
            @Parameter(description = "Tipo do perfil (vendedor, comprador, etc.)", required = true)
            @PathVariable ProfileType type
    );

    @Operation(summary = "Lista todos os perfis ativos")
    @ApiResponse(responseCode = "200", description = "Perfis ativos retornados")
    @GetMapping
    ResponseEntity<List<ProfileResponseDTO>> allActive();
}