# TVShows App (Java + React + Docker)

This project implements a TV Shows listing/details application:
- **Backend**: Spring Boot (Java 11, compatible with Java 8+), H2 in‑memory DB
- **Frontend**: React + Vite + Material UI
- **Docker**: Separate containers + `docker-compose`
- **TVMaze**: Imports show details using `tvtitles.txt`

## Quick Start (Docker)
1. Ensure Docker is installed.
2. From the repo root (`tvshows-app`), run:

   ```bash
   docker compose up --build
