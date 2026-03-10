# Employee API

Spring Boot REST API for employee CRUD operations with H2 in-memory database, plus a React UI that can be run or deployed **separately** from the API. The project includes Swagger documentation and unit tests with 80%+ code coverage.

## Requirements

- **Backend:** Java 17+, Maven 3.6+
- **Frontend:** Node.js 18+ and npm

## Architecture

| Component    | Description |
|-------------|-------------|
| **REST API** | Spring Boot app (port 8080). Serves `/api/employees`, Swagger UI, H2 console. CORS is enabled for the UI origin(s). |
| **React UI** | Vite + React app. Built to static files and served from its own process or static host. Calls the API using a configurable base URL. |

The API and the React UI are **deployed and run independently**. They communicate over HTTP; the API allows the UI origin via CORS.

---

## 1. Test locally (API and UI separate)

Run the API and the UI in two terminals. The UI uses the Vite dev server and proxies `/api` to the backend, so no extra config is needed.

**Terminal 1 – start the API:**

```bash
mvn spring-boot:run
```

API base: **http://localhost:8080** (Swagger: http://localhost:8080/swagger-ui.html).

**Terminal 2 – start the React UI:**

```bash
cd frontend
npm install
npm run dev
```

Open **http://localhost:3000** in your browser. The UI will call the API via the dev proxy (no `VITE_API_URL` needed).

**Login (static credentials):** username `admin`, password `admin`.

---

## 2. Deploy API and UI separately

### Backend (REST API)

- Build: `mvn clean package` (or `mvn clean verify` for tests and coverage).
- Run the JAR: `java -jar target/employee-api-1.0.0-SNAPSHOT.jar`.
- Or run in your environment (e.g. Docker, cloud) with **port 8080** (or override `server.port`).

**CORS:** The API allows UI origins from `app.cors.allowed-origins` in `application.yml` (default: `http://localhost:3000`, `http://localhost:4173`). For production, set this to your UI’s origin(s), e.g.:

```yaml
app:
  cors:
    allowed-origins: https://my-app.example.com
```

### Frontend (React UI)

- Set the API base URL for the environment where the UI will run (e.g. production API URL).
- Build and serve the built files from any static host (Nginx, S3 + CloudFront, Netlify, etc.).

**Option A – build with API URL in env (typical for production):**

```bash
cd frontend
npm install

# Point to your API (example: same machine, different port)
echo "VITE_API_URL=http://localhost:8080" > .env
# Or for production: VITE_API_URL=https://api.yourdomain.com

npm run build
```

The build output is in **`frontend/dist/`**. Serve that folder as static files (e.g. `npx serve dist`, or upload to your host).

**Option B – run built UI locally (no proxy):**

```bash
cd frontend
cp .env.example .env
# Edit .env and set VITE_API_URL=http://localhost:8080

npm install
npm run build
npm run preview
```

Open **http://localhost:4173**. Ensure the API is running on 8080 and that `app.cors.allowed-origins` in the API includes `http://localhost:4173`.

**Reference:** See `frontend/.env.example` for `VITE_API_URL` usage.

---

## 3. Backend-only build and test

You can build and run the API without the UI:

```bash
# Build (includes tests; fails if coverage < 80%)
mvn clean verify

# Run
mvn spring-boot:run
```

- **API:** http://localhost:8080  
- **Swagger UI:** http://localhost:8080/swagger-ui.html  
- **OpenAPI JSON:** http://localhost:8080/v3/api-docs  
- **H2 Console:** http://localhost:8080/h2-console (JDBC URL: `jdbc:h2:mem:employeedb`, user: `sa`, password: empty)

---

## REST APIs

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST   | `/api/employees`      | Create employee |
| PUT    | `/api/employees/{id}` | Update employee |
| GET    | `/api/employees/{id}` | Get one employee |
| GET    | `/api/employees`      | Get all employees |

---

## Web UI (React) – summary

- **Login:** Static credentials: `admin` / `admin`.
- **Dashboard:** Lists all employees; **Add employee** (top right), **Edit** per row, **Logout**.
- **New / Edit employee:** Form (Name, Email, Department). Save uses POST or PUT; on success, redirects to dashboard.

When API and UI run separately, configure:

1. **UI:** `VITE_API_URL` to the API base (e.g. `http://localhost:8080` or `https://api.yourdomain.com`).
2. **API:** `app.cors.allowed-origins` to include the UI origin (e.g. `http://localhost:3000`, `http://localhost:4173`, or your production UI URL).

---

## Test & coverage (backend)

```bash
mvn test          # run tests
mvn verify        # run tests and enforce 80% line coverage (JaCoCo)
```

Coverage report: `target/site/jacoco/index.html`

---

## Example API requests

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
