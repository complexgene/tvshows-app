# TVMazeV2 App

A production-ready demo with **Spring Boot (Java 17, Gradle)** and **React (Vite + Tailwind)**.  
Both services are dockerized and run with a single `docker-compose up`.

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
# FE: http://localhost:3000
# BE: http://localhost:5665/v1
# Swagger: http://localhost:5665/v1/swagger-ui

## API Endpoints

Base URL: `http://localhost:5665/v1`

- `GET  /v1/health` → Returns a simple health check response from `HealthController`.
- `GET  /v1/api/shows?page={page}&size={size}` → Returns a paginated list of shows. Response is a list of `ShowListItemDto` with metadata.
- `GET  /v1/api/shows/{id}` → Returns detailed information about a single show (`ShowDetailsDto`).
- `POST /v1/api/shows/import` → Reads the given file of titles and fetched the data extrnally and saves it in H2 DB.

## Assumptions & Decisions

- Using **Gradle** (not Maven) as the build tool.
- Backend runs on port `5665` with context path `/v1`.
- Database is **in-memory H2** for demo purposes; no external DB setup required.
- Docker Compose runs both backend and frontend containers on a shared network.
- Frontend uses Vite proxy to forward `/v1/api` calls to the backend service.
- Healthcheck uses `/v1/health` endpoint for simplicity instead of Actuator.
- The solution is optimized for easy local spin-up (`docker compose up --build`) and not for production scaling.
- Some of the movie titles are having special characters, have ignored them during validation while parsing.
- Have kept flattened class level entities instead of multiple tables and relationships for now.
- Used Taildwind CSS as the design framework on frontend
- Used spring boot framework in the backend. 

## Extra Features
- Exception handling & created one sample exception to demostrate excpetion management throughout.
- Different logging levels based on the severity
- added swagger open api integration for easy api access on http://localhost:5665/v1/swagger-ui/
- Since movie list can be large and details for them can be fetched in parallel, have used multi threading.
- On a similar note when fetching since the fetched list could be very large we have used Pagenation in the list api.
- Have added strategy factory patterns to plugin different ways(managed via configs) to fetch data due to large list.
    - SINGLE THREAD : All the calls happen on a single thread, not very efficient
    - POOLED : Calling using pool based multi threads to parallely make the call, can cause issue as all thread might get occupied
    - BATCHED : Make a batch call, efficient as we don't do unbounded thread allocation, and happens on a specific set


