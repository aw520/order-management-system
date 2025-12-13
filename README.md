# 🧾 Order Management System

**Tech Stack:** Java, Spring Boot, Hibernate, MySQL, Spring Security, JUnit

---

## 🚀 Overview
The **Order Management System (OMS)** is a backend application designed to manage customer orders efficiently and securely.  
It provides RESTful APIs for creating, updating, canceling, and tracking orders, ensuring data integrity and controlled access through robust validation and authentication mechanisms.

---

## 🏗️ Features

- **Secure RESTful APIs**
    - Built with Spring Boot to handle core operations such as order creation, update, cancellation, and retrieval.
    - Implements clear and consistent HTTP response patterns and status codes.

- **Data Persistence**
    - Integrated with **MySQL** for reliable and scalable data storage.
    - Utilizes **Hibernate (JPA)** for efficient object-relational mapping and query abstraction.

- **Business Validation**
    - Ensures completeness and correctness of order data through validation logic.
    - Enforces business rules such as:
        - Required fields (e.g., customer ID, item details)
        - Non-negative pricing
        - Unique order IDs

- **Testing**
    - Implemented **JUnit** tests across multiple layers (service, controller, repository).
    - Validated edge cases to prevent regressions and ensure system reliability.

---

## 🧩 Tech Stack

| Category | Technology |
|-----------|-------------|
| Language | Java |
| Framework | Spring Boot |
| ORM | Hibernate (JPA) |
| Database | MySQL |

| Testing | JUnit 5 |

<!--- **Security**
    - Configured **Spring Security** with **Role-Based Access Control (RBAC)**.
    - **Admins**: manage all orders.  
      **Users**: access only their own orders.
| Security | Spring Security (RBAC) |--->