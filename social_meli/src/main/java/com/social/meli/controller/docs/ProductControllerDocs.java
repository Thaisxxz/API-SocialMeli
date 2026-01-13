package com.social.meli.controller.docs;

import com.social.meli.dto.product.CreateProductDTO;
import com.social.meli.dto.product.ProductResponseDTO;
import com.social.meli.dto.product.ProductUpdateDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Produtos", description = "Operações relacionadas aos produtos")
public interface ProductControllerDocs {

    @Operation(summary = "Cria um novo produto")
    @ApiResponse(responseCode = "201", description = "Produto criado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos para criação do produto")
    @PostMapping
    ResponseEntity<ProductResponseDTO> createProduct(
            @Parameter(description = "Dados do novo produto", required = true)
            @RequestBody CreateProductDTO createProductDTO
    );

    @Operation(summary = "Busca produto por ID")
    @ApiResponse(responseCode = "200", description = "Produto retornado com sucesso")
    @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    @ApiResponse(responseCode = "400", description = "Parâmetro de ID inválido")
    @GetMapping("/{id}")
    ResponseEntity<ProductResponseDTO> findById(
            @Parameter(description = "ID do produto", required = true)
            @PathVariable Long id
    );

    @Operation(summary = "Lista todos os produtos ativos")
    @ApiResponse(responseCode = "200", description = "Lista de produtos ativos retornada")
    @GetMapping("/active")
    ResponseEntity<List<ProductResponseDTO>> findAllActive();

    @Operation(summary = "Lista produtos por categoria")
    @ApiResponse(responseCode = "200", description = "Produtos da categoria retornados")
    @ApiResponse(responseCode = "400", description = "Categoria informada inválida")
    @GetMapping("/category/{categoryId}")
    ResponseEntity<List<ProductResponseDTO>> findAllByCategory(
            @Parameter(description = "ID da categoria", required = true)
            @PathVariable Long categoryId
    );

    @Operation(summary = "Busca produtos por nome")
    @ApiResponse(responseCode = "200", description = "Produtos filtrados pelo nome retornados")
    @ApiResponse(responseCode = "400", description = "Parâmetro inválido (nome)")
    @GetMapping("/search")
    ResponseEntity<List<ProductResponseDTO>> findAllByName(
            @Parameter(description = "Nome do produto para busca", required = true)
            @RequestParam String name
    );

    @Operation(summary = "Atualiza dados de um produto")
    @ApiResponse(responseCode = "200", description = "Produto atualizado com sucesso")
    @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    @ApiResponse(responseCode = "400", description = "Dados inválidos para atualização")
    @PutMapping("/{id}")
    ResponseEntity<ProductResponseDTO> updateProduct(
            @Parameter(description = "ID do produto a ser atualizado", required = true)
            @PathVariable Long id,
            @Parameter(description = "Dados do produto para atualização", required = true)
            @RequestBody ProductUpdateDTO productUpdateDTO
    );

    @Operation(summary = "Inativa um produto")
    @ApiResponse(responseCode = "204", description = "Produto inativado com sucesso")
    @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    @ApiResponse(responseCode = "400", description = "Parâmetro de ID inválido")
    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteProduct(
            @Parameter(description = "ID do produto a ser inativado", required = true)
            @PathVariable Long id
    );
}