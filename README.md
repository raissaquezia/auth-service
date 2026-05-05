
# 🚀 Auth Service

O **Auth Service** é um **microsserviço de autenticação e gerenciamento de usuários**, responsável por centralizar:

* 🔐 Autenticação (login/logout)
* 🔑 Autorização (roles e OAuth2)
* 👤 Gestão de usuários
* 📱 Controle de sessões
* 🔄 Emissão e renovação de tokens JWT

Ele foi projetado para ser utilizado como **serviço independente dentro de uma arquitetura de microsserviços**, atuando como o ponto central de identidade (**Identity Service**) para outras aplicações.

👉 Em outras palavras:
esse serviço é quem **valida usuários, emite tokens e protege o restante do sistema**.

---

# 🧱 Stack Tecnológica

### 💻 Backend

* **Java 21**
* **Spring Boot 3.2.5**

### ⚙️ Módulos Spring

* Spring Web → API REST
* Spring Data JPA → Persistência
* Spring Security → Segurança
* Spring Authorization Server → OAuth2
* Spring Validation → Validação

### 🗄️ Banco de Dados

* PostgreSQL

### 🔐 Segurança

* Auth0 Java JWT

### 🧰 Ferramentas

* Lombok
* Maven
* Docker + Docker Compose

---

# 🏗️ Arquitetura

```text
Controller → Service → Repository → Database
                ↓
              DTOs
```

### 📌 Camadas

* **Controllers** → Entrada HTTP
* **Services** → Regras de negócio
* **Repositories** → Banco de dados
* **Entities** → Modelos
* **DTOs** → Transferência de dados

---

# 🔐 Segurança e Tokens

### 🎟️ Tipos de Token

| Tipo          | Uso                | Expiração |
| ------------- | ------------------ | --------- |
| Access Token  | Rotas protegidas   | 15 min    |
| Refresh Token | Renovação de token | 7 dias    |

### 📱 Sessões

* Criadas a cada login
* Associadas a IP + dispositivo
* Permitem:

  * Logout por sessão
  * Logout global

---

# 🌐 CORS

Permitido para:

```
http://localhost:3000
http://localhost:5173
```

---

# 📡 API Endpoints

---

## 🔑 Autenticação (`/auth`)

| Método | Rota                    | Descrição                 | Acesso    |
| ------ | ----------------------- | ------------------------- | --------- |
| POST   | `/auth/login`           | Login e geração de tokens | Público   |
| POST   | `/auth/refresh`         | Renovar token             | Público   |
| POST   | `/auth/logout`          | Logout sessão atual       | Protegido |
| POST   | `/auth/logout-all`      | Logout global             | Protegido |
| GET    | `/auth/me`              | Usuário autenticado       | Protegido |
| GET    | `/auth/sessions`        | Listar sessões            | Protegido |
| DELETE | `/auth/sessions/{id}`   | Invalidar sessão          | Protegido |
| POST   | `/auth/forgot-password` | Recuperação de senha      | Público   |
| POST   | `/auth/reset-password`  | Reset de senha            | Público   |
| POST   | `/auth/change-password` | Alterar senha             | Protegido |

---

## 👤 Usuários (`/users`)

| Método | Rota        | Descrição       |
| ------ | ----------- | --------------- |
| GET    | `/users/me` | Dados completos |
| PATCH  | `/users/me` | Atualizar dados |
| DELETE | `/users/me` | Excluir conta   |

---

## 🛠️ Administração (`/api`)

| Método | Rota                  | Descrição           |
| ------ | --------------------- | ------------------- |
| POST   | `/api/users/register` | Criar usuário       |
| POST   | `/api/admin/clients`  | Criar client OAuth2 |

---

# 🔄 Fluxos de Uso (Exemplos Reais)

---

## 👤 Fluxo: Registro de Usuário

### 1️⃣ Criar usuário

```http
POST /api/users/register
Content-Type: application/json
```

```json
{
  "login": "usuario_teste",
  "password": "senha123",
  "clientId": "client_app_1",
  "role": "USER"
}
```

**Response (201 Created)**

```json
{}
```

---

### 2️⃣ Login

```http
POST /auth/login
```

```json
{
  "login": "usuario_teste",
  "password": "senha123",
  "clientId": "client_app_1",
  "device": "Chrome - MacBook"
}
```

**Response**

```json
{
  "accessToken": "jwt_token...",
  "refreshToken": "refresh_token...",
  "expiresIn": 900,
  "refreshExpiresIn": 604800,
  "tokenType": "Bearer",
  "user": {
    "id": "uuid",
    "login": "usuario_teste",
    "role": "USER",
    "clientId": "client_app_1"
  }
}
```

---

### 3️⃣ Acessar rota protegida

```http
GET /users/me
Authorization: Bearer ACCESS_TOKEN
```

---

## 🔐 Fluxo: Criação de Client OAuth2

> ⚠️ Requer `ROLE_ADMIN`

```http
POST /api/admin/clients
Authorization: Bearer ADMIN_TOKEN
```

```json
{
  "clientName": "frontend-app",
  "redirectUris": [
    "http://localhost:3000/callback",
    "http://localhost:5173/callback"
  ],
  "scopes": ["read", "write"],
  "grantTypes": ["authorization_code", "refresh_token"]
}
```

**Response**

```json
{
  "clientId": "client_app_1",
  "clientSecret": "generated_secret"
}
```

> ⚠️ O `clientSecret` é exibido apenas uma vez.

---

## 🔄 Fluxo: Refresh Token

```http
POST /auth/refresh
```

```json
{
  "refreshToken": "refresh_token..."
}
```

---

## 🚪 Logout

```http
POST /auth/logout
Authorization: Bearer ACCESS_TOKEN
```

---

# 🐳 Execução com Docker

---

## 📋 Pré-requisitos

* Docker
* Docker Compose

---

## ⚙️ Configuração

```bash
cp .env.exemple .env
```

```env
POSTGRES_DB=userservice_db
POSTGRES_USER=admin
POSTGRES_PASSWORD=admin_password

API_SECURITY_TOKEN_SECRET=chave_super_segura
```

---

## ▶️ Subir aplicação

```bash
docker-compose up -d --build
```

---

## 📜 Logs

```bash
docker-compose logs -f app
```

---

## 🧪 Testar

```
http://localhost:8080
```

---

## 🛑 Parar

```bash
docker-compose down
```

Reset total:

```bash
docker-compose down -v
```
