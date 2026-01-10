# Docker Setup for Bookstore Application

This guide explains how to run the bookstore application using Docker.

## Prerequisites

- Docker Engine 20.10+ or Docker Desktop
- Docker Compose 2.0+ (optional, for docker-compose)

## Quick Start

### Option 1: Using Docker Compose (Recommended)

```bash
# Build and start the application
docker-compose up -d

# View logs
docker-compose logs -f

# Stop the application
docker-compose down

# Stop and remove volumes (deletes database)
docker-compose down -v
```

### Option 2: Using Docker directly

```bash
# Build the image
docker build -t bookstore:latest .

# Run the container
docker run -d \
  --name bookstore-app \
  -p 8080:8080 \
  -v bookstore-data:/app/data \
  -e SPRING_DATASOURCE_URL=jdbc:sqlite:/app/data/bookstore.db \
  bookstore:latest

# View logs
docker logs -f bookstore-app

# Stop the container
docker stop bookstore-app

# Remove the container
docker rm bookstore-app
```

## Accessing the Application

Once the container is running, access the application at:

- **API Base URL**: http://localhost:8080
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **API Docs**: http://localhost:8080/v3/api-docs
- **Health Check**: http://localhost:8080/health

## Database Persistence

The SQLite database is stored in a Docker volume (`bookstore-data`) to persist data across container restarts.

To access the database file:
```bash
# Find the volume location
docker volume inspect bookstore-data

# Or use docker-compose
docker-compose exec bookstore sh
# Then navigate to /app/data/bookstore.db
```

## Environment Variables

You can customize the application using environment variables:

- `SPRING_DATASOURCE_URL`: Database connection URL (default: `jdbc:sqlite:bookstore.db`)

Example:
```bash
docker run -e SPRING_DATASOURCE_URL=jdbc:sqlite:/app/data/custom.db bookstore:latest
```

## Building for Production

For production builds, you may want to:

1. Use a specific version tag instead of `latest`
2. Set up proper logging
3. Configure resource limits
4. Use a production-ready database (PostgreSQL, MySQL)

Example production docker-compose.yml:
```yaml
services:
  bookstore:
    build:
      context: .
      dockerfile: Dockerfile
    image: bookstore:1.0.0
    ports:
      - "8080:8080"
    volumes:
      - bookstore-data:/app/data
    environment:
      - SPRING_DATASOURCE_URL=jdbc:sqlite:/app/data/bookstore.db
    deploy:
      resources:
        limits:
          cpus: '1'
          memory: 512M
        reservations:
          cpus: '0.5'
          memory: 256M
    restart: always
```

## Troubleshooting

### Container won't start
```bash
# Check logs
docker-compose logs bookstore

# Check if port is already in use
lsof -i :8080
```

### Database issues
```bash
# Remove volume and restart (WARNING: deletes all data)
docker-compose down -v
docker-compose up -d
```

### Rebuild after code changes
```bash
docker-compose build --no-cache
docker-compose up -d
```

## Health Check

The container includes a health check that verifies the application is running:
```bash
# Check health status
docker ps
# Look for "healthy" status

# Or manually check
curl http://localhost:8080/health
```
