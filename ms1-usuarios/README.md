#  API Gestão Financeira - MS1 Usuários

---
<br>

## Sobre o Projeto

Microsserviço responsável pelo gerenciamento de usuários da aplicação de Gestão Financeira.

O projeto foi desenvolvido utilizando Java 25 e Spring Boot, seguindo os princípios da Arquitetura Hexagonal (Ports and Adapters), garantindo baixo acoplamento entre as camadas e maior facilidade para evolução e manutenção.

<br>


### Tecnologias Utilizadas

---
- Java 25
- Spring Boot
- Spring Data JPA
- Spring Security
- PostgreSQL
- Docker
- Docker Compose
- Swagger / OpenAPI
- JUnit 5
- Mockito
- Maven

<br>


### Arquitetura

---

O projeto utiliza Arquitetura Hexagonal (Ports and Adapters).


```
src/main/java/com/ms1usuarios

├── application
│   └── service
│
├── domain
│   ├── model
│   └── exception
│
├── dto
│
├── infrastructure
│   ├── controller
│   ├── persistence
│   ├── security
│   └── handler
│
└── ports
├── input
└── output
```
<br>


### Funcionalidades

---
- Usuários
- Criar usuário
- Buscar usuário por ID
- Listar usuários
- Atualizar usuário
- Remover usuário

<br>

### Importação CSV

---
Permite importar múltiplos usuários através de arquivo CSV.

Exemplo:
```
nome,email,senha
Joao,joao@email.com,123456
Maria,maria@email.com,654321
```

<br>


### Autenticação

---
Autenticação utilizando Spring Security com HTTP Basic Authentication.

Rotas protegidas exigem usuário e senha cadastrados no banco.

<br>


### Executando com Docker

---
Build
```
docker compose up --build
```
<br>

Verificar containers
```
docker ps
```

<br>

### Configuração das Variáveis

---
Arquivo:

````
.env
````
<br>

Exemplo:

````
USER_DB_NAME=usuariosdb
USER_DB_USER=postgres
USER_DB_PASSWORD=postgres


MS1_SERVER_PORT=8080
````

<br>

### Endpoints


---
**Criar usuário**
````
POST /users
````

<br>

**Login**
````
POST /users/login
````

<br>

**Buscar usuário**
````
GET /users/{id}
````

<br>

**Listar usuários**
````
GET /users
````

<br>

**Atualizar usuário**
````
PUT /users/{id}
````

<br>

**Remover usuário**
````
DELETE /users/{id}
````

<br>

**Importar CSV**
````
POST /users/import
````

<br>

**Swagger**

Após iniciar a aplicação:
````
http://localhost:8080/swagger-ui.html
````

ou

````
http://localhost:8080/swagger-ui/index.html
````
<br>


### Segurança

---
Rotas públicas:
````
POST /users
POST /users/login
````

Rotas protegidas:
````
GET /users
GET /users/{id}
PUT /users/{id}
DELETE /users/{id}
POST /users/import
````

<br>

### Testes

---
O projeto possui testes unitários utilizando JUnit 5 e Mockito.

<br>

### Services testados

---
- ImportUsuariosCsvService
- CreateUsuarioService
- LoginService
- UpdateUsuarioService
- DeleteUsuarioService
- FindUsuarioService
- ListUsuariosService


<br>

### Executar testes

---
````
./mvnw test
````

<br>

Resultado atual:
````
Tests run: 15
Failures: 0
Errors: 0

BUILD SUCCESS
````