# Kome API

The backend REST API for the Kome blogging platform, built with Spring Boot 3, Java 21, and MyBatis-Plus.

> For overall architecture, deployment guide, and roadmap,
> see [this blog post](https://kome.km-o.com/post/kome-blog-overview).

## Related Projects

| Project      | Description                     | Repository                                       |
|--------------|---------------------------------|--------------------------------------------------|
| kome-blog    | Blog frontend                   | [GitHub](https://github.com/km-hello/kome-blog)  |
| kome-admin   | Admin dashboard                 | [GitHub](https://github.com/km-hello/kome-admin) |
| **kome-api** | Backend REST API (this project) | —                                                |

## Tech Stack

| Category   | Technologies                   |
|------------|--------------------------------|
| Framework  | Spring Boot 3.5                |
| Language   | Java 21                        |
| ORM        | MyBatis-Plus 3.5               |
| Database   | MySQL 8+                       |
| Migration  | Flyway                         |
| Security   | Spring Security + JWT (jjwt)   |
| API Docs   | SpringDoc OpenAPI (Swagger UI) |
| AI         | OpenAI-compatible Chat API     |
| Monitoring | Spring Actuator                |

## Features

- **RESTful API** — Public endpoints for blog content, admin endpoints for management (CRUD for posts, memos, tags,
  friend links)
- **JWT Authentication** — Stateless Bearer token auth with BCrypt password hashing
- **Database Migration** — Flyway-managed schema with versioned SQL scripts
- **AI Content Assistance** — Generate post summaries and URL slugs via OpenAI-compatible API
- **Full-Text Search** — MySQL FULLTEXT index on post title and summary
- **Soft Delete** — Logical deletion for posts, memos, and users
- **First-Run Setup** — One-time initialization endpoint to create the admin account
- **API Documentation** — Swagger UI available in development mode

## Getting Started

### Prerequisites

- Java 21+
- MySQL 8+

### Install & Run

```bash
git clone https://github.com/km-hello/kome-api.git
cd kome-api

# Configure database (see Environment Variables below)
# Then start the application:
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

The server starts at `http://localhost:8080`. Swagger UI is available at `/swagger-ui.html` in the `dev` profile.

### Commands

| Command                                                 | Description                              |
|---------------------------------------------------------|------------------------------------------|
| `./mvnw spring-boot:run -Dspring-boot.run.profiles=dev` | Start with dev profile (Swagger enabled) |
| `./mvnw clean package`                                  | Build production JAR                     |
| `java -jar target/kome-api-*.jar`                       | Run production JAR                       |

## API Overview

### Public Endpoints

| Method | Endpoint                | Description                      |
|--------|-------------------------|----------------------------------|
| GET    | `/api/posts`            | List published posts (paginated) |
| GET    | `/api/posts/{slug}`     | Get post by slug                 |
| GET    | `/api/posts/archive`    | Posts grouped by year/month      |
| GET    | `/api/tags`             | List tags with post counts       |
| GET    | `/api/memos`            | List published memos (paginated) |
| GET    | `/api/memos/latest`     | Latest memos                     |
| GET    | `/api/memos/stats`      | Memo statistics                  |
| GET    | `/api/links`            | List public friend links         |
| GET    | `/api/site/info`        | Site information                 |
| GET    | `/api/site/initialized` | Check initialization status      |
| POST   | `/api/site/setup`       | First-time admin setup           |
| POST   | `/api/user/login`       | User login                       |

### Admin Endpoints (requires JWT)

| Method              | Endpoint                   | Description               |
|---------------------|----------------------------|---------------------------|
| GET/POST/PUT/DELETE | `/api/admin/posts/**`      | Post management           |
| GET/POST/PUT/DELETE | `/api/admin/memos/**`      | Memo management           |
| GET/POST/PUT/DELETE | `/api/admin/tags/**`       | Tag management            |
| GET/POST/PUT/DELETE | `/api/admin/links/**`      | Friend link management    |
| GET/PUT             | `/api/admin/user`          | User profile management   |
| PUT                 | `/api/admin/user/password` | Change password           |
| POST                | `/api/admin/ai/summary`    | AI-generated post summary |
| POST                | `/api/admin/ai/slug`       | AI-generated URL slug     |

## Project Structure

```
src/main/java/com/kmo/kome/
├── controller/         # REST controllers (7 controllers)
├── service/            # Service interfaces
│   └── impl/           # Service implementations
├── entity/             # Database entities (User, Post, Tag, Memo, Link, PostTag)
├── mapper/             # MyBatis mapper interfaces
├── dto/
│   ├── request/        # Request DTOs with validation
│   └── response/       # Response DTOs
├── config/             # Security, Web, OpenAPI, AI configuration
├── security/           # JWT filter, entry point, access denied handler
├── common/             # Result wrapper, enums, exception handling
└── utils/              # JWT utilities

src/main/resources/
├── application.yaml        # Main configuration
├── application-dev.yaml    # Development profile
├── db/migration/           # Flyway SQL scripts
└── mapper/                 # MyBatis XML mappers
```

## Environment Variables

| Variable                 | Description                            | Default                  |
|--------------------------|----------------------------------------|--------------------------|
| `SPRING_PROFILES_ACTIVE` | Active profile (`dev` enables Swagger) | `default`                |
| `SERVER_PORT`            | Server port                            | `8080`                   |
| `DB_HOST`                | MySQL host                             | `localhost`              |
| `DB_PORT`                | MySQL port                             | `3306`                   |
| `DB_NAME`                | Database name                          | `kome_db`                |
| `DB_USER`                | Database user                          | —                        |
| `DB_PASSWORD`            | Database password                      | —                        |
| `JWT_SECRET`             | JWT signing secret                     | —                        |
| `JWT_EXPIRATION`         | Token expiration (ms)                  | `259200000` (3 days)     |
| `AI_API_KEY`             | OpenAI-compatible API key              | —                        |
| `AI_BASE_URL`            | AI API base URL                        | `https://api.openai.com` |
| `AI_MODEL`               | AI model name                          | `gpt-4o-mini`            |
| `AI_TIMEOUT`             | AI request timeout (seconds)           | `30`                     |
