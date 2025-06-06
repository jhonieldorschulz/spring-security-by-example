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

## 🧪 Como Testar

1. **Login como Admin**:
   - Acesse `http://localhost:8080`
   - Login com `admin/admin`
   - Verifique acesso à área administrativa

2. **Login como User**:
   - Acesse `http://localhost:8080`
   - Login com `user/user`
   - Verifique que área administrativa está bloqueada

3. **Teste de Segurança**:
   - Tente acessar `/admin` como usuário comum
   - Verifique o redirecionamento de segurança
   - Teste logout e invalidação de sessão

4. **Validação de Formulário**:
   - Tente login com credenciais inválidas
   - Verifique mensagens de erro
   - Teste campos vazios e validações

## 🔍 Logs e Debugging

Para habilitar logs detalhados de segurança, adicione ao `application.properties`:
```properties
logging.level.org.springframework.security=DEBUG
```

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