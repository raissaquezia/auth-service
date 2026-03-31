# Auth Microservice (JWT)

Este microserviço é responsável pela autenticação de usuários e gerenciamento de clientes utilizando **Spring Boot** e **JWT (JSON Web Token)** de forma manual. 

> **Nota:** Esta documentação reflete a versão **pré-migração** para o protocolo OAuth2/OIDC.

## 🛠 Tecnologias
* **Java 17+**
* **Spring Boot 3.x**
* **Spring Security** (Configuração manual de JWT)
* **JPA / Hibernate**
* **PostgreSQL** (ou H2 para desenvolvimento)
* **Lombok**

---

## 📊 Estrutura de Entidades Principal

Atualmente, o sistema gerencia o conceito de `Client`, que define as aplicações que podem interagir com o serviço de autenticação.

```java
@Entity
@Table(name = "clients")
public class Client {
    private UUID id;
    private String name;
    private Long tokenExpirationMillis;
}
```

---

## 🚀 Endpoints da API

### 🔐 Autenticação

#### Registro de Usuário
`POST /auth/register`
Realiza o cadastro de um novo usuário no sistema.
* **Corpo da Requisição:**
  ```json
  {
    "username": "raissa_dev",
    "password": "senha_segura",
    "role": "USER"
  }
  ```

#### Login e Geração de Token
`POST /auth/login`
Valida as credenciais e retorna um token JWT customizado.
* **Corpo da Requisição:**
  ```json
  {
    "username": "raissa_dev",
    "password": "senha_segura",
    "role": "ADMIN",
    "clientId": "uuid-do-cliente",
    "expiresIn": 3600000
  }
  ```
* **Resposta (200 OK):**
  ```json
  {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
  }
  ```

---

### 🏢 Gerenciamento de Clientes (Applications)

#### Listar Clientes
`GET /clients`
Retorna todos os clientes cadastrados.

#### Cadastrar Cliente
`POST /clients`
Cria uma nova aplicação cliente que pode solicitar tokens.
* **Corpo da Requisição:**
  ```json
  {
    "name": "Frontend Web App",
    "tokenExpirationMillis": 86400000
  }
  ```

---

## ⚙️ Configuração de Segurança Atual

A segurança está implementada via `SecurityFilterChain`, interceptando requisições e validando o cabeçalho `Authorization: Bearer <token>`. A assinatura do token utiliza uma chave secreta (HMAC) definida nas propriedades da aplicação.

* **Chave de Assinatura:** Definida em `application.properties` como `api.security.token.secret`.
* **Validação:** Filtro customizado que estende `OncePerRequestFilter`.

---

## 🛠 Como Executar

1. Clone o repositório.
2. Configure o banco de dados no `application.properties`.
3. Execute `./mvnw spring-boot:run`.

---
