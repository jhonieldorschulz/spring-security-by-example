# Spring Security Demo

Este projeto demonstra a implementação de segurança em uma aplicação Spring Boot usando Spring Security. O projeto inclui autenticação, autorização baseada em roles, criptografia de senha e uma interface web moderna.

## 🔐 Mecanismos de Segurança

### Autenticação
- Implementação customizada do `UserDetailsService`
- Autenticação baseada em formulário (form login)
- Senhas criptografadas usando BCrypt
- Sessão gerenciada pelo Spring Security
- Página de login personalizada

### Autorização
- Controle de acesso baseado em roles (RBAC)
- Dois níveis de acesso:
  - `ROLE_USER`: Acesso básico
  - `ROLE_ADMIN`: Acesso administrativo
- Proteção de rotas por anotações e configuração

### Configurações de Segurança
```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    // Configurações principais de segurança
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) {
        http
            .authorizeHttpRequests()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/login").permitAll()
                .anyRequest().authenticated()
            .and()
            .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/home")
            .and()
            .logout()
                .logoutSuccessUrl("/login");
    }
}
```

### Criptografia de Senha
- Utilização do `BCryptPasswordEncoder`
- Senhas armazenadas de forma segura no banco de dados
- Salt automático para cada hash

## 🚀 Como Executar

1. Clone o repositório:
```bash
git clone [URL_DO_REPOSITÓRIO]
```

2. Entre no diretório do projeto:
```bash
cd spring-security-by-example
```

3. Execute o projeto:
```bash
./mvnw spring-boot:run
```

4. Acesse a aplicação:
```
http://localhost:8080
```

## 👥 Usuários para Teste

### Administrador
- Username: `admin`
- Password: `admin`
- Role: `ROLE_ADMIN`
- Acesso a todas as funcionalidades, incluindo área administrativa

### Usuário Comum
- Username: `user`
- Password: `user`
- Role: `ROLE_USER`
- Acesso apenas às funcionalidades básicas

## 🗺️ Mapa de URLs

| URL | Acesso | Descrição |
|-----|---------|------------|
| `/` | Autenticado | Redireciona para home |
| `/login` | Público | Página de login |
| `/home` | Autenticado | Dashboard principal |
| `/admin` | ROLE_ADMIN | Área administrativa |
| `/logout` | Autenticado | Realiza logout |

## 🏗️ Estrutura do Projeto

```
src/main/java/
└── br.com.bpkedu.spring_security_by_example/
    ├── config/
    │   └── SecurityConfig.java         # Configurações de segurança
    ├── controller/
    │   ├── HomeController.java        # Controller da página principal
    │   └── LoginController.java       # Controller de autenticação
    ├── model/
    │   └── User.java                  # Entidade de usuário
    ├── repository/
    │   └── UserRepository.java        # Repositório JPA
    └── service/
        └── UserDetailsServiceImpl.java # Implementação do UserDetailsService
```

## 🔒 Fluxo de Autenticação

1. Usuário acessa uma URL protegida
2. Spring Security redireciona para `/login`
3. Usuário submete credenciais
4. `UserDetailsServiceImpl` valida as credenciais
5. Se válido:
   - Usuário é redirecionado para `/home`
   - Sessão é criada
6. Se inválido:
   - Usuário retorna para `/login` com mensagem de erro

## 🛡️ Características de Segurança

- **CSRF Protection**: Habilitada por padrão
- **Session Management**: Configuração padrão do Spring Security
- **Password Encoding**: BCrypt com força 10
- **Error Handling**: Mensagens de erro customizadas
- **Logout Handler**: Invalidação de sessão automática

## 📝 Templates

- **Login**: Template customizado com Thymeleaf e TailwindCSS
- **Home**: Dashboard responsivo com informações do usuário
- **Admin**: Área restrita para administradores

## ⚙️ Configurações Técnicas

### Application Properties
```properties
# Database
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver

# JPA
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=update

# Security
spring.security.user.name=admin
spring.security.user.password=admin
```

## 🧪 Testando a API REST

### 1. Autenticação

Primeiro, você precisa obter um token JWT autenticando com suas credenciais:

```bash
# Como admin
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "admin"
  }'

# Como user comum
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "user",
    "password": "user"
  }'
```

Resposta esperada:
```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "type": "Bearer"
}
```

### 2. Gerenciamento de Produtos

Para os próximos comandos, substitua `$TOKEN` pelo token obtido no passo anterior.

#### 2.1 Listar Produtos (Qualquer usuário autenticado)
```bash
curl -X GET http://localhost:8080/api/products \
  -H "Authorization: Bearer $TOKEN"
```

#### 2.2 Buscar Produto por ID (Qualquer usuário autenticado)
```bash
curl -X GET http://localhost:8080/api/products/1 \
  -H "Authorization: Bearer $TOKEN"
```

#### 2.3 Criar Novo Produto (Apenas ADMIN)
```bash
curl -X POST http://localhost:8080/api/products \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Smartphone XYZ",
    "description": "Último modelo com 256GB",
    "price": 1999.90,
    "quantity": 50
  }'
```

#### 2.4 Atualizar Produto (Apenas ADMIN)
```bash
curl -X PUT http://localhost:8080/api/products/1 \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Smartphone XYZ Plus",
    "description": "Versão atualizada com 512GB",
    "price": 2499.90,
    "quantity": 30
  }'
```

#### 2.5 Deletar Produto (Apenas ADMIN)
```bash
curl -X DELETE http://localhost:8080/api/products/1 \
  -H "Authorization: Bearer $TOKEN"
```

### 3. Script para Testes

Você pode usar este script bash para testar todos os endpoints:

```bash
#!/bin/bash

# Cores para output
GREEN='\033[0;32m'
RED='\033[0;31m'
NC='\033[0m'

# URL base
BASE_URL="http://localhost:8080"

echo -e "${GREEN}1. Obtendo token do admin...${NC}"
ADMIN_TOKEN=$(curl -s -X POST $BASE_URL/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username": "admin", "password": "admin"}' | jq -r .token)

echo -e "${GREEN}2. Criando novo produto...${NC}"
PRODUCT_ID=$(curl -s -X POST $BASE_URL/api/products \
  -H "Authorization: Bearer $ADMIN_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Produto Teste",
    "description": "Descrição do produto teste",
    "price": 99.90,
    "quantity": 10
  }' | jq -r .id)

echo -e "${GREEN}3. Listando todos os produtos...${NC}"
curl -s -X GET $BASE_URL/api/products \
  -H "Authorization: Bearer $ADMIN_TOKEN" | jq .

echo -e "${GREEN}4. Buscando produto criado (ID: $PRODUCT_ID)...${NC}"
curl -s -X GET $BASE_URL/api/products/$PRODUCT_ID \
  -H "Authorization: Bearer $ADMIN_TOKEN" | jq .

echo -e "${GREEN}5. Atualizando produto...${NC}"
curl -s -X PUT $BASE_URL/api/products/$PRODUCT_ID \
  -H "Authorization: Bearer $ADMIN_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Produto Atualizado",
    "description": "Descrição atualizada",
    "price": 149.90,
    "quantity": 20
  }' | jq .

echo -e "${GREEN}6. Deletando produto...${NC}"
curl -s -X DELETE $BASE_URL/api/products/$PRODUCT_ID \
  -H "Authorization: Bearer $ADMIN_TOKEN"

echo -e "${GREEN}7. Verificando se produto foi deletado...${NC}"
RESPONSE=$(curl -s -w "%{http_code}" -X GET $BASE_URL/api/products/$PRODUCT_ID \
  -H "Authorization: Bearer $ADMIN_TOKEN")
if [[ $RESPONSE == *"404"* ]]; then
  echo -e "${GREEN}Produto deletado com sucesso!${NC}"
else
  echo -e "${RED}Erro ao deletar produto!${NC}"
fi
```

### 4. Testando Diferentes Roles

#### Como User (Acesso Limitado)
```bash
# Obter token de user
USER_TOKEN=$(curl -s -X POST $BASE_URL/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username": "user", "password": "user"}' | jq -r .token)

# Tentar criar produto (deve falhar)
curl -X POST $BASE_URL/api/products \
  -H "Authorization: Bearer $USER_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Teste Sem Permissão",
    "price": 99.90,
    "quantity": 1
  }'
```

### 5. Exemplos de Respostas

#### Produto Criado com Sucesso
```json
{
  "id": 1,
  "name": "Smartphone XYZ",
  "description": "Último modelo com 256GB",
  "price": 1999.90,
  "quantity": 50
}
```

#### Erro de Validação
```json
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
```

#### Erro de Autorização
```json
{
  "timestamp": "2024-01-01T10:00:00.000+00:00",
  "status": 403,
  "error": "Forbidden",
  "message": "Access Denied"
}
```

### 6. Dicas de Teste

1. **Ambiente de Desenvolvimento**:
   - Use o Swagger UI para testes interativos: `http://localhost:8080/swagger-ui.html`
   - H2 Console disponível em: `http://localhost:8080/h2-console`

2. **Ferramentas Recomendadas**:
   - [curl](https://curl.se/) para testes via linha de comando
   - [jq](https://stedolan.github.io/jq/) para formatar respostas JSON
   - [Postman](https://www.postman.com/) ou [Insomnia](https://insomnia.rest/) para testes via GUI

3. **Boas Práticas**:
   - Sempre verifique o código de status HTTP da resposta
   - Valide o formato do token JWT recebido
   - Teste cenários de erro (dados inválidos, token expirado, etc.)
   - Verifique as permissões com diferentes roles

## 📚 Referências

- [Spring Security Documentation](https://docs.spring.io/spring-security/reference/index.html)
- [Spring Boot Documentation](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/)
- [Thymeleaf + Spring Security](https://www.thymeleaf.org/doc/articles/springsecurity.html)

## 🤝 Contribuindo

1. Fork o projeto
2. Crie sua Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a Branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request 

## 🔑 API REST com JWT

### Autenticação JWT
O projeto inclui uma API REST protegida com JWT (JSON Web Token):

1. **Obter Token**:
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username": "admin", "password": "admin"}'
```

Resposta:
```json
{
  "token": "eyJhbGciOiJ...",
  "type": "Bearer"
}
```

2. **Usar Token**:
```bash
curl -X GET http://localhost:8080/api/products \
  -H "Authorization: Bearer eyJhbGciOiJ..."
```

### Endpoints da API

#### Autenticação
- `POST /api/auth/login`: Autenticar usuário e obter token JWT

#### Produtos (protegidos por JWT)
- `GET /api/products`: Listar todos os produtos
- `GET /api/products/{id}`: Buscar produto por ID
- `POST /api/products`: Criar novo produto (requer ROLE_ADMIN)
- `PUT /api/products/{id}`: Atualizar produto (requer ROLE_ADMIN)
- `DELETE /api/products/{id}`: Remover produto (requer ROLE_ADMIN)

### Exemplo de Produto
```json
{
  "name": "Produto Teste",
  "description": "Descrição do produto",
  "price": 99.90,
  "quantity": 10
}
```

### Documentação da API
A documentação completa da API está disponível através do Swagger UI:
```
http://localhost:8080/swagger-ui.html
```

## 🔒 Segurança da API

### JWT Configuration
- Token expira em 24 horas
- Assinatura usando HMAC SHA-512
- Token inclui:
  - Subject (username)
  - Roles do usuário
  - Data de emissão
  - Data de expiração

### Proteção de Endpoints
- Autenticação via Bearer Token
- Autorização baseada em roles
- Validação de token em cada requisição
- CORS configurado
- CSRF desabilitado para API

### Fluxo de Autenticação JWT
1. Cliente envia credenciais para `/api/auth/login`
2. Servidor valida credenciais e gera token JWT
3. Cliente armazena token
4. Cliente inclui token no header `Authorization` em requisições futuras
5. Servidor valida token e autoriza acesso

## 📝 Exemplos de Uso da API

### 1. Login e Obtenção do Token
```bash
# Login como admin
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username": "admin", "password": "admin"}'
```

### 2. Listar Produtos
```bash
# Usar token obtido no login
curl -X GET http://localhost:8080/api/products \
  -H "Authorization: Bearer ${TOKEN}"
```

### 3. Criar Produto (requer ROLE_ADMIN)
```bash
curl -X POST http://localhost:8080/api/products \
  -H "Authorization: Bearer ${TOKEN}" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Novo Produto",
    "description": "Descrição detalhada",
    "price": 199.90,
    "quantity": 50
  }'
```

### 4. Atualizar Produto
```bash
curl -X PUT http://localhost:8080/api/products/1 \
  -H "Authorization: Bearer ${TOKEN}" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Produto Atualizado",
    "price": 299.90,
    "quantity": 100
  }'
```

### 5. Deletar Produto
```bash
curl -X DELETE http://localhost:8080/api/products/1 \
  -H "Authorization: Bearer ${TOKEN}"
```

## 🧪 Testando a API

1. **Postman/Insomnia**:
   - Importe a coleção do Swagger
   - Configure variável de ambiente para o token
   - Use a feature de testes automatizados

2. **Swagger UI**:
   - Acesse `/swagger-ui.html`
   - Clique em "Authorize"
   - Insira o token JWT
   - Teste os endpoints interativamente

3. **Curl**:
   - Use os exemplos acima
   - Salve o token em uma variável:
     ```bash
     TOKEN=$(curl -s -X POST http://localhost:8080/api/auth/login \
       -H "Content-Type: application/json" \
       -d '{"username":"admin","password":"admin"}' | jq -r .token)
     ```

## 📚 Documentação Adicional

### Swagger/OpenAPI
- Documentação interativa: `/swagger-ui.html`
- Especificação OpenAPI: `/api-docs`
- Autenticação integrada
- Exemplos de requisição/resposta
- Schemas dos modelos

### Segurança
- Tokens JWT assinados
- Renovação automática de token
- Blacklist de tokens (opcional)
- Rate limiting (opcional)
- Logs de segurança 