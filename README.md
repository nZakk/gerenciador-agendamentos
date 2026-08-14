# Barber Shop Appointment Manager

REST API for managing barber shop appointments, developed with Java and Spring Boot. The application allows users to create and cancel appointments, list existing appointments, and check available time slots.

This project was developed for educational purposes, with a focus on understanding the complete software development lifecycle — from implementation and automated testing to Docker containerization and production deployment.

## Architecture

The application follows a simple client-server architecture, separating the front-end, back-end, and database into independent components.

```text
User Browser
     |
     v
React + Vite Front-end
(Render Static Site)
     |
     | HTTPS / REST API
     v
Java + Spring Boot Back-end
(Render Web Service)
     |
     | Spring Data JPA / JDBC
     v
PostgreSQL Database
(Render PostgreSQL)
```

The front-end communicates with the back-end through HTTP requests using Axios. The Spring Boot API is responsible for handling requests, applying business rules, and accessing the database through Spring Data JPA.

In production, the back-end runs inside a Docker container and connects to PostgreSQL using environment variables for database credentials and configuration.

## Tech Stack

### Back-end

* **Java 21**
* **Spring Boot**
* **Spring Web** — REST API development
* **Spring Data JPA** — database persistence
* **Hibernate** — ORM implementation
* **PostgreSQL JDBC Driver** — communication between Java and PostgreSQL
* **SLF4J** — application logging
* **Spring Boot Actuator** — basic application health monitoring

### Front-end

* **React**
* **Vite**
* **Axios**
* **JavaScript**
* **HTML5**
* **CSS**

### Database

* **PostgreSQL** — production database
* **H2 Database** — in-memory database used during tests

### Testing

* **JUnit**
* **Mockito**
* **Spring Boot Test**
* **H2 in-memory database**

### Build and Deployment

* **Maven** — dependency management, compilation, testing, and JAR packaging
* **Docker** — application containerization
* **Git** — local version control
* **GitHub** — remote source-code repository
* **Render Web Service** — Spring Boot deployment
* **Render Static Site** — React deployment
* **Render PostgreSQL** — production database hosting
