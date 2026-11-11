# MS2 - Transações



O MS2 é o microsserviço responsável pelo gerenciamento das transações financeiras da aplicação de Gestão Financeira.

O serviço permite registrar movimentações financeiras, consultar saldo, realizar análises de despesas, exportar relatórios e converter valores para moedas estrangeiras utilizando integrações externas.

<br>

## Funcionalidades

---

- Registro de transações: Depósito, Retirada, Transferência e Compra;
- Consulta de transações;
- Análise de despesas: Resumo por categoria, Total por dia e Total por mês;
- Consulta de saldo;
- Conversão para USD e EUR via BrasilAPI;
- Exportação de relatório CSV;
- Autenticação via Basic Auth integrada ao MS1;

<br>


# Arquitetura

---

O projeto utiliza Arquitetura Hexagonal, separando regras de negócio, integrações externas e detalhes de infraestrutura.

<br>

### Estrutura de pastas

```
adapters
├── input
│   └── controller
│
└── output
    ├── client
    └── persistence

application
└── service

domain
├── model
└── exception

infrastructure

ports
├── input
└── output

dto

```
<br>

### Executando o Projeto

----

O projeto foi desenvolvido para ser executado através do Docker Compose. Toda a infraestrutura necessária é criada automaticamente, incluindo os bancos de dados PostgreSQL, os microsserviços e o PgAdmin.
Containers Esperados

<br>

### Variáveis de ambiente

---
O projeto utiliza variáveis de ambiente centralizadas no arquivo:

```
.env
```

Utilize o arquivo de exemplo disponibilizado no repositório:

```
.env.example
```
ou copie manualmente o conteúdo para um arquivo .env.

As variáveis controlam:

- portas dos microsserviços;
- credenciais dos bancos PostgreSQL;
- credenciais do PgAdmin;
- outras configurações necessárias para execução local.

<br>

### Os microsserviços utilizam o arquivo:

---
```
Dockerfile
```

Este arquivo tem o objetivo de:

- gerar as imagens da aplicação.
- compilar o projeto utilizando Maven;
- gerar o arquivo JAR da aplicação;
- construir a imagem final do Spring Boot;
- iniciar o microsserviço.

<br>

### Docker Compose

---
O ambiente completo pode ser iniciado com:

```
docker compose up -d --build
```

Esse comando:

- Cria os bancos PostgreSQL.
- Cria o PgAdmin.
- Compila o MS1.
- Compila o MS2.
- Cria as imagens Docker.
- Inicia todos os containers necessários.

<br>

### Verificando os containers

---

Após a execução do comando build, verifique:

```
docker ps
```

os seguintes containers devem estar ativos:

```
postgres-users
postgres-transactions
 
ms1-usuarios
ms2-transacoes
 
pgadmin
```

Isso garante que os microsserviços sejam recompilados e executados com a versão mais recente do código.

<br>

### Acessando os serviços

---
Swagger do MS2
```
http://localhost:8081/swagger-ui/index.html
```

<br>

### Executando Testes

---

````
cd ms2-transacoes
 
./mvnw.cmd verify
````
