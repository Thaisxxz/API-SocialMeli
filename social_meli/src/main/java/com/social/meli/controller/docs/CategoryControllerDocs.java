package com.social.meli.controller.docs;

import com.social.meli.dto.product.category.CategoryResponseDTO;
import com.social.meli.dto.product.category.CreateCategoryDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Categorias", description = "Operações relacionadas a categorias de produtos")
public interface CategoryControllerDocs {

    @Operation(summary = "Cria uma nova categoria")
    @ApiResponse(responseCode = "201", description = "Categoria criada com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos para criação de categoria")
    @PostMapping
    ResponseEntity<CategoryResponseDTO> createCategory(
            @Parameter(description = "Dados da nova categoria", required = true)
            @RequestBody CreateCategoryDTO createCategoryDTO
    );

    @Operation(summary = "Lista todas as categorias")
    @ApiResponse(responseCode = "200", description = "Lista de categorias retornada com sucesso")
    @GetMapping
    ResponseEntity<List<CategoryResponseDTO>> findAll();

    @Operation(summary = "Busca categoria por ID")
    @ApiResponse(responseCode = "200", description = "Categoria encontrada")
    @ApiResponse(responseCode = "404", description = "Categoria não encontrada")
    @ApiResponse(responseCode = "400", description = "Parâmetro de ID inválido")
    @GetMapping("/{id}")
    ResponseEntity<CategoryResponseDTO> getCategoryById(
            @Parameter(description = "ID da categoria", required = true)
            @PathVariable Long id
    );

    @Operation(summary = "Busca categorias por nome")
    @ApiResponse(responseCode = "200", description = "Lista de categorias filtrada por nome")
    @ApiResponse(responseCode = "400", description = "Parâmetro inválido (nome)")
    @GetMapping("/search")
    ResponseEntity<List<CategoryResponseDTO>> findAllByName(
            @Parameter(description = "Texto a ser buscado no nome da categoria", required = true)
            @RequestParam String name
    );
}