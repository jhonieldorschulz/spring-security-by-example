package br.com.bpkedu.spring_security_by_example.controller;

import br.com.bpkedu.spring_security_by_example.domain.Product;
import br.com.bpkedu.spring_security_by_example.repository.ProductRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@Tag(name = "Produtos", description = "Endpoints para gerenciamento de produtos (CRUD)")
@SecurityRequirement(name = "bearerAuth")
public class ProductController {

    private final ProductRepository productRepository;

    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping
    @Operation(
        summary = "Listar produtos",
        description = "Lista todos os produtos cadastrados. Acessível por qualquer usuário autenticado."
    )
    @ApiResponse(
        responseCode = "200",
        description = "Lista de produtos retornada com sucesso",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = Product.class),
            examples = @ExampleObject(
                value = """
                [
                  {
                    "id": 1,
                    "name": "Smartphone XYZ",
                    "description": "Último modelo com 256GB",
                    "price": 1999.90,
                    "quantity": 50
                  }
                ]
                """
            )
        )
    )
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Buscar produto",
        description = "Busca um produto pelo ID. Acessível por qualquer usuário autenticado."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Produto encontrado",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = Product.class),
                examples = @ExampleObject(
                    value = """
                    {
                      "id": 1,
                      "name": "Smartphone XYZ",
                      "description": "Último modelo com 256GB",
                      "price": 1999.90,
                      "quantity": 50
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Produto não encontrado",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                examples = @ExampleObject(
                    value = """
                    {
                      "timestamp": "2024-01-01T10:00:00.000+00:00",
                      "status": 404,
                      "error": "Not Found",
                      "message": "Produto não encontrado"
                    }
                    """
                )
            )
        )
    })
    public ResponseEntity<Product> getProductById(
            @Parameter(description = "ID do produto", example = "1")
            @PathVariable Long id) {
        return productRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
        summary = "Criar produto",
        description = "Cria um novo produto. Requer role ADMIN."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Produto criado com sucesso",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = Product.class),
                examples = @ExampleObject(
                    value = """
                    {
                      "id": 1,
                      "name": "Smartphone XYZ",
                      "description": "Último modelo com 256GB",
                      "price": 1999.90,
                      "quantity": 50
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Dados inválidos",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                examples = @ExampleObject(
                    value = """
                    {
                      "timestamp": "2024-01-01T10:00:00.000+00:00",
                      "status": 400,
                      "error": "Bad Request",
                      "message": "Validation failed",
                      "errors": [
                        {
                          "field": "name",
                          "message": "Nome é obrigatório"
                        },
                        {
                          "field": "price",
                          "message": "Preço deve ser maior que zero"
                        }
                      ]
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Acesso negado",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                examples = @ExampleObject(
                    value = """
                    {
                      "timestamp": "2024-01-01T10:00:00.000+00:00",
                      "status": 403,
                      "error": "Forbidden",
                      "message": "Access Denied"
                    }
                    """
                )
            )
        )
    })
    public ResponseEntity<Product> createProduct(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Dados do produto",
                required = true,
                content = @Content(
                    examples = @ExampleObject(
                        value = """
                        {
                          "name": "Smartphone XYZ",
                          "description": "Último modelo com 256GB",
                          "price": 1999.90,
                          "quantity": 50
                        }
                        """
                    )
                )
            )
            @Valid @RequestBody Product product) {
        Product savedProduct = productRepository.save(product);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedProduct.getId())
                .toUri();
        return ResponseEntity.created(location).body(savedProduct);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
        summary = "Atualizar produto",
        description = "Atualiza um produto existente. Requer role ADMIN."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Produto atualizado com sucesso",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = Product.class),
                examples = @ExampleObject(
                    value = """
                    {
                      "id": 1,
                      "name": "Smartphone XYZ Plus",
                      "description": "Versão atualizada com 512GB",
                      "price": 2499.90,
                      "quantity": 30
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Produto não encontrado"
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Acesso negado"
        )
    })
    public ResponseEntity<Product> updateProduct(
            @Parameter(description = "ID do produto", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody Product product) {
        return productRepository.findById(id)
                .map(existingProduct -> {
                    product.setId(id);
                    Product updatedProduct = productRepository.save(product);
                    return ResponseEntity.ok(updatedProduct);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
        summary = "Deletar produto",
        description = "Remove um produto. Requer role ADMIN."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Produto removido com sucesso"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Produto não encontrado"
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Acesso negado"
        )
    })
    public ResponseEntity<Void> deleteProduct(
            @Parameter(description = "ID do produto", example = "1")
            @PathVariable Long id) {
        return productRepository.findById(id)
                .map(product -> {
                    productRepository.delete(product);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
} 