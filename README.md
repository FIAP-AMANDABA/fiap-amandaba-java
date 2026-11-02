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

## Decisões arquiteturais e escolhas tecnológicas

Durante o desenvolvimento da API Java do Amandaba, foram realizadas escolhas arquiteturais visando facilitar a manutenção, garantir a segurança das informações dos usuários e permitir a integração com os demais componentes do sistema, especialmente a API .NET e o banco de dados Oracle compartilhado.

As tecnologias foram escolhidas considerando a necessidade de desenvolver uma API REST organizada, segura e preparada para atender tanto o aplicativo mobile quanto outros serviços do ecossistema Amandaba.

### 1. Spring Boot — Desenvolvimento da API

O Spring Boot foi escolhido como framework principal para o desenvolvimento da API devido à sua integração com o ecossistema Spring e à facilidade de configuração de aplicações Java.

Entre os principais motivos para sua utilização estão:

- Configuração simplificada por meio de *auto-configuration*.
- Integração nativa com Spring Security, Spring Data JPA e Bean Validation.
- Facilidade para criação de endpoints REST utilizando `@RestController`.
- Gerenciamento de dependências e componentes por meio do container Spring.
- Suporte à execução independente da aplicação, sem necessidade de configurar manualmente um servidor de aplicação externo.

A utilização do Spring Boot permitiu concentrar o desenvolvimento nas regras de negócio da aplicação, reduzindo a necessidade de configurações manuais de infraestrutura.

### 2. Spring Security — Segurança e controle de acesso

O Spring Security foi escolhido para centralizar os mecanismos de autenticação e autorização da API.

Como o Amandaba possui diferentes tipos de usuários, tutores e veterinários, era necessário garantir que cada perfil tivesse acesso somente às funcionalidades permitidas.

A tecnologia oferece recursos para:

- Configurar quais endpoints são públicos e quais exigem autenticação.
- Interceptar requisições HTTP.
- Validar credenciais e tokens de autenticação.
- Aplicar regras de autorização de acordo com o perfil do usuário.
- Integrar filtros personalizados ao fluxo de segurança da aplicação.

No projeto, o Spring Security trabalha em conjunto com um filtro JWT e com anotações como `@PreAuthorize`, permitindo restringir funcionalidades específicas aos tutores ou veterinários.

Essa escolha evita implementar manualmente todo o mecanismo de segurança em cada controller, centralizando as configurações e tornando as regras de acesso mais consistentes.

### 3. JWT (JSON Web Token) — Autenticação sem estado de sessão

Foi escolhido o JWT como mecanismo de autenticação para permitir que o aplicativo mobile e os demais serviços do Amandaba utilizem uma forma padronizada de identificação dos usuários.

Diferentemente de uma autenticação baseada em sessões armazenadas no servidor, o JWT permite que as informações necessárias para identificar o usuário sejam transportadas em um token assinado.

No Amandaba, após um login válido, a API Java gera um token contendo informações como:

- Identificador do usuário.
- E-mail utilizado na autenticação.
- Perfil de acesso (TUTOR ou VETERINARIO).
- Emissor e destinatário do token.

O token é assinado utilizando o algoritmo HS256 e enviado pelo cliente nas próximas requisições por meio do cabeçalho:

`Authorization: Bearer <token>`

Uma decisão importante foi padronizar as informações do JWT para permitir sua validação também pela API .NET. Dessa forma, o usuário pode ser autenticado no serviço Java e utilizar o mesmo mecanismo de identificação ao acessar funcionalidades disponibilizadas pelo serviço .NET.

Essa abordagem reduz a necessidade de implementar mecanismos de login independentes em cada API e facilita a comunicação entre os componentes do sistema.

### 4. BCrypt — Proteção das senhas

Para armazenar as senhas dos usuários, foi escolhido o algoritmo BCrypt, disponibilizado pelo Spring Security por meio da classe `BCryptPasswordEncoder`.

A escolha foi motivada pela necessidade de proteger informações sensíveis e evitar o armazenamento de senhas em texto puro no banco de dados.

O BCrypt utiliza um processo de hashing com salt e custo computacional configurável, tornando mais difícil a descoberta das senhas por ataques de força bruta.

No fluxo de cadastro, a senha recebida é processada pelo `PasswordEncoder` antes de ser persistida no Oracle.

Durante o login, a senha informada é comparada com o hash armazenado utilizando o método `matches`, sem a necessidade de recuperar ou descriptografar a senha original.

Essa decisão aumenta a segurança das contas e segue uma prática recomendada para aplicações que trabalham com autenticação de usuários.

### 5. Spring Data JPA — Persistência e acesso ao banco de dados

O Spring Data JPA foi escolhido para facilitar a comunicação entre a aplicação Java e o banco de dados Oracle.

Por meio dele, é possível representar os dados utilizando entidades Java e realizar operações de persistência através de interfaces Repository, reduzindo a necessidade de implementar manualmente operações SQL para funcionalidades convencionais.

No projeto, essa tecnologia é utilizada para:

- Consultar usuários pelo e-mail e identificadores.
- Cadastrar tutores e veterinários.
- Atualizar informações cadastrais.
- Consultar dados relacionados aos perfis.
- Gerenciar operações de persistência.

Além das consultas convencionais disponibilizadas pelo JPA, o projeto também utiliza consultas SQL nativas quando é necessário trabalhar diretamente com estruturas específicas do banco Oracle, principalmente nos relatórios de saúde.

A escolha permite combinar produtividade na implementação das operações CRUD com a flexibilidade necessária para consultas mais específicas.

Outro ponto importante é que o Hibernate está configurado com `ddl-auto=none`, evitando que a aplicação altere automaticamente a estrutura do banco compartilhado com a API .NET.

### 6. Separação de responsabilidades — Arquitetura em camadas

A aplicação foi organizada seguindo uma arquitetura em camadas, com o objetivo de separar as responsabilidades e reduzir o acoplamento entre os componentes.

Essa decisão foi importante porque o Amandaba possui diferentes funcionalidades, como autenticação, gerenciamento de perfis, busca de veterinários e consulta de informações de saúde.

A arquitetura adotada distribui essas responsabilidades da seguinte maneira:

- **Controller:** responsável por receber as requisições HTTP, validar os dados de entrada e retornar as respostas.
- **Service:** responsável por executar as regras de negócio e coordenar as operações da aplicação.
- **Repository:** responsável pelo acesso e persistência dos dados no Oracle.
- **Model:** representa as entidades utilizadas no mapeamento dos dados persistidos.
- **DTO:** controla a comunicação entre a API e seus consumidores, evitando a exposição direta das entidades.
- **Config:** centraliza configurações de segurança, filtros, CORS e outros componentes da aplicação.

Essa organização permite que alterações em uma camada tenham menor impacto sobre as demais.

Por exemplo, uma mudança na forma de consultar os usuários pode ser realizada na camada Repository sem exigir que toda a lógica de autenticação seja reescrita no Controller.

Além disso, essa separação facilita a realização de testes unitários, pois os serviços podem ser testados isoladamente utilizando mocks para simular os repositórios e outras dependências.

### 7. Oracle compartilhado — Integração entre Java e .NET

Uma decisão arquitetural importante foi manter o banco de dados Oracle compartilhado entre a API Java e a API .NET.

Essa escolha permite que os dois serviços trabalhem sobre uma mesma estrutura de dados, evitando duplicação de informações e a necessidade de sincronizar bancos independentes.

A API Java é responsável principalmente pelo cadastro, autenticação e gerenciamento dos perfis de tutores e veterinários. Já a API .NET concentra as funcionalidades relacionadas ao gerenciamento dos pets e suas informações de saúde.

Ambas utilizam as estruturas definidas na disciplina de Banco de Dados, mantendo uma fonte centralizada de informações.

Para evitar conflitos entre os serviços, a aplicação Java não realiza a criação automática das tabelas pelo Hibernate e mantém o Flyway desabilitado por padrão no ambiente compartilhado.

Além disso, foram utilizadas consultas SQL nativas em funcionalidades que dependem de estruturas específicas do Oracle, como os relatórios de saúde e a função PL/SQL responsável pelo cálculo da idade dos pets.

Essa decisão demonstra a aplicação de uma arquitetura baseada em serviços independentes que compartilham uma mesma camada de persistência, respeitando as responsabilidades de cada API.

### Considerações finais

As decisões arquiteturais adotadas buscam equilibrar segurança, organização, manutenção e integração entre tecnologias.

A combinação de Spring Boot, Spring Security, JWT, BCrypt e Spring Data JPA permite desenvolver uma API REST estruturada e segura, enquanto a arquitetura em camadas facilita a evolução do código.

A integração com o Oracle compartilhado e com a API .NET permite que o Amandaba funcione como um ecossistema composto por diferentes serviços, cada um responsável por um conjunto específico de funcionalidades.

### Orientação de deploy

O projeto preserva o `Dockerfile` da versão original (build Maven + execução com Java 21). No host de nuvem, configure todas as variáveis na interface de gerenciamento de segredos, habilite conectividade com o Oracle, publique a porta `8080` e teste os endpoints públicos e privados. Nenhum deploy foi efetuado automaticamente.

## Segurança e responsabilidades

O projeto não utiliza OAuth2, Keycloak, serviço externo de autenticação ou outra tecnologia adicional. O JWT usa JWT/Spring Security já presentes no projeto recebido. 
