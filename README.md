# 🐾 Amandaba — Java Advanced | Sprint 4

Aplicação Spring Boot que gerencia **cadastro, login JWT e perfil de tutores e veterinários** do Amandaba. Mantém integração com o **Oracle compartilhado com a API .NET**, responsável por pets, informações de saúde e compartilhamentos. A aplicação inclui uma interface web simples para demonstrar cadastro, login, consulta/edição do próprio perfil e busca de veterinários.

## Arquitetura e tecnologias

- Java 21, Spring Boot 4.1.1, Spring Web MVC.
- Spring Security, BCrypt, JWT com JJWT (bibliotecas já presentes no projeto original).
- Spring Data JPA/Hibernate, Oracle JDBC, Flyway (migração opcional e isolada).
- Jakarta Bean Validation, JUnit 5, Mockito, HTML/CSS/JavaScript (sem frameworks adicionais de interface).

```text
Navegador / aplicativo mobile
    | cadastro, login e perfil
    v
Java / Spring Boot ----> Oracle (TAB_USUARIO, TAB_TUTOR, TAB_VETERINARIO)
    | emite token JWT               ^
    v                               |
Front-end -> Bearer JWT -> .NET ----+----> Pets, saude, compartilhamento
```

O JWT é emitido pelo Java com claims `sub` (email), `userId` e `perfil` (`TUTOR` ou `VETERINARIO`), assinado com **HS256**, `iss=amandaba-java`, `aud=amandaba-apis`. Veja `INTEGRACAO_DOTNET.md` para instruções.

## Requisitos

- JDK 21.
- Oracle acessível com o schema do arquivo SQL do Challenge (tabelas `TAB_USUARIO`, `TAB_TUTOR` e `TAB_VETERINARIO`).
- Acesso ao Maven via `./mvnw` (Maven Wrapper) na primeira execução.
- As variáveis de ambiente abaixo configuradas **fora do código-fonte**.

## Configuração

Copie `.env.example` para seu gerenciador de ambiente, mas **não comite** `.env`. Configure:

| Variável | Descrição |
|---|---|
| `DB_URL` | Ex.: `jdbc:oracle:thin:@//HOST:1521/SERVICE` |
| `DB_USER` | Usuário Oracle |
| `DB_PASSWORD` | Senha Oracle |
| `JWT_SECRET` | **Chave secreta forte com pelo menos 32 bytes UTF-8**, igual à usada para validação no .NET |
| `JWT_ISSUER` | Opcional; padrão `amandaba-java` |
| `JWT_AUDIENCE` | Opcional; padrão `amandaba-apis` |
| `JWT_VALIDITY_MS` | Opcional; padrão 3600000 (1h) |
| `FLYWAY_ENABLED` | Padrão `false` no Oracle compartilhado |
| `FRONTEND_ORIGINS` | Origens CORS separadas por vírgula |
| `PORT` | Porta HTTP; padrão 8080 |

Execute:

```bash
./mvnw clean test
./mvnw spring-boot:run
```

No Windows: `mvnw.cmd clean test` e `mvnw.cmd spring-boot:run`.

Acesse `http://localhost:8080/` para a interface web e utilize os endpoints JSON para integração mobile ou .NET.

**Importante:** valores de `DS_SENHA_HASH_USUARIO` como `senha_hash_1`, incluídos como dados fictícios no script do banco, não são BCrypt e **não permitem login**. Cadastre novos usuários pelo Java para fazer a demonstração. Não publique senhas ou chaves. Se as credenciais do ZIP original eram reais, faça a rotação delas, inclusive do banco.

## Endpoints

### Públicos

| Método | Endpoint | Objetivo |
|---|---|---|
| `POST` | `/api/auth/register/tutor` | Cadastrar tutor (201) |
| `POST` | `/api/auth/register/vet` | Cadastrar veterinário com CRMV (201) |
| `POST` | `/api/auth/login` | Validar BCrypt e emitir JWT |
| `GET` | `/` | Interface web de demonstração |

### Protegidos por Bearer JWT

| Método | Endpoint | Objetivo | Perfil |
|---|---|---|---|
| `GET` | `/api/usuarios/me` | Consultar próprio perfil | Ambos |
| `PUT` | `/api/usuarios/me` | Editar nome, e-mail, telefone e nascimento | Ambos |
| `PUT` | `/api/usuarios/me/senha` | Alterar senha com senha atual | Ambos |
| `DELETE` | `/api/usuarios/me` | Desativar conta (soft delete) | Ambos |
| `GET` | `/api/tutores/me` | Consultar perfil do tutor | Tutor |
| `PUT` | `/api/tutores/me` | Editar perfil do tutor | Tutor |
| `GET` | `/tutor/dadosTutor/{userId}` | Rota antiga (somente próprio ID) | Tutor |
| `GET` | `/api/veterinarios/me` | Consultar perfil do veterinário | Veterinário |
| `PUT` | `/api/veterinarios/me` | Editar perfil do veterinário | Veterinário |
| `PUT` | `/api/veterinarios/me/crmv` | Atualizar CRMV | Veterinário |
| `GET` | `/api/veterinarios?nome=&crmv=&pagina=0&tamanho=10` | Busca paginada de veterinários ativos | Ambos |
| `GET` | `/api/veterinarios/{id}` | Dados públicos do profissional | Ambos |
| `GET` | `/api/relatorios/tutor/me` | Relatório dos pets do tutor, com indicadores clínicos | Tutor |
| `GET` | `/api/relatorios/veterinario/me` | Relatório dos pets com compartilhamento ativo | Veterinário |

O catálogo público dos veterinários mostra apenas `idVeterinario`, `nomeCompleto` e `crmv`, sem CPF, email, senha ou telefone.

### Exemplos JSON

Cadastro de tutor em `/api/auth/register/tutor`:

```json
{"nomeCompleto":"Ana Silva","cpf":"12345678901","email":"ana@exemplo.com","password":"senha-segura123","telefone":"11999999999","dataNascimento":"1995-05-12"}
```

Cadastro de veterinário em `/api/auth/register/vet` (aceita `crmv_veterinario` ou `crmv`):

```json
{"nomeCompleto":"Bruna Costa","cpf":"12345678902","email":"bruna@exemplo.com","password":"senha-segura123","telefone":"11988888888","dataNascimento":"1993-09-18","crmv_veterinario":"CRMV-SP-12345"}
```

Login em `/api/auth/login`:

```json
{"email":"ana@exemplo.com","password":"senha-segura123"}
```

Resposta:

```json
{"token":"eyJ...","tipo":"Bearer","perfil":"TUTOR","idUsuario":12,"idPerfil":5}
```

Editar meu perfil via `PUT /api/usuarios/me` (headers: `Authorization: Bearer eyJ...`):

```json
{"nomeCompleto":"Ana Maria Silva","email":"ana@exemplo.com","telefone":"11999998888","dataNascimento":"1995-05-12"}
```

Alterar senha em `PUT /api/usuarios/me/senha`:

```json
{"senhaAtual":"senha-segura123","novaSenha":"minha-nova-senha123"}
```

Editar CRMV em `PUT /api/veterinarios/me/crmv`: `{"crmv":"CRMV-SP-12346"}`.

## Autorização

- `401` quando não há JWT válido; `403` para perfil incorreto.
- Apenas tutores acessam `/api/tutores/me` e a rota antiga `/tutor/dadosTutor/{userId}`.
- Apenas veterinários acessam `/api/veterinarios/me` e edição de CRMV.
- A identidade é obtida do JWT verificado e confrontada com o status da conta e o perfil no Oracle.
- E-mail e CPF são únicos; a edição do CPF não está disponível por ser um identificador cadastral.
- Desativar a conta altera o status em `TAB_USUARIO` para `INATIVO`; não apaga vínculos nem pets.
- Após mudar o e-mail, o token anterior deixa de autenticar porque `sub` corresponde ao e-mail antigo. Faça novo login.

## Relatórios de saúde integrados ao Oracle

`GET /api/relatorios/tutor/me` retorna somente os pets do tutor logado. `GET /api/relatorios/veterinario/me` retorna somente pets com compartilhamento `ATIVO` para o veterinário logado. Os relatórios apresentam espécie, idade, doenças ativas, medicamentos em uso, vacinas com próxima dose atrasada e consultas registradas. A idade é calculada pela função **`PKG_PET_SAUDE.FN_CALCULAR_IDADE_PET`** já existente no script Oracle da disciplina de BD, em vez de reproduzir o cálculo em Java.

Esses endpoints são de análise/consulta, não alteram informações de saúde (as atualizações permanecem sob responsabilidade do .NET). Para funcionarem, o Oracle deve possuir todo o schema de pets e a package `PKG_PET_SAUDE`, além das três tabelas de contas. A migration isolada do Java **não cria o schema de saúde**.

## Banco compartilhado e Flyway

A aplicação utiliza as tabelas da disciplina de Banco de Dados. `spring.jpa.hibernate.ddl-auto=none` impede a criação automática de schema. `FLYWAY_ENABLED=false` por padrão evita que **Java e .NET tentem administrar as mesmas tabelas simultaneamente**.

O Flyway permanece presente e há uma migration `V1__create_shared_account_tables.sql` para criar essas **três tabelas apenas em um schema Oracle novo** (sem as tabelas previamente criadas). Em um schema compartilhado que já contenha as tabelas do Challenge, **não rode a migration de criação**, pois o script completo de Banco de Dados é a fonte de verdade. Os arquivos antigos para `aman_users`, incompatíveis com o schema final, foram preservados em `src/main/resources/db/legacy-migration` como histórico, não como migrações ativas.

**Não execute o script completo de Banco de Dados da Sprint 4 em uma base com dados importantes** sem conferir os `DROP TABLE` no início.

## Testes

```bash
./mvnw clean test
```

Testes unitários de cadastro separado, autenticação correta/incorreta, usuários inativos, token emitido, token com assinatura inválida, atualização de senha e desativação. Os testes usam mocks e não precisam de Oracle. Testes end-to-end com Oracle/Swagger exigem instância e credenciais próprias.

## Conceitos de Java Advanced aplicados

Durante o desenvolvimento da API Java do Amandaba, foram aplicados conceitos e recursos do ecossistema Java e Spring Boot, com foco em organização do código, segurança, validação de dados, reutilização e separação de responsabilidades.

### 1. DTOs (Data Transfer Objects)

Os DTOs são utilizados para transportar informações entre o cliente e a API, evitando que as entidades utilizadas para persistência no banco de dados sejam expostas diretamente nas requisições e respostas.

No projeto, os DTOs foram organizados em dois grupos:

- **DTOs de requisição (`dto/request`):** recebem os dados enviados pelo cliente, como `LoginRequest`, `RegisterRequestTutor`, `RegisterRequestVeterinario`, `AtualizarPerfilRequest` e `AlterarSenhaRequest`.
- **DTOs de resposta (`dto/response`):** definem quais informações serão devolvidas pela API, como `AuthResponse`, `UserResponse`, `TutorResponse`, `VetResponse` e `RelatorioPetResponse`.

Também foram utilizados Java Records em DTOs que possuem estrutura mais simples, como `LoginRequest` e `AtualizarCrmvRequest`. Essa abordagem reduz a quantidade de código necessária para representar objetos imutáveis de transporte de dados.

Essa separação contribui para a segurança da aplicação, pois permite controlar quais informações podem ser recebidas e quais podem ser disponibilizadas ao cliente, evitando a exposição de dados sensíveis, como senhas e informações cadastrais privadas.

### 2. Validação de dados com Jakarta Bean Validation

A validação dos dados recebidos pela API é realizada utilizando Jakarta Bean Validation, integrado ao Spring Boot por meio da dependência `spring-boot-starter-validation`.

Foram utilizadas anotações para estabelecer regras diretamente nos DTOs, como:

- `@NotBlank`: impede o envio de campos obrigatórios vazios ou contendo apenas espaços.
- `@Email`: verifica se o endereço de e-mail possui formato válido.
- `@Size`: estabelece limites mínimos e máximos para o tamanho dos campos.
- `@Pattern`: permite definir padrões específicos, como a validação do CPF com 11 dígitos numéricos.
- `@Past`: garante que a data de nascimento informada seja anterior à data atual.

Nos controllers, a anotação `@Valid` é utilizada junto aos parâmetros recebidos com `@RequestBody`, fazendo com que as validações sejam executadas automaticamente antes da chamada dos serviços.

Além disso, a classe `ApiExceptionHandler`, anotada com `@RestControllerAdvice`, centraliza o tratamento de exceções da API. Dessa forma, erros de validação podem ser convertidos em respostas HTTP 400 (Bad Request), enquanto violações de integridade dos dados podem retornar HTTP 409 (Conflict).

Essa abordagem reduz a necessidade de validações repetidas nos serviços e proporciona respostas mais consistentes para os consumidores da API.

### 3. Lombok e redução de código repetitivo

O projeto possui a dependência do **Project Lombok** configurada no `pom.xml`, incluindo sua configuração como processador de anotações durante a compilação.

O Lombok é uma biblioteca que permite reduzir código repetitivo (*boilerplate*) em classes Java por meio de anotações. Entre os recursos disponíveis estão:

- `@Getter` e `@Setter`: geração automática de métodos de acesso.
- `@NoArgsConstructor`: geração de construtor sem argumentos.
- `@AllArgsConstructor`: geração de construtor com todos os atributos.
- `@Builder`: disponibilização do padrão Builder para criação de objetos.

Entretanto, na versão atual do projeto, embora a dependência e o processamento de anotações estejam configurados, as classes ainda utilizam principalmente construtores e métodos de acesso implementados manualmente. Portanto, o Lombok está preparado para utilização, mas não é um recurso efetivamente aplicado nas classes atuais.

### 4. Injeção de dependência

A aplicação utiliza o mecanismo de Injeção de Dependência (DI) disponibilizado pelo Spring Framework, permitindo que as classes recebam automaticamente os componentes dos quais dependem, sem precisar instanciá-los diretamente.

O projeto utiliza principalmente a **injeção por construtor**, abordagem que facilita a manutenção, os testes e o gerenciamento das dependências.

Por exemplo, a classe `AuthService` recebe por construtor os seguintes componentes:

- `UserRepository`;
- `TutorRepository`;
- `VeterinarioRepository`;
- `PasswordEncoder`;
- `JwtService`.

Essas dependências são gerenciadas pelo container de inversão de controle do Spring, que identifica os componentes por meio de anotações como `@Service`, `@Repository` e `@Component`.

Com isso, as classes ficam menos acopladas às implementações específicas, facilitando a substituição de componentes e a criação de testes unitários utilizando mocks.

### 5. Arquitetura em camadas

A API foi organizada utilizando uma arquitetura em camadas, separando as responsabilidades de cada parte da aplicação.

A estrutura principal é composta por:

| Camada | Responsabilidade |
|---|---|
| `controllers` | Receber requisições HTTP, validar entradas e encaminhar chamadas para os serviços. |
| `service` | Implementar as regras de negócio, autenticação, gerenciamento de perfis e relatórios. |
| `repository` | Realizar operações de persistência e consultas ao banco de dados Oracle. |
| `model` | Representar as entidades persistidas no banco de dados. |
| `dto/request` | Definir os dados aceitos nas requisições. |
| `dto/response` | Definir os dados disponibilizados nas respostas. |
| `config` | Centralizar configurações de segurança, CORS e tratamento de exceções. |
| `enums` | Representar valores controlados, como status de usuários e veterinários. |

Essa organização permite que cada camada tenha uma responsabilidade bem definida. Por exemplo, o `AuthController` recebe as solicitações de cadastro e login, encaminhando o processamento para `AuthService`, que utiliza os repositórios para consultar e persistir informações.

Dessa maneira, as regras de negócio não ficam concentradas nos controllers, facilitando a manutenção, a reutilização de funcionalidades e a evolução da aplicação.

### 6. Autenticação e autorização com Spring Security e JWT

A segurança da aplicação é implementada utilizando Spring Security, JSON Web Tokens (JWT) e BCrypt para proteção das senhas.

O processo de autenticação funciona da seguinte maneira:

1. O usuário realiza o cadastro informando seus dados pessoais e uma senha.
2. A aplicação utiliza `BCryptPasswordEncoder` para gerar um hash seguro da senha antes de armazená-la no banco de dados.
3. No login, a aplicação consulta o usuário pelo e-mail e verifica a senha informada utilizando o mecanismo de comparação do BCrypt.
4. Após a autenticação bem-sucedida, a classe `JwtService` gera um token JWT contendo informações como e-mail, identificador do usuário e perfil de acesso.
5. O cliente utiliza esse token nas próximas requisições, enviando-o no cabeçalho HTTP `Authorization: Bearer`.
6. A classe `JwtAuthenticationFilter`, que estende `OncePerRequestFilter`, intercepta as requisições, verifica a assinatura e a validade do token e estabelece o contexto de autenticação do Spring Security.

O token utiliza o algoritmo HS256 para assinatura e possui informações de emissor (`issuer`) e público destinatário (`audience`), permitindo que a API Java e a API .NET compartilhem o mecanismo de validação de autenticação.

Além da autenticação, o projeto implementa autorização baseada em perfis (*Role-Based Access Control*). São utilizados dois perfis principais:

- **TUTOR:** possui acesso às funcionalidades relacionadas ao próprio perfil e aos relatórios de saúde de seus pets.
- **VETERINARIO:** possui acesso ao próprio perfil profissional, atualização do CRMV, busca de profissionais e relatórios dos pets compartilhados com ele.

A autorização é aplicada por meio de anotações como `@PreAuthorize`, habilitadas através de `@EnableMethodSecurity` na configuração de segurança.

O filtro JWT também verifica a situação da conta e o perfil do usuário no Oracle, evitando que a autorização dependa exclusivamente das informações presentes no token.

A escolha de JWT permite implementar autenticação sem estado de sessão (*stateless*), facilitando a integração entre diferentes aplicações e a comunicação com o aplicativo mobile e a API .NET do Amandaba.

### Orientação de deploy

O projeto preserva o `Dockerfile` da versão original (build Maven + execução com Java 21). No host de nuvem, configure todas as variáveis na interface de gerenciamento de segredos, habilite conectividade com o Oracle, publique a porta `8080` e teste os endpoints públicos e privados. Nenhum deploy foi efetuado automaticamente.

## Segurança e responsabilidades

O projeto não utiliza OAuth2, Keycloak, serviço externo de autenticação ou outra tecnologia adicional. O JWT usa JWT/Spring Security já presentes no projeto recebido. 
