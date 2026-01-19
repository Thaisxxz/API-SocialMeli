# SocialMeli API

API REST inspirada no ecossistema do Mercado Livre para conectar compradores e vendedores. Os usuários podem seguir vendedores, acompanhar publicações e promoções, ordenar resultados e interagir por meio de um feed social.

---

## 📚 Sumário
1. [Stack e requisitos](#-stack-e-requisitos)
2. [Funcionalidades / User Stories](#-funcionalidades--user-stories)
3. [Regras de validação](#-regras-de-validação)
4. [Estrutura do projeto](#-estrutura-do-projeto)
5. [Documentação via Swagger](#-documentação-via-swagger)
6. [Execução local](#-execução-local)
7. [Execução com Docker](#-execução-com-docker)
8. [Testes](#-testes)
9. [Principais endpoints](#-principais-endpoints)
10. [Próximos passos](#-próximos-passos)
11. [Licença](#-licença)

---

## 🚀 Stack e requisitos

- **Java 21**
- **Spring Boot**
- **Lombok** – habilitar *annotation processing* na IDE
- **Swagger / OpenAPI 3** – documentação e testes interativos
- **JUnit 5 + Mockito** – testes unitários e de integração
- **Docker / Docker Compose** – containerização opcional

**Pré-requisitos locais**  
- JDK 21  
- Maven 3.9+  
- Docker 24+ (opcional)

---

## 🧩 Funcionalidades / User Stories

| ID       | Descrição                                                                 |
|----------|---------------------------------------------------------------------------|
| **US-0001** | Seguir um vendedor específico (`POST /users/{userId}/follow/{userIdToFollow}`) |
| **US-0002** | Obter a contagem de seguidores de um vendedor                           |
| **US-0003** | Listar todos os seguidores de um vendedor                               |
| **US-0004** | Listar todos os vendedores seguidos por um usuário                      |
| **US-0005** | Registrar uma nova publicação                                           |
| **US-0006** | Listar publicações dos vendedores seguidos nas últimas 2 semanas        |
| **US-0007** | Deixar de seguir um vendedor                                            |
| **US-0008** | Ordenar respostas alfabeticamente (ASC/DESC)                            |
| **US-0009** | Ordenar respostas por data (ASC/DESC)                                   |
| **Promoções** | Criar promoções e obter a quantidade de promoções por vendedor        |

---

## ✅ Regras de validação

- `user_name`: obrigatório, máx. **15** caracteres, sem caracteres especiais.
- `product_name`: obrigatório, máx. **40** caracteres.
- `type`: obrigatório, máx. **15** caracteres.
- `brand`: obrigatório, máx. **25** caracteres, sem caracteres especiais.
- `color`: obrigatório, máx. **15** caracteres, sem caracteres especiais.
- `notes`: opcional, máx. **80** caracteres, sem caracteres especiais.
- `category`: obrigatório.
- `price`: obrigatório, **≤ 10.000.000**.
- Datas em `dd-MM-yyyy`; feeds limitados às **duas últimas semanas**.
- Ordenações via parâmetro `order`: `name_asc`, `name_desc`, `date_asc`, `date_desc`.
- Utilizar códigos HTTP adequados para erros de validação (400, 404, etc.).

---

## 🗂️ Estrutura do projeto

```
socialmeli/
├── src/
│   ├── main/
│   │   ├── java/… (controllers, services, repositories, dtos, models, config)
│   │   └── resources/
│   │       └── application.yml
│   └── test/
│       └── integration/…
├── pom.xml
├── Dockerfile
├── docker-compose.yml
└── README.md
```

---

## 📖 Documentação via Swagger

- UI: `http://localhost:8080/swagger-ui/index.html`
- OpenAPI JSON/YAML: `http://localhost:8080/v3/api-docs`

---

## ▶️ Execução local

```bash
# Clonar o repositório
git clone https://github.com/Thaisxxz/API-SocialMeli
cd socialmeli

# Executar com Maven Wrapper
./mvnw spring-boot:run

# ou gerar o JAR e executar
mvn clean package
java -jar target/socialmeli-*.jar
```

A API estará disponível em `http://localhost:8080`.

---

## 🐳 Execução com Docker

```bash
# Build da imagem
docker build -t socialmeli-api .

# Executar container
docker run --rm -p 8080:8080 --name socialmeli socialmeli-api
```

### Docker Compose

```bash
docker compose up --build
# ou
docker-compose up --build
```

Pare o ambiente com `docker compose down`.
