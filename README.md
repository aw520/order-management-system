# Order Management System (OMS)

## Overview

The **Order Management System (OMS)** is a backend microservices-based application designed to manage users, products, and orders in a modular and scalable way.  
The system follows modern backend best practices using **Spring Boot**, **RESTful APIs**, **Docker**, and **relational databases**, with a strong focus on clean architecture and service isolation.

This project emphasizes real-world backend concerns such as authentication, authorization, service-to-service communication, persistence, and containerized deployment.

---

## Architecture

The system consists of multiple independent microservices:

- **User Service**
  - Manages user registration and authentication
  - Issues JWT access tokens
- **Product Service**
  - Manages product data and inventory
  - Exposes APIs for product lookup and validation
- **Order Service**
  - Handles order creation, updates, and queries
  - Communicates with Product Service via REST for product validation
- **MySQL**
  - Persistent data storage
  - Each service owns its own database schema
- **Redis**
  - Used for caching and token-related data (e.g. refresh tokens)
- **Docker & Docker Compose**
  - Containerizes all services and infrastructure for local development

### Communication Pattern

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

### Infrastructure
- Docker
- Docker Compose
- MySQL 8
- Redis

### Tooling
- Maven
- Lombok
- Postman

---

## Key Features

- Microservice-based architecture
- JWT-based authentication and authorization
- OAuth2 Resource Server for JWT validation
- Clear separation of controller, service, and repository layers
- Database isolation per service
- Dockerized local development environment
- Global exception handling with consistent error responses
- Externalized configuration via environment variables

---

## Project Structure

```text
order-management-system/
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
│   └── entity/
│
├── order-service/
│   ├── controller/
│   ├── service/
│   ├── repository/
│   └── entity/
│
├── docker-compose.yml
└── README.md
```

## Future Improvements

- [ ] Introduce product price history in ProductService
    - Persist historical price records with effective timestamps
    - Support audit and analytics use cases
    - Orders will continue to store price snapshots (no recalculation)

- [ ] Add price quote expiration and validation
- [ ] Use Kafka for messaging between order service and product service
- [ ] Handle validation errors in order service, ensure product service is able to rollback
- [ ] Add blob storage for product images
- [ ] Add log monitoring
- [ ] OAuth2 authentication
- [ ] Flyway database migrations
## TODO:
- [ ] Order service ask for a token from user service (new end point), use it to communicate with product service
