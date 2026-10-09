# Lab 1: Person Service with CI/CD

REST API for managing Person entities, with full CI/CD pipeline.

## Live Demo

- **Application**: https://person-service1.onrender.com
- **Swagger UI**: https://person-service1.onrender.com/swagger-ui.html
- **Health**: https://person-service1.onrender.com/actuator/health

## Tech Stack

| Component        | Technology           |
|------------------|----------------------|
| Language         | Java 17              |
| Framework        | Spring Boot 3.3.5    |
| Build Tool       | Maven                |
| Database         | PostgreSQL (Render)  |
| Migrations       | Flyway               |
| Containerization | Docker (multi-stage) |
| CI/CD            | GitHub Actions       |
| Deployment       | Render (Docker)      |
| API Testing      | Postman + Newman     |
| Documentation    | SpringDoc OpenAPI    |

## API Endpoints

Base URL: `/api/v1/persons`

| Method | Path | Description | Status |
|--------|------|-------------|--------|
| GET    | `/api/v1/persons`      | Get all persons   | 200            |
| GET    | `/api/v1/persons/{id}` | Get person by ID  | 200 / 404      |
| POST   | `/api/v1/persons`      | Create new person | 201 + Location |
| PATCH  | `/api/v1/persons/{id}` | Update person     | 200 / 404      |
| DELETE | `/api/v1/persons/{id}` | Delete person     | 204 / 404      |

### Example Request

```bash
curl -X POST https://person-service-xxxx.onrender.com/api/v1/persons \
  -H "Content-Type: application/json" \
  -d '{"name":"Ali Ahmed","age":31,"address":"Moscow","work":"BMSTU"}'
