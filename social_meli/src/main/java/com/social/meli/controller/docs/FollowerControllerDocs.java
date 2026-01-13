package com.social.meli.controller.docs;

import com.social.meli.dto.follower.FollowerResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Seguidores", description = "Operações relacionadas a seguidores e seguindo")
public interface FollowerControllerDocs {

    @Operation(summary = "Conta quantos seguidores um vendedor possui")
    @ApiResponse(responseCode = "200", description = "Contagem de seguidores retornada com sucesso")
    @ApiResponse(responseCode = "404", description = "Vendedor não encontrado")
    @ApiResponse(responseCode = "400", description = "Parâmetro do vendedor inválido")
    @GetMapping("/{sellerProfileId}/followers/count")
    ResponseEntity<Long> countFollowers(
            @Parameter(description = "ID do perfil do vendedor", required = true)
            @PathVariable Long sellerProfileId
    );

    @Operation(summary = "Conta quantos vendedores um comprador está seguindo")
    @ApiResponse(responseCode = "200", description = "Contagem de perfis seguindo retornada com sucesso")
    @ApiResponse(responseCode = "404", description = "Comprador não encontrado")
    @ApiResponse(responseCode = "400", description = "Parâmetro do comprador inválido")
    @GetMapping("/{buyerProfileId}/following/count")
    ResponseEntity<Long> countFollowing(
            @Parameter(description = "ID do perfil do comprador", required = true)
            @PathVariable Long buyerProfileId
    );

    @Operation(summary = "Verifica se comprador está seguindo um vendedor")
    @ApiResponse(responseCode = "200", description = "Indica se está seguindo ou não")
    @ApiResponse(responseCode = "404", description = "Comprador ou vendedor não encontrado")
    @ApiResponse(responseCode = "400", description = "Parâmetro inválido")
    @GetMapping("/{buyerProfileId}/following/{sellerProfileId}")
    ResponseEntity<Boolean> isFollowing(
            @Parameter(description = "ID do perfil do comprador", required = true)
            @PathVariable Long buyerProfileId,
            @Parameter(description = "ID do perfil do vendedor", required = true)
            @PathVariable Long sellerProfileId
    );

    @Operation(summary = "Seguir um vendedor (comprador começa a seguir vendedor)")
    @ApiResponse(responseCode = "201", description = "Seguiu com sucesso")
    @ApiResponse(responseCode = "404", description = "Comprador ou vendedor não encontrado")
    @ApiResponse(responseCode = "400", description = "Parâmetro inválido")
    @PostMapping("/{buyerProfileId}/following/{sellerProfileId}")
    ResponseEntity<Void> follow(
            @Parameter(description = "ID do perfil do comprador", required = true)
            @PathVariable Long buyerProfileId,
            @Parameter(description = "ID do perfil do vendedor", required = true)
            @PathVariable Long sellerProfileId
    );

    @Operation(summary = "Deixar de seguir um vendedor")
    @ApiResponse(responseCode = "204", description = "Deixou de seguir com sucesso")
    @ApiResponse(responseCode = "404", description = "Comprador ou vendedor não encontrado")
    @ApiResponse(responseCode = "400", description = "Parâmetro inválido")
    @DeleteMapping("/{buyerProfileId}/following/{sellerProfileId}")
    ResponseEntity<Void> unfollow(
            @Parameter(description = "ID do perfil do comprador", required = true)
            @PathVariable Long buyerProfileId,
            @Parameter(description = "ID do perfil do vendedor", required = true)
            @PathVariable Long sellerProfileId
    );

    @Operation(summary = "Listar todos os seguidores de um vendedor")
    @ApiResponse(responseCode = "200", description = "Lista de seguidores retornada")
    @ApiResponse(responseCode = "404", description = "Vendedor não encontrado")
    @ApiResponse(responseCode = "400", description = "Parâmetro do vendedor inválido")
    @GetMapping("/{sellerProfileId}/followers")
    ResponseEntity<List<FollowerResponseDTO>> listFollowers(
            @Parameter(description = "ID do perfil do vendedor", required = true)
            @PathVariable Long sellerProfileId
    );

    @Operation(summary = "Listar todos os vendedores que um comprador está seguindo")
    @ApiResponse(responseCode = "200", description = "Lista de seguindo retornada")
    @ApiResponse(responseCode = "404", description = "Comprador não encontrado")
    @ApiResponse(responseCode = "400", description = "Parâmetro do comprador inválido")
    @GetMapping("/{buyerProfileId}/following")
    ResponseEntity<List<FollowerResponseDTO>> listFollowing(
            @Parameter(description = "ID do perfil do comprador", required = true)
            @PathVariable Long buyerProfileId
    );
}