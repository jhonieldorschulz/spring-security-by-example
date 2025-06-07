package br.com.bpkedu.spring_security_by_example.controller;

import br.com.bpkedu.spring_security_by_example.dto.AuthRequestDTO;
import br.com.bpkedu.spring_security_by_example.dto.TokenDTO;
import br.com.bpkedu.spring_security_by_example.service.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticação", description = "Endpoints para autenticação de usuários e gerenciamento de tokens")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public AuthController(AuthenticationManager authenticationManager, TokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    @PostMapping("/login")
    @Operation(
        summary = "Autenticar usuário",
        description = "Autentica um usuário com suas credenciais e retorna um token JWT. " +
                "O token deve ser incluído no header Authorization das requisições subsequentes."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Autenticação bem-sucedida",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = TokenDTO.class),
                examples = @ExampleObject(
                    value = """
                    {
                      "token": "eyJhbGciOiJIUzUxMiJ9...",
                      "type": "Bearer"
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Credenciais inválidas",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                examples = @ExampleObject(
                    value = """
                    {
                      "timestamp": "2024-01-01T10:00:00.000+00:00",
                      "status": 401,
                      "error": "Unauthorized",
                      "message": "Bad credentials"
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Dados de requisição inválidos",
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
                          "field": "username",
                          "message": "Username é obrigatório"
                        }
                      ]
                    }
                    """
                )
            )
        )
    })
    public ResponseEntity<TokenDTO> authenticateUser(
            @Valid @RequestBody 
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Credenciais do usuário",
                required = true,
                content = @Content(
                    examples = @ExampleObject(
                        value = """
                        {
                          "username": "admin",
                          "password": "admin"
                        }
                        """
                    )
                )
            ) AuthRequestDTO loginRequest) {
        
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(),
                loginRequest.getPassword()
            )
        );

        String jwt = tokenService.generateToken(authentication);
        return ResponseEntity.ok(new TokenDTO(jwt, "Bearer"));
    }
} 