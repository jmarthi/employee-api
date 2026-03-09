# Employee API

Spring Boot REST API for employee CRUD operations with H2 in-memory database, Swagger documentation, and unit tests with 80%+ code coverage.

## Requirements

- Java 17+
- Maven 3.6+

## Build & Run

```bash
# Build (includes tests and JaCoCo coverage; fails if coverage < 80%)
mvn clean verify

# Run application
mvn spring-boot:run
```

## REST APIs

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST   | `/api/employees`     | Create employee |
| PUT    | `/api/employees/{id}`| Update employee |
| GET    | `/api/employees/{id}`| Get one employee |
| GET    | `/api/employees`     | Get all employees |

## Swagger UI

When the app is running:

- **Swagger UI:** http://localhost:8080/swagger-ui.html
- **OpenAPI JSON:** http://localhost:8080/v3/api-docs

## H2 Console

- **URL:** http://localhost:8080/h2-console  
- **JDBC URL:** `jdbc:h2:mem:employeedb`  
- **User:** `sa`  
- **Password:** (empty)

## Test & Coverage

```bash
mvn test          # run tests
mvn verify        # run tests and enforce 80% line coverage (JaCoCo)
```

Coverage report: `target/site/jacoco/index.html`

## Example Requests

**Create employee:**
```bash
curl -X POST http://localhost:8080/api/employees \
  -H "Content-Type: application/json" \
  -d '{"name":"John Doe","email":"john@example.com","department":"Engineering"}'
```

**Get all employees:**
```bash
curl http://localhost:8080/api/employees
```

**Get one employee:**
```bash
curl http://localhost:8080/api/employees/1
```

**Update employee:**
```bash
curl -X PUT http://localhost:8080/api/employees/1 \
  -H "Content-Type: application/json" \
  -d '{"name":"Jane Doe","email":"jane@example.com","department":"HR"}'
```
