# Wallet Service

## Overview

**Wallet Service** is a Java microservice that manages digital wallets, enabling deposits, withdrawals, transfers, and
balance inquiries (current and historical). It is designed for high availability and auditability.

## Features

- **Create Wallet** (`POST /wallets`)
- **Get Current Balance** (`GET /wallets/{id}/balance`)
- **Get Balance History by Period** (`GET /wallets/{id}/balance/history?start=&end=`)
- **Deposit Funds** (`POST /wallets/deposit`)
- **Withdraw Funds** (`POST /wallets/withdraw`)
- **Transfer Funds** (`POST /wallets/transfer`)

## Non-Functional Requirements

- **Transactional**: critical operations use `@Transactional` to guarantee ACID.
- **Migrations**: Flyway manages versioned SQL scripts (`VYYYYMMDD_X_…`, `VYYYYMMDD_X_…`).
- **Logging**: SLF4J with pattern `c=<class>, m=<method>, msg=<message>`.
- **Metrics**: Custom New Relic metrics (`Custom/…`).
- **Containerization**: Docker multi-stage build, exposes port `8080`.
- **Orchestration**: Kubernetes with Deployment, Service, Secret, and HPA (CPU 50%, Memory 70%).

## Architecture

- **Hexagonal (Ports & Adapters)**
    - **Core**: domain (`Domain`), ports (interfaces), use cases (`@Service`).
    - **Adapters**:
        - **Persistence**: JPA + MapStruct + UUID↔String converters.
        - **Web**: Spring MVC RestControllers + DTOs + MapStruct mappers.
- **SOLID Principles** and Clean Code applied across all layers.

## Technologies

- **Language**: Java 21
- **Framework**: Spring Boot 3, Spring Data JPA
- **Database**: Oracle XE (PDB `XEPDB1`)
- **Migrations**: Flyway
- **Container**: Docker, Kubernetes (K8s)
- **Monitoring**: New Relic, SLF4J
- **Testing**: JUnit 5, MockMvc, H2 (profile `test`)

## Installation and Running

1. **Prerequisites**: Java 21, Docker, Kubernetes (with Metrics Server).
2. **Build & Test**:
   ```bash
   ./gradlew clean build
   ```

3. **Run with Docker Compose**:

    ```bash
    docker-compose up -d
   ```

4. **Kubernetes (namespace wallet-service):**:
   ```bash
    kubectl apply -f k8s/namespace.yaml
    kubectl apply -n wallet-service -f k8s/secret.yaml \
        -f k8s/deployment.yaml \
        -f k8s/service.yaml \
        -f k8s/hpa.yaml
   ```

## Design Choices

* Hexagonal + SOLID: facilitates testing and maintenance by decoupling from infrastructure.

* UUID as String: converted via AttributeConverter for readability in Oracle.

* Flyway: versioned migrations and ddl-auto: validate for safety.

* HPA: horizontal scaling based on CPU and memory metrics.

* New Relic: custom metrics for monitoring and alerting.

* SLF4J: structured logging for better observability.

* MapStruct: for DTO to entity conversion, ensuring immutability and performance.

* JPA: for ORM, with `@Transactional` for ACID compliance.

* Spring Boot: for rapid development and ease of configuration.

* Docker: for containerization, ensuring consistent environments.

* Kubernetes: for orchestration, enabling scaling and management of microservices.

## Trade-offs & Compromises

* Using Oracle XE simplifies integration tests but limits PDB customization.

* MapStruct mapping with automatic converters minimizes boilerplate.

* Generic HPA thresholds; production should adjust probes and thresholds.

## Estimated Time

Approximate total: 12 hours (including setup, implementation, testing, and deployment).
