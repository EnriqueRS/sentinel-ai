# sentinel-ai

Minimal, production-oriented Spring Boot service that consumes JSON alerts from Kafka (`system.alerts`) and persists them into a Postgres database. Tests use Testcontainers to provide Kafka and Postgres for integration tests.

## Table of contents

- [Project overview](#project-overview)
- [Technologies](#technologies)
- [Prerequisites](#prerequisites)
- [Configuration](#configuration)
- [Run locally (development)](#run-locally-development)
- [Run with Docker Compose (optional)](#run-with-docker-compose-optional)
- [Running tests](#running-tests)
- [Project structure](#project-structure)
- [Troubleshooting](#troubleshooting)
- [Contributing](#contributing)
- [License](#license)

## Project overview

`sentinel-ai` listens to the Kafka topic `system.alerts`, transforms incoming JSON messages into domain `Alert` objects and delegates processing to the application use case that persists alerts into a PostgreSQL database.

Integration tests use Testcontainers to spin up Kafka and Postgres automatically, allowing end-to-end test scenarios without external dependencies.

## Technologies

- Java 17+ (or configured project JDK)
- Spring Boot
- Spring Kafka
- Spring Data / JDBC / JPA
- Testcontainers (Kafka & Postgres) for integration tests
- Maven build system
- Jackson for JSON
- PostgreSQL as persistence engine
- Kafka (Confluent/Apache) as message broker
- Awaitility used in integration tests

## Prerequisites

- Java JDK 17 or newer
- Maven 3.6+
- Docker (required to run Testcontainers and any Docker-based local services)
- (Optional) Docker Compose if you want to run Kafka/Postgres via compose

## Configuration

Application configuration uses environment variables (see `application.yml` placeholders). The most common variables:

- `SPRING_DATASOURCE_URL` — JDBC URL for Postgres, e.g. `jdbc:postgresql://localhost:5432/sentinel`
- `SPRING_DATASOURCE_USERNAME` — DB user
- `SPRING_DATASOURCE_PASSWORD` — DB password
- `SPRING_KAFKA_BOOTSTRAP_SERVERS` — Kafka bootstrap servers, e.g. `localhost:9092`

Example environment export:

```bash
export SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/sentinel
export SPRING_DATASOURCE_USERNAME=sentinel
export SPRING_DATASOURCE_PASSWORD=secret
export SPRING_KAFKA_BOOTSTRAP_SERVERS=localhost:9092
