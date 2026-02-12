# Order Management System (OMS)

## Overview

The **Order Management System (OMS)** is a backend **microservices-based application** designed to manage users, products, and orders in a modular and scalable way.

The system is built using **Spring Boot** and follows modern backend engineering practices, including **service isolation**, **event-driven communication**, **JWT-based security**, and **containerized deployment**.

This project focuses on **real-world backend concerns** such as authentication, authorization, asynchronous service coordination, transactional consistency, and fault-tolerant communication between services.

---

## Architecture

The system consists of multiple independent microservices, each owning its own data and responsibility.

### Services

- **User Service**
  - Manages user registration and authentication
  - Issues JWT access tokens
  - Acts as the identity provider for the system

- **Product Service**
  - Manages product data and inventory
  - Validates product availability and pricing
  - Handles product validation requests asynchronously

- **Order Service**
  - Handles order creation, updates, and queries
  - Publishes product validation requests
  - Consumes validation responses and updates order state accordingly

- **Common Module**
  - Shared DTOs and Kafka message contracts
  - Ensures consistent serialization across services

- **MySQL**
  - Persistent data storage
  - Each service owns its own database schema

- **Redis**
  - Used for caching and token-related data (e.g. refresh tokens)

- **Kafka**
  - Asynchronous, event-driven communication between services
  - Decouples Order Service and Product Service

- **Docker & Docker Compose**
  - Containerizes all services and infrastructure
  - Enables consistent local development and testing

---

## Communication Pattern

### REST (Synchronous)
- External APIs (client → service)
- Public-facing queries and commands

### Kafka (Asynchronous, Event-Driven)
- Order → Product validation workflow
- Prevents tight coupling between services
- Improves scalability and fault tolerance

#### Validation Flow

1. Order Service publishes a `ProductValidationRequest`
2. Product Service consumes the request and performs validation
3. Product Service publishes a `ProductValidationResponse`
4. Order Service consumes the response and updates order status

This pattern follows a **Saga-style asynchronous workflow**, avoiding distributed transactions while maintaining consistency.
- KRaft Kafka between product service and order service
- Synchronous REST-based communication
- JSON over HTTP

---

## Tech Stack

### Backend
- Java 17
- Spring Boot
- Spring Web (REST)
- Spring Data JPA (Hibernate)
- Spring Security
- Spring Kafka

### Messaging
- Apache Kafka

### Infrastructure
- Docker
- Docker Compose
- MySQL 8
- Redis

### Tooling
- Maven (multi-module)
- Lombok
- Postman

---

## Key Features

- Microservice-based architecture
- Event-driven communication using Kafka
- JWT-based authentication and authorization
- OAuth2 Resource Server for JWT validation
- Asynchronous product validation workflow
- Clear separation of controller, service, and repository layers
- Database isolation per service
- Idempotent order processing
- Dockerized local development environment
- Global exception handling with consistent error responses
- Externalized configuration via environment variables

---

## Project Structure

```text
order-management-system/
├── common/
│   └── kafka/
│
├── user-service/
│   ├── controller/
│   ├── service/
│   ├── repository/
│   ├── entity/
│   └── security/
│
├── product-service/
│   ├── controller/
│   ├── service/
│   ├── repository/
│   ├── kafka/
│   │   └── ProductValidationListener.java
│   └── entity/
│
├── order-service/
│   ├── controller/
│   ├── service/
│   ├── repository/
│   ├── kafka/
│   │   ├── ProductValidationProducer.java
│   │   └── ProductValidationReplyHandler.java
│   └── entity/
│
├── docker-compose.yml
└── README.md
```

## Future Improvements/TODO:

- [ ] Introduce product price history in ProductService
    - Persist historical price records with effective timestamps
    - Support audit and analytics use cases
    - Orders will continue to store price snapshots (no recalculation)

- [ ] Add price quote expiration and validation
- [ ] Handle validation errors in order service, ensure product service is able to rollback
- [ ] Add blob storage for product images
- [ ] Add log monitoring
- [ ] OAuth2 authentication
- [ ] Flyway database migrations
- [ ] Order service ask for a token from user service (new end point), use it to communicate with product service
- [ ] Use Nginx as reverse proxy
