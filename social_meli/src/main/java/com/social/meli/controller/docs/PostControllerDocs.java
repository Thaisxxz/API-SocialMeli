package com.social.meli.controller.docs;

import com.social.meli.dto.post.PostCreateDTO;
import com.social.meli.dto.post.PostResponseDTO;
import com.social.meli.dto.post.PostUpdateDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Posts", description = "Operações relacionadas às postagens dos perfis")
public interface PostControllerDocs {

    @Operation(summary = "Cria uma nova postagem")
    @ApiResponse(responseCode = "201", description = "Post criado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos para criação do post")
    @PostMapping
    ResponseEntity<PostResponseDTO> PostCreate(
            @Parameter(description = "Dados para criação do post. Campos obrigatórios validados.", required = true)
            @RequestBody @Valid PostCreateDTO postCreateDTO
    );

    @Operation(summary = "Buscar post por ID")
    @ApiResponse(responseCode = "200", description = "Post retornado com sucesso")
    @ApiResponse(responseCode = "404", description = "Post não encontrado")
    @ApiResponse(responseCode = "400", description = "Parâmetro de ID inválido")
    @GetMapping("/{id}")
    ResponseEntity<PostResponseDTO> findById(
            @Parameter(description = "ID do post", required = true)
            @PathVariable UUID id
    );

    @Operation(summary = "Lista todos os posts de um perfil")
    @ApiResponse(responseCode = "200", description = "Posts do perfil retornados")
    @ApiResponse(responseCode = "400", description = "Parâmetro do perfil inválido")
    @GetMapping("/profile/{profileId}")
    ResponseEntity<List<PostResponseDTO>> findAllByProfile(
            @Parameter(description = "ID do perfil", required = true)
            @PathVariable Long profileId,
            @RequestParam(required=false) String order
    );

    @Operation(summary = "Lista todos os posts promocionais de um perfil")
    @ApiResponse(responseCode = "200", description = "Posts promocionais retornados")
    @ApiResponse(responseCode = "400", description = "Parâmetro do perfil inválido")
    @GetMapping("/profile/{profileId}/promo")
    ResponseEntity<List<PostResponseDTO>> findAllByPromo(
            @Parameter(description = "ID do perfil", required = true)
            @PathVariable Long profileId,
            @RequestParam(required=false) String order

    );

    @Operation(summary = "Lista a timeline de um comprador")
    @ApiResponse(responseCode = "200", description = "Timeline retornada")
    @ApiResponse(responseCode = "400", description = "Parâmetro do comprador inválido")
    @GetMapping("/timeline/{buyerProfileId}")
    ResponseEntity<List<PostResponseDTO>> findAllByTimeLine(
            @Parameter(description = "ID do comprador", required = true)
            @PathVariable Long buyerProfileId,
            @RequestParam(required = false) String order
    );

    @Operation(summary = "Atualiza um post existente")
    @ApiResponse(responseCode = "200", description = "Post atualizado com sucesso")
    @ApiResponse(responseCode = "404", description = "Post não encontrado")
    @ApiResponse(responseCode = "400", description = "Dados inválidos para atualização")
    @PutMapping("/update/{id}")
    ResponseEntity<PostResponseDTO> update(
            @Parameter(description = "ID do post", required = true)
            @PathVariable UUID id,
            @Parameter(description = "Dados para atualização do post", required = true)
            @RequestBody PostUpdateDTO postUpdateDTO
    );

    @Operation(summary = "Remove um post")
    @ApiResponse(responseCode = "204", description = "Post removido com sucesso")
    @ApiResponse(responseCode = "404", description = "Post não encontrado")
    @ApiResponse(responseCode = "400", description = "Parâmetro de ID inválido")
    @DeleteMapping("/delete/{id}")
    ResponseEntity<Void> delete(
            @Parameter(description = "ID do post", required = true)
            @PathVariable UUID id
    );

    @Operation(summary = "Inativa um post existente")
    @ApiResponse(responseCode = "204", description = "Post inativado com sucesso")
    @ApiResponse(responseCode = "404", description = "Post não encontrado")
    @ApiResponse(responseCode = "400", description = "Parâmetro de ID inválido")
    @PatchMapping("/inactive/{id}")
    ResponseEntity<Void> inactive(
            @Parameter(description = "ID do post", required = true)
            @PathVariable UUID id
    );
}
