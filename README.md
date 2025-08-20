# TVMazeV2 App

A production-ready demo with **Spring Boot (Java 17, Gradle)** and **React (Vite + Tailwind)**.  
Both services are dockerized and run with a single `docker-compose up`.

<img width="781" height="589" alt="Screenshot 2025-08-21 at 12 47 04 AM" src="https://github.com/user-attachments/assets/59f688a2-1783-4f54-9b37-a08e16ae5d5e" />

<img width="848" height="587" alt="Screenshot 2025-08-21 at 12 47 20 AM" src="https://github.com/user-attachments/assets/a0c9b8d9-ee93-4d18-998c-5d9c8daf59bb" />


---

## Tech

- Backend: Spring Boot 3.x (Gradle), H2 in-memory DB, context path `/v1`
- Frontend: React + Vite + Tailwind
- Docker: Multi-stage images, compose-managed network

---

## Quick Start (Docker)

```bash
# from repo root
docker compose up --build

Once this is successful, you would have
# FE(Frontend)  : http://localhost:3000
# BE(Backend)   : http://localhost:5665/v1
# Swagger       : http://localhost:5665/v1/swagger-ui

## API Endpoints

Base URL: `http://localhost:5665/v1`

- `GET  /health` → Returns a simple health check response from `HealthController`.
- `GET  /api/shows?page={page}&size={size}` → Returns a paginated list of shows. Response is a list of `ShowListItemDto` with metadata.
- `GET  /api/shows/{id}` → Returns detailed information about a single show (`ShowDetailsDto`).
- `POST /api/shows/import` → Reads the given file of titles and fetched the data extrnally and saves it in H2 DB.

## Assumptions & Decisions

- Using **Gradle** (not Maven) as the build tool.
- Backend runs on port `5665` with context path `/v1`.
- Database used is **in-memory H2** for demo purposes; no external DB setup required.
- Docker Compose runs both backend and frontend containers on a shared network.
- Frontend uses Vite proxy to forward `/v1/api` calls to the backend service.
- Healthcheck uses `/v1/health` endpoint for simplicity instead of Actuator.
- The solution is optimized for easy local spin-up (`docker compose up --build`) and not for production scaling.
- Some of the movie titles are having special characters, have ignored them during validation while parsing.
- Have kept flattened class level entities instead of multiple tables and relationships for now.
- Used Taildwind CSS as the design framework on frontend
- Used spring boot framework in the backend. 

## Extra Features
- Exception handling, custom exceptions & global exeption handler for exception management.
  Also added sample error code to show ease of error management & contracts.
  {
    "code": 502,
    "status": "error",
    "message": "Import failed: ExecutorService in active state did not accept task: java.util.concurrent.CompletableFuture$AsyncRun@3870a9db",
    "errorType": "TvShowExternalDataFetchException",
    "path": "/v1/api/shows/import",
    "timestamp": 1755715268142
  }
- Different logging levels based on the severity(INFO, DEBUG, ERROR).
- added swagger open api integration for easy api access on http://localhost:5665/v1/swagger-ui/
- Since movie list can be large and details for them can be fetched in parallel, have used multi threading.
- On a similar note when fetching since the fetched list could be very large we have used Pagenation in the list api.
- Have added strategy factory patterns to plugin different ways(managed via configs) to fetch data due to large list.
    - SINGLE THREAD : All the calls happen on a single thread, not very efficient
    - BATCHED : Make a batch call, efficient as we don't do unbounded thread allocation, and happens on a specific set

-- Thank you --
