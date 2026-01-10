# Bookstore Backend API - Design Document

**Version:** 1.0.0  
**Date:** 2026  
**Status:** Review-Ready

---

## Table of Contents

1. [Introduction](#1-introduction)
2. [Problem Statement](#2-problem-statement)
3. [High-Level Architecture](#3-high-level-architecture)
4. [Architecture Diagram](#4-architecture-diagram)
5. [Component Responsibilities](#5-component-responsibilities)
6. [Data Flow Explanation](#6-data-flow-explanation)
7. [Exception Handling Strategy](#7-exception-handling-strategy)
8. [API Documentation Strategy](#8-api-documentation-strategy)
9. [Testing & Quality Strategy](#9-testing--quality-strategy)
10. [Non-Functional Considerations](#10-non-functional-considerations)
11. [Assumptions & Limitations](#11-assumptions--limitations)
12. [Conclusion](#12-conclusion)

---

## 1. Introduction

### 1.1 Purpose of the Document

This design document provides a comprehensive technical overview of the Bookstore Backend API system. It is intended for technical reviewers, architects, and developers who need to understand the system's architecture, design decisions, and implementation approach without requiring direct code examination.

### 1.2 Scope of the System

The Bookstore Backend API is a RESTful web service that provides comprehensive book management capabilities. The system is designed as a backend-only service, focusing exclusively on API endpoints for book operations.

**In Scope:**
- RESTful API endpoints for book CRUD operations
- Book search and filtering capabilities
- Data persistence using SQLite database
- API documentation via OpenAPI/Swagger
- Comprehensive error handling
- Input validation
- Code quality and testing infrastructure

**Out of Scope (Intentionally):**
- User interface (UI) components
- User authentication and authorization
- Payment processing
- Order management
- Inventory management beyond basic quantity tracking
- Multi-tenancy or organization-level isolation

### 1.3 What the Bookstore Application Does

The Bookstore Backend API enables API consumers to:

- **Create** new book entries with complete metadata (title, author, ISBN, price, quantity)
- **Retrieve** books by ID, ISBN, author, or title search
- **Update** books either fully (PUT) or partially (PATCH)
- **Delete** books from the system
- **Search** and filter books using various criteria
- **Discover** available APIs through interactive Swagger UI documentation

The system maintains data integrity through validation, enforces business rules (e.g., unique ISBN constraint), and provides consistent error responses for all failure scenarios.

---

## 2. Problem Statement

### 2.1 Business Problem

Modern bookstore operations require efficient, programmatic access to book inventory data. Traditional manual book management systems lack the flexibility and integration capabilities needed for:

- **Inventory Management Systems**: Automated systems that need to query and update book information
- **E-commerce Platforms**: Online stores requiring real-time book availability and pricing
- **Library Management Systems**: Institutions managing large book catalogs
- **Analytics and Reporting Tools**: Systems aggregating book data for business intelligence

The absence of a standardized, RESTful API creates integration challenges, manual data entry overhead, and inconsistent data access patterns across different systems.

### 2.2 Intended Users (API Consumers)

The primary users of this system are:

1. **Backend Developers**: Integrating the bookstore API into larger applications
2. **Frontend Developers**: Building user interfaces that consume book data
3. **System Integrators**: Connecting the bookstore with external systems (inventory, payment, shipping)
4. **QA Engineers**: Testing book management workflows
5. **DevOps Engineers**: Monitoring and maintaining the service

All interactions occur programmatically via HTTP requests; there is no direct human user interface.

### 2.3 Core Use Cases

The system supports the following primary use cases:

1. **Create Book**: Add a new book to the catalog with all required metadata
2. **Retrieve All Books**: Fetch a complete list of all books in the system
3. **Retrieve Book by ID**: Get detailed information about a specific book using its unique identifier
4. **Retrieve Book by ISBN**: Look up a book using its International Standard Book Number
5. **Search Books by Author**: Find all books written by a specific author
6. **Search Books by Title**: Perform partial text matching on book titles
7. **Update Book (Full)**: Replace all book information with new values
8. **Update Book (Partial)**: Modify specific fields (e.g., price, quantity) without changing others
9. **Delete Book**: Remove a book from the system permanently
10. **API Discovery**: Explore available endpoints and test them interactively via Swagger UI

---

## 3. High-Level Architecture

### 3.1 System Type

The Bookstore application is a **backend REST API service** built using the Spring Boot framework. It follows a stateless, request-response pattern where clients send HTTP requests and receive JSON responses.

### 3.2 Key Technologies

The system is built using the following technology stack:

- **Java 17**: Modern Java language features and performance improvements
- **Spring Boot 4.0.0**: Framework providing auto-configuration, embedded server, and production-ready features
- **Spring Web MVC**: RESTful web service framework for handling HTTP requests
- **Spring Data JDBC**: Lightweight data access layer for database operations
- **SQLite**: File-based relational database for simplicity and portability
- **Maven**: Build automation and dependency management tool
- **Lombok**: Reduces boilerplate code (getters, setters, constructors)
- **Jakarta Validation**: Input validation framework
- **SpringDoc OpenAPI**: API documentation generation and Swagger UI integration
- **JaCoCo**: Code coverage analysis tool
- **SonarQube/SonarCloud**: Static code analysis and quality gates

### 3.3 Architectural Pattern: Layered Architecture

The system employs a **layered architecture** (also known as n-tier architecture) to achieve separation of concerns, maintainability, and testability. This pattern is chosen because:

1. **Separation of Concerns**: Each layer has a distinct responsibility, making the codebase easier to understand and modify
2. **Testability**: Layers can be tested independently using mocks and stubs
3. **Maintainability**: Changes in one layer (e.g., database schema) don't directly impact other layers (e.g., API contracts)
4. **Scalability**: Individual layers can be optimized or replaced without affecting the entire system
5. **Industry Standard**: Aligns with Spring Boot best practices and enterprise application patterns

The architecture consists of the following layers (from top to bottom):

1. **Controller Layer**: Handles HTTP requests and responses
2. **Service Interface Layer**: Defines business logic contracts
3. **Service Implementation Layer**: Contains business logic and orchestration
4. **Mapper Layer**: Transforms between DTOs and entities
5. **Repository Layer**: Abstracts database access
6. **Database Layer**: SQLite database for persistence

Each layer communicates only with adjacent layers, maintaining clear boundaries and dependencies.

---

## 4. Architecture Diagram

### 4.1 Visual Architecture Overview

The following textual description can be used to recreate the architecture diagram in tools like draw.io or Lucidchart:

```
┌─────────────────────────────────────────────────────────────────┐
│                         CLIENT LAYER                             │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐          │
│  │   Swagger UI │  │   Postman    │  │  API Client  │          │
│  │  (Browser)   │  │              │  │  (Custom)    │          │
│  └──────┬───────┘  └──────┬───────┘  └──────┬───────┘          │
│         │                 │                 │                   │
└─────────┼─────────────────┼─────────────────┼───────────────────┘
          │                 │                 │
          │         HTTP/REST Requests       │
          │         (JSON Payloads)           │
          │                 │                 │
          ▼                 ▼                 ▼
┌─────────────────────────────────────────────────────────────────┐
│                    CONTROLLER LAYER                              │
│  ┌──────────────────────────┐  ┌──────────────────────────┐    │
│  │   BookController         │  │   BookstoreController    │    │
│  │   - GET /api/books       │  │   - GET /                │    │
│  │   - GET /api/books/{id}  │  │   - GET /health          │    │
│  │   - POST /api/books     │  │                          │    │
│  │   - PUT /api/books/{id} │  │                          │    │
│  │   - PATCH /api/books/{id}│  │                          │    │
│  │   - DELETE /api/books/{id}│ │                          │    │
│  └───────────┬──────────────┘  └──────────────────────────┘    │
│              │                                                   │
│              │  Validates Input                                  │
│              │  Maps HTTP to Service Calls                      │
│              │                                                   │
└──────────────┼───────────────────────────────────────────────────┘
               │
               │  Service Method Calls
               │
               ▼
┌─────────────────────────────────────────────────────────────────┐
│                  SERVICE INTERFACE LAYER                         │
│  ┌──────────────────────────────────────────────────────────┐   │
│  │              BookService (Interface)                     │   │
│  │  - getAllBooks()                                         │   │
│  │  - getBookById(Long id)                                  │   │
│  │  - getBookByIsbn(String isbn)                            │   │
│  │  - getBooksByAuthor(String author)                      │   │
│  │  - searchBooksByTitle(String title)                     │   │
│  │  - createBook(BookRequest)                              │   │
│  │  - updateBook(Long id, BookRequest)                     │   │
│  │  - patchBook(Long id, BookUpdateRequest)                │   │
│  │  - deleteBook(Long id)                                  │   │
│  └───────────────────────┬────────────────────────────────────┘   │
└──────────────────────────┼────────────────────────────────────────┘
                           │
                           │  Implements
                           │
                           ▼
┌─────────────────────────────────────────────────────────────────┐
│              SERVICE IMPLEMENTATION LAYER                         │
│  ┌──────────────────────────────────────────────────────────┐   │
│  │           BookServiceImpl                                 │   │
│  │  - Business Logic                                        │   │
│  │  - Validation Rules                                      │   │
│  │  - Transaction Management                                │   │
│  │  - Exception Handling                                    │   │
│  └───────┬───────────────────────────────┬───────────────────┘   │
│          │                               │                        │
│          │  Uses                         │  Uses                  │
│          │                               │                        │
└──────────┼───────────────────────────────┼────────────────────────┘
           │                               │
           ▼                               ▼
┌──────────────────────────┐  ┌──────────────────────────┐
│      MAPPER LAYER        │  │   REPOSITORY LAYER       │
│  ┌────────────────────┐  │  │  ┌────────────────────┐ │
│  │   BookMapper       │  │  │  │  BookRepository    │ │
│  │  (Static Methods)  │  │  │  │  (Spring Data JDBC)│ │
│  │                    │  │  │  │                     │ │
│  │  - toEntity()     │  │  │  │  - findAll()        │ │
│  │  - toResponse()   │  │  │  │  - findById()       │ │
│  │  - updateEntity() │  │  │  │  - findByIsbn()     │ │
│  └────────────────────┘  │  │  │  - findByAuthor()  │ │
│                          │  │  │  - save()          │ │
│  Converts:               │  │  │  - deleteById()     │ │
│  DTO ↔ Entity           │  │  └──────────┬───────────┘ │
└──────────────────────────┘  │            │             │
                               │            │             │
                               │            │  SQL Queries│
                               │            │             │
                               │            ▼             │
                               │  ┌────────────────────┐ │
                               │  │  NamedParameter    │ │
                               │  │  JdbcTemplate     │ │
                               │  │  (Custom Queries) │ │
                               │  └──────────┬───────────┘ │
                               └─────────────┼─────────────┘
                                             │
                                             │  JDBC
                                             │
                                             ▼
                    ┌──────────────────────────────────────┐
                    │         DATABASE LAYER               │
                    │  ┌────────────────────────────────┐  │
                    │  │      SQLite Database           │  │
                    │  │      (bookstore.db)            │  │
                    │  │                                │  │
                    │  │  Table: books                 │  │
                    │  │  - id (PK)                    │  │
                    │  │  - title                      │  │
                    │  │  - author                     │  │
                    │  │  - isbn (UNIQUE)              │  │
                    │  │  - price                      │  │
                    │  │  - quantity                   │  │
                    │  │  - created_at                 │  │
                    │  │  - updated_at                 │  │
                    │  └────────────────────────────────┘  │
                    └──────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────┐
│              GLOBAL EXCEPTION HANDLER                           │
│  ┌──────────────────────────────────────────────────────────┐  │
│  │        GlobalExceptionHandler                            │  │
│  │  (@RestControllerAdvice)                                  │  │
│  │                                                          │  │
│  │  Catches exceptions from all layers and converts        │  │
│  │  them to standardized ErrorResponse DTOs                │  │
│  └──────────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────────┘
```

### 4.2 Request/Response Flow

**Request Flow (e.g., Create Book):**

1. **Client** sends HTTP POST request to `/api/books` with JSON payload
2. **Controller** receives request, validates input using Jakarta Validation
3. **Controller** calls `bookService.createBook(bookRequest)`
4. **Service Implementation** executes business logic:
   - Checks for duplicate ISBN
   - Maps `BookRequest` DTO to `Book` entity using `BookMapper`
   - Persists entity via `BookRepository`
   - Maps entity back to `BookResponse` DTO
5. **Repository** executes SQL INSERT via Spring Data JDBC
6. **Database** stores the record and returns generated ID
7. **Response** flows back through layers: Repository → Service → Controller
8. **Controller** returns HTTP 201 Created with `BookResponse` JSON

**Exception Flow:**

- If any exception occurs, `GlobalExceptionHandler` intercepts it
- Exception is converted to `ErrorResponse` DTO with appropriate HTTP status
- Error response is returned to client

### 4.3 Key Architectural Components

- **Controllers**: Entry points for HTTP requests, handle routing and HTTP concerns
- **Service Interfaces**: Define contracts for business operations
- **Service Implementations**: Contain business logic, validation, and orchestration
- **Mappers**: Transform data between DTOs (API layer) and Entities (persistence layer)
- **Repositories**: Abstract database access, provide CRUD operations
- **Database**: SQLite file-based database for data persistence
- **Global Exception Handler**: Centralized error handling across all layers

---

## 5. Component Responsibilities

### 5.1 Controller Layer

**Responsibilities:**
- **HTTP Request Handling**: Receives and processes HTTP requests (GET, POST, PUT, PATCH, DELETE)
- **Input Validation**: Validates request parameters and request bodies using Jakarta Validation annotations (`@Valid`, `@NotBlank`, etc.)
- **HTTP Response Management**: Constructs appropriate HTTP responses with correct status codes (200, 201, 204, 400, 404, 409, 500)
- **API Documentation**: Provides OpenAPI/Swagger annotations for API documentation
- **Content Negotiation**: Handles JSON request/response serialization/deserialization
- **Path Mapping**: Maps URL paths to service methods using Spring MVC annotations

**Key Classes:**
- `BookController`: Handles all book-related endpoints (`/api/books/*`)
- `BookstoreController`: Handles root-level endpoints (`/`, `/health`)

**Design Decision**: Controllers are kept thin, delegating all business logic to the service layer. This ensures controllers focus solely on HTTP concerns and remain easily testable.

### 5.2 Service Interface Layer

**Responsibilities:**
- **Contract Definition**: Defines the business logic contract without implementation details
- **Abstraction**: Provides abstraction between controllers and implementations, enabling easy mocking for testing
- **API Documentation**: Documents expected behavior, parameters, return types, and exceptions

**Key Interfaces:**
- `BookService`: Defines all book-related business operations

**Design Decision**: Using interfaces allows for multiple implementations, easier testing with mocks, and follows the Dependency Inversion Principle.

### 5.3 Service Implementation Layer

**Responsibilities:**
- **Business Logic**: Implements core business rules (e.g., ISBN uniqueness validation)
- **Orchestration**: Coordinates calls to repositories and mappers
- **Transaction Management**: Manages database transactions using `@Transactional` annotations
- **Exception Handling**: Throws domain-specific exceptions (`ResourceNotFoundException`, `DuplicateResourceException`)
- **Data Transformation Coordination**: Uses mappers to convert between DTOs and entities

**Key Classes:**
- `BookServiceImpl`: Implements all book service operations

**Design Decision**: Business logic is centralized in the service layer, ensuring consistency and making it easier to modify business rules without affecting API contracts or database schema.

### 5.4 Mapper Layer

**Responsibilities:**
- **DTO to Entity Conversion**: Transforms `BookRequest` DTOs to `Book` entities
- **Entity to DTO Conversion**: Transforms `Book` entities to `BookResponse` DTOs
- **Partial Update Mapping**: Handles partial updates from `BookUpdateRequest` to `Book` entities
- **Null Safety**: Handles null values gracefully to prevent `NullPointerException`

**Key Classes:**
- `BookMapper`: Static utility class with mapping methods

**Design Decision**: Mappers are implemented as static utility classes to keep them stateless and easily testable. This separation ensures that transformation logic is isolated and reusable.

### 5.5 Repository Layer

**Responsibilities:**
- **Data Access Abstraction**: Provides an abstraction layer over database operations
- **CRUD Operations**: Implements Create, Read, Update, Delete operations
- **Query Methods**: Defines custom query methods (e.g., `findByIsbn`, `findByAuthor`)
- **SQL Execution**: Executes SQL queries via Spring Data JDBC

**Key Interfaces:**
- `BookRepository`: Extends `CrudRepository<Book, Long>` and defines custom query methods

**Design Decision**: Using Spring Data JDBC repositories reduces boilerplate code while maintaining control over SQL queries. Custom queries are defined using `@Query` annotations for complex operations.

### 5.6 DTO Usage

**Data Transfer Objects (DTOs)** are used to:

- **Decouple API Contracts from Database Schema**: Changes to database entities don't directly affect API responses
- **Control Exposed Data**: Only necessary fields are exposed in responses (e.g., `BookResponse` includes `id`, `createdAt`, `updatedAt` which aren't in requests)
- **Validation**: Request DTOs contain validation annotations (`@NotBlank`, `@Positive`, etc.)
- **API Documentation**: DTOs are annotated with OpenAPI/Swagger annotations for documentation

**Key DTOs:**
- `BookRequest`: Input DTO for creating/updating books (all fields required)
- `BookUpdateRequest`: Input DTO for partial updates (all fields optional)
- `BookResponse`: Output DTO containing book data returned to clients
- `ErrorResponse`: Standardized error response structure

**Design Decision**: DTOs prevent exposing internal entity structure, allow versioning of API contracts independently from database schema, and provide a clear contract for API consumers.

### 5.7 Utility and Configuration Components

**Configuration Classes:**
- `OpenApiConfig`: Configures OpenAPI/Swagger documentation metadata
- `DatabaseConfig`: Configures database connection and SQLite-specific settings
- `SqliteDialect`: Custom SQL dialect for SQLite compatibility

**Exception Classes:**
- `ResourceNotFoundException`: Thrown when a requested resource doesn't exist (404)
- `DuplicateResourceException`: Thrown when attempting to create/update with duplicate data (409)
- `ValidationException`: Thrown for validation failures (400)

**Design Decision**: Configuration is externalized using Spring Boot's auto-configuration with custom overrides where needed. Custom exceptions provide clear, domain-specific error handling.

---

## 6. Data Flow Explanation

This section describes the step-by-step flow for the **Create Book** use case, which demonstrates the complete request lifecycle through all layers.

### 6.1 Create Book Flow

**Step 1: Client Request**
- Client sends HTTP POST request to `http://localhost:8080/api/books`
- Request body contains JSON: `{"title": "The Great Gatsby", "author": "F. Scott Fitzgerald", "isbn": "978-0-7432-7356-5", "price": 12.99, "quantity": 50}`
- Content-Type header: `application/json`

**Step 2: Controller Receives Request**
- `BookController.createBook()` method is invoked via Spring MVC routing
- Spring automatically deserializes JSON to `BookRequest` DTO
- `@Valid` annotation triggers Jakarta Validation framework

**Step 3: Input Validation**
- Validation framework checks `BookRequest` fields:
  - `title`: Must not be blank, max 500 characters
  - `author`: Must not be blank, max 200 characters
  - `isbn`: Must not be blank, max 20 characters
  - `price`: Must not be null, must be positive
  - `quantity`: Must not be null, must be zero or positive
- If validation fails, `MethodArgumentNotValidException` is thrown and handled by `GlobalExceptionHandler`, returning HTTP 400 with validation errors

**Step 4: Service Layer Invocation**
- Controller calls `bookService.createBook(bookRequest)`
- `BookServiceImpl.createBook()` method is executed
- Method is annotated with `@Transactional` to ensure atomicity

**Step 5: Business Logic Validation**
- Service checks if ISBN already exists: `bookRepository.findByIsbn(bookRequest.getIsbn())`
- If ISBN exists, `DuplicateResourceException` is thrown (HTTP 409 Conflict)
- This is a business rule validation beyond input validation

**Step 6: Entity Mapping**
- Service calls `BookMapper.toEntity(bookRequest)` to convert DTO to entity
- Mapper creates new `Book` entity and sets:
  - Title, author, ISBN, price, quantity from request
  - `createdAt` and `updatedAt` timestamps set to current time
  - `id` is null (will be generated by database)

**Step 7: Database Persistence**
- Service uses `NamedParameterJdbcTemplate` for custom INSERT query (due to SQLite's `last_insert_rowid()` requirement)
- SQL INSERT statement: `INSERT INTO books (title, author, isbn, price, quantity, created_at, updated_at) VALUES (...)`
- Database executes INSERT and generates auto-increment ID

**Step 8: Retrieve Generated ID**
- Service queries `SELECT last_insert_rowid()` to get the generated ID
- ID is set on the `Book` entity: `book.setId(generatedId)`

**Step 9: Response Mapping**
- Service calls `BookMapper.toResponse(book)` to convert entity to DTO
- Mapper creates `BookResponse` DTO with all fields including ID and timestamps

**Step 10: Controller Response**
- Controller receives `BookResponse` from service
- Controller returns `ResponseEntity.status(HttpStatus.CREATED).body(bookResponse)`
- Spring serializes `BookResponse` to JSON

**Step 11: Client Receives Response**
- HTTP 201 Created status code
- Response body contains JSON: `{"id": 1, "title": "The Great Gatsby", "author": "F. Scott Fitzgerald", "isbn": "978-0-7432-7356-5", "price": 12.99, "quantity": 50, "createdAt": "2024-01-15T10:30:00", "updatedAt": "2024-01-15T10:30:00"}`

### 6.2 Error Scenarios

**Scenario 1: Validation Failure**
- If `price` is negative, validation fails at Step 3
- `GlobalExceptionHandler` catches `MethodArgumentNotValidException`
- Returns HTTP 400 with detailed validation errors

**Scenario 2: Duplicate ISBN**
- If ISBN already exists, business logic validation fails at Step 5
- `DuplicateResourceException` is thrown
- `GlobalExceptionHandler` catches it and returns HTTP 409 Conflict

**Scenario 3: Database Error**
- If database connection fails, exception propagates to `GlobalExceptionHandler`
- Generic exception handler returns HTTP 500 Internal Server Error

---

## 7. Exception Handling Strategy

### 7.1 Centralized Exception Handling

The system employs a **centralized exception handling strategy** using Spring's `@RestControllerAdvice` annotation. All exceptions are caught by `GlobalExceptionHandler` and converted to standardized `ErrorResponse` DTOs.

**Why Centralized Handling?**

1. **Consistency**: All error responses follow the same structure, making it easier for API consumers to handle errors
2. **Separation of Concerns**: Controllers and services don't need to handle HTTP response construction
3. **Maintainability**: Error handling logic is in one place, making it easy to modify error response formats
4. **Security**: Prevents leaking internal implementation details (stack traces, internal exceptions) to clients
5. **Compliance**: Ensures proper HTTP status codes are returned for different error types

### 7.2 Exception Types and HTTP Status Codes

The system handles the following exception types:

| Exception Type | HTTP Status | Use Case | Example |
|---------------|-------------|----------|---------|
| `ResourceNotFoundException` | 404 Not Found | Requested resource doesn't exist | Book with ID 123 not found |
| `DuplicateResourceException` | 409 Conflict | Attempting to create/update with duplicate data | ISBN already exists |
| `ValidationException` | 400 Bad Request | Custom validation failures | Business rule violation |
| `MethodArgumentNotValidException` | 400 Bad Request | Jakarta Validation failures | Missing required field |
| `ConstraintViolationException` | 400 Bad Request | Path variable/query param validation | Blank ISBN in path |
| `IllegalArgumentException` | 400 Bad Request | Invalid method arguments | Invalid date format |
| `Exception` (Generic) | 500 Internal Server Error | Unexpected errors | Database connection failure |

### 7.3 Error Response Structure

All error responses follow a consistent structure defined by `ErrorResponse` DTO:

```json
{
  "timestamp": "2024-01-15T10:30:00",
  "status": 404,
  "error": "Resource Not Found",
  "message": "Book with ID 123 not found",
  "path": "/api/books/123"
}
```

**Fields:**
- `timestamp`: When the error occurred (ISO 8601 format)
- `status`: HTTP status code
- `error`: Error category/type (human-readable)
- `message`: Detailed error message
- `path`: API endpoint where error occurred

**Validation Error Response** (for `MethodArgumentNotValidException`):

```json
{
  "timestamp": "2024-01-15T10:30:00",
  "status": 400,
  "error": "Validation Failed",
  "message": "Request validation failed",
  "errors": {
    "title": "Title is required",
    "price": "Price must be positive"
  },
  "path": "/api/books"
}
```

### 7.4 Exception Propagation Flow

1. **Exception Thrown**: Service layer throws domain exception (e.g., `ResourceNotFoundException`)
2. **Exception Propagation**: Exception propagates up through service → controller
3. **Global Handler Intercepts**: `GlobalExceptionHandler` catches exception via `@ExceptionHandler` method
4. **Error Response Creation**: Handler creates `ErrorResponse` DTO with appropriate status code
5. **HTTP Response**: Handler returns `ResponseEntity<ErrorResponse>` with correct HTTP status
6. **Client Receives**: Client receives JSON error response

**Design Decision**: Exceptions are not caught and handled within individual methods. Instead, they propagate to the global handler, ensuring consistent error handling and reducing code duplication.

---

## 8. API Documentation Strategy

### 8.1 OpenAPI/Swagger Integration

The system uses **SpringDoc OpenAPI** (version 2.7.0) to automatically generate API documentation from code annotations. This approach ensures that documentation stays synchronized with code changes.

**Configuration:**
- OpenAPI metadata is configured in `OpenApiConfig` class
- API documentation is accessible at `/v3/api-docs` (JSON format)
- Interactive Swagger UI is available at `/swagger-ui.html`

### 8.2 Documentation Approach

**Code-First Documentation:**
- Controllers are annotated with `@Tag` for grouping endpoints
- Each endpoint method uses `@Operation` for summary and description
- Request/response DTOs use `@Schema` annotations for field descriptions
- `@ApiResponses` annotations document all possible HTTP responses
- `@Parameter` annotations describe path variables and query parameters
- `@ExampleObject` annotations provide example request/response payloads

**Benefits:**
- Documentation is generated automatically, reducing maintenance overhead
- Documentation cannot become outdated since it's derived from code
- Developers are encouraged to document APIs as they write code
- Swagger UI provides interactive testing capabilities

### 8.3 Swagger UI as Interaction Layer

**Swagger UI** serves as the primary interface for API consumers to:

1. **Discover APIs**: Browse all available endpoints organized by tags
2. **Understand Contracts**: View request/response schemas, required fields, and data types
3. **Test APIs**: Execute API calls directly from the browser with real-time responses
4. **View Examples**: See example requests and responses for each endpoint
5. **Validate Requests**: Understand validation rules before making actual API calls

**Access Points:**
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

### 8.4 API Consumer Workflow

1. **Discovery**: Consumer navigates to Swagger UI
2. **Exploration**: Consumer browses available endpoints and reads descriptions
3. **Understanding**: Consumer reviews request/response schemas and examples
4. **Testing**: Consumer uses "Try it out" feature to test endpoints
5. **Integration**: Consumer uses documented schemas to build API clients

**Design Decision**: Using OpenAPI/Swagger eliminates the need for separate API documentation files (e.g., Word documents, wikis) that can become outdated. The interactive nature of Swagger UI reduces the learning curve for new API consumers.

---

## 9. Testing & Quality Strategy

### 9.1 Unit Testing Approach

The system employs a **comprehensive unit testing strategy** focusing on the service layer, which contains the core business logic.

**Testing Framework:**
- **JUnit 5**: Modern testing framework for Java
- **Mockito**: Mocking framework for isolating units under test
- **AssertJ**: Fluent assertions for readable test code

**Test Structure:**
- Tests are organized in `src/test/java` mirroring the main source structure
- Each test class corresponds to a production class (e.g., `BookServiceTest` tests `BookServiceImpl`)
- Tests use descriptive names following the pattern: `should[ExpectedBehavior]When[Condition]`

**Example Test Pattern:**
```java
@Test
@DisplayName("Should throw ResourceNotFoundException when ID does not exist")
void shouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
    // Arrange
    when(bookRepository.findById(1L)).thenReturn(Optional.empty());
    
    // Act & Assert
    assertThatThrownBy(() -> bookService.getBookById(1L))
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessageContaining("Book not found with id: 1");
}
```

### 9.2 Service Layer Testing Focus

**Why Focus on Service Layer?**

1. **Business Logic Concentration**: Service layer contains the most critical business rules
2. **Testability**: Service layer can be easily tested with mocked repositories
3. **Coverage**: Testing service layer provides high confidence in system correctness
4. **Maintainability**: Service tests serve as living documentation of business rules

**Test Coverage Areas:**
- **Happy Paths**: Successful operations (create, read, update, delete)
- **Error Scenarios**: Exception handling (not found, duplicates, validation)
- **Edge Cases**: Null values, empty lists, boundary conditions
- **Business Rules**: ISBN uniqueness, partial updates, etc.

**Mocking Strategy:**
- Repository layer is mocked to isolate service logic
- Database interactions are not tested in unit tests (covered by integration tests)
- Mapper layer is tested separately (utility class tests)

### 9.3 Code Coverage with JaCoCo

**JaCoCo Integration:**
- JaCoCo Maven plugin is configured to generate code coverage reports
- Coverage is measured during test execution
- Reports are generated in `target/site/jacoco/` directory
- HTML reports provide visual coverage analysis

**Coverage Exclusions:**
- DTOs (`**/dto/**`): Simple data structures don't require coverage
- Entities (`**/entity/**`): Plain Java objects with getters/setters
- Configuration (`**/config/**`): Spring configuration classes
- Application main class: Bootstrap code

**Coverage Goals:**
- Service layer: High coverage (>80%) to ensure business logic is tested
- Controller layer: Moderate coverage focusing on request handling
- Repository layer: Covered by integration tests

### 9.4 Static Code Analysis with SonarQube/SonarCloud

**SonarQube Integration:**
- SonarQube Maven plugin is configured for static code analysis
- Analysis can be run locally or integrated with SonarCloud for CI/CD
- Configuration is defined in `sonar-project.properties`

**Analysis Areas:**
- **Code Smells**: Maintainability issues, code duplication
- **Bugs**: Potential runtime errors
- **Security Vulnerabilities**: Security hotspots and vulnerabilities
- **Technical Debt**: Code complexity, maintainability index
- **Coverage**: Integration with JaCoCo reports

**Quality Gates:**
- Code coverage thresholds
- Duplication limits
- Maintainability ratings
- Security vulnerability counts

**Design Decision**: Combining unit tests, code coverage, and static analysis ensures code quality at multiple levels. This multi-layered approach catches issues early and maintains high code standards.

---

## 10. Non-Functional Considerations

### 10.1 Maintainability

**Code Organization:**
- Clear package structure separating layers (controller, service, repository, dto, entity, exception)
- Consistent naming conventions following Java standards
- Comprehensive JavaDoc comments explaining purpose and behavior

**Separation of Concerns:**
- Each layer has distinct responsibilities
- DTOs separate API contracts from database schema
- Mappers isolate transformation logic
- Business logic is centralized in service layer

**Reduced Coupling:**
- Interfaces define contracts between layers
- Dependencies flow in one direction (controller → service → repository)
- No circular dependencies

**Maintainability Benefits:**
- Changes to database schema don't affect API contracts
- Business logic modifications are isolated to service layer
- New features can be added without modifying existing code extensively

### 10.2 Scalability

**Horizontal Scalability:**
- Stateless design allows multiple instances to run behind a load balancer
- No session state stored in application (stateless REST API)
- Database can be migrated to a shared database server (PostgreSQL, MySQL)

**Vertical Scalability:**
- Spring Boot's embedded Tomcat can handle moderate traffic
- Database connection pooling (via Spring Data JDBC) optimizes resource usage
- Transaction management ensures data consistency under load

**Scalability Limitations:**
- SQLite is file-based and not suitable for high-concurrency scenarios
- Current design is suitable for small to medium-scale applications
- For production scale, migration to PostgreSQL/MySQL is recommended

**Design Decision**: The system is designed to be scalable with minimal changes. The layered architecture allows swapping the database layer without affecting other components.

### 10.3 Testability

**Dependency Injection:**
- All dependencies are injected via constructors (no field injection)
- Enables easy mocking in tests
- Follows SOLID principles (Dependency Inversion)

**Mocking Support:**
- Service layer can be tested with mocked repositories
- Controllers can be tested with mocked services
- Integration tests can use test databases

**Test Isolation:**
- Unit tests don't require database setup
- Tests run quickly without external dependencies
- Each test is independent and can run in any order

**Testability Benefits:**
- Fast feedback loop during development
- Easy to add new tests for new features
- Tests serve as documentation of expected behavior

### 10.4 Code Quality

**Standards Compliance:**
- Follows Java naming conventions
- Consistent code formatting
- Comprehensive error handling

**Code Review Readiness:**
- Clear separation of concerns makes code easy to review
- Well-documented APIs and methods
- Consistent patterns throughout codebase

**Quality Tools:**
- SonarQube for static analysis
- JaCoCo for coverage metrics
- Maven for build and dependency management

### 10.5 CI/CD Readiness

**Build Automation:**
- Maven provides standardized build process
- Tests run automatically during build
- Coverage reports generated automatically

**Quality Gates:**
- SonarQube can be integrated into CI/CD pipeline
- Coverage thresholds can be enforced
- Static analysis can block merges if quality degrades

**Deployment Readiness:**
- Spring Boot creates executable JAR files
- Embedded server eliminates need for external application server
- Configuration externalized via `application.properties`

**CI/CD Integration Points:**
- Maven build can be triggered on commits
- Tests run automatically
- SonarQube analysis can be integrated
- Deployment can be automated to staging/production

---

## 11. Assumptions & Limitations

### 11.1 No User Interface

**Assumption:** The system is designed exclusively as a backend API service. There is no user interface, and all interactions occur programmatically via HTTP requests.

**Implications:**
- API consumers must build their own UIs or integrate the API into existing systems
- Swagger UI serves as the only interactive interface for testing and exploration
- No user authentication/authorization is implemented (all endpoints are public)

**Future Considerations:**
- If UI is needed, it can be built as a separate frontend application consuming this API
- Authentication can be added using Spring Security without modifying core business logic

### 11.2 Backend Service Only

**Assumption:** This system is intended to be consumed by other applications or services, not end users directly.

**Implications:**
- API design prioritizes programmatic access over human readability
- Error messages are structured for programmatic parsing
- No user-facing features (e.g., password reset, email notifications)

### 11.3 Database Assumptions

**Current Implementation:**
- SQLite file-based database (`bookstore.db`)
- Suitable for development, testing, and small-scale deployments
- Single-file database simplifies deployment and backup

**Limitations:**
- Not suitable for high-concurrency scenarios
- No support for distributed deployments (file-based)
- Limited concurrent write performance

**Migration Path:**
- Database layer is abstracted via Spring Data JDBC
- Migration to PostgreSQL/MySQL requires only configuration changes
- No code changes needed for database migration

### 11.4 Deployment Assumptions

**Current State:**
- Application runs as standalone Spring Boot application
- Embedded Tomcat server handles HTTP requests
- Default port: 8080 (configurable)

**Production Considerations:**
- For production, consider:
  - External application server (Tomcat, Jetty)
  - Containerization (Docker)
  - Orchestration (Kubernetes)
  - Load balancing
  - Database migration to production-grade database

### 11.5 Security Assumptions

**Current State:**
- No authentication/authorization implemented
- All endpoints are publicly accessible
- No rate limiting or request throttling

**Security Limitations:**
- System is not suitable for production use without security enhancements
- Should add authentication (OAuth2, JWT) before production deployment
- Should implement HTTPS for encrypted communication
- Should add input sanitization and SQL injection prevention (handled by Spring Data JDBC)

### 11.6 Functional Limitations

**Intentionally Out of Scope:**
- User management and authentication
- Order processing
- Payment integration
- Inventory alerts (low stock notifications)
- Book categories or tags
- Book reviews or ratings
- Multi-language support
- Bulk operations (import/export)

**These limitations are by design** to keep the system focused on core book management functionality. Additional features can be added incrementally without major architectural changes.

---

## 12. Conclusion

### 12.1 Design Summary

The Bookstore Backend API is a well-architected RESTful service that demonstrates enterprise-level backend engineering practices. The system employs a layered architecture that separates concerns, promotes testability, and ensures maintainability.

**Key Strengths:**
- **Clear Architecture**: Layered design with distinct responsibilities
- **Comprehensive API**: Full CRUD operations with search and filtering
- **Robust Error Handling**: Centralized exception handling with consistent error responses
- **API Documentation**: Automatic OpenAPI/Swagger documentation
- **Quality Assurance**: Unit testing, code coverage, and static analysis
- **Maintainability**: Well-organized codebase following best practices

### 12.2 Alignment with Best Practices

The design aligns with several industry best practices:

1. **SOLID Principles**: Dependency injection, interface segregation, single responsibility
2. **RESTful Design**: Proper HTTP methods, status codes, resource naming
3. **Separation of Concerns**: Clear boundaries between layers
4. **Test-Driven Development**: Comprehensive unit tests with high coverage
5. **API-First Approach**: OpenAPI documentation drives API design
6. **Error Handling**: Consistent error responses following REST conventions
7. **Code Quality**: Static analysis and coverage metrics ensure quality

### 12.3 Production Readiness

**Current State:**
- System is suitable for development, testing, and small-scale deployments
- Core functionality is complete and well-tested
- Architecture supports future enhancements

**Production Considerations:**
Before production deployment, consider:
- Database migration to PostgreSQL/MySQL
- Authentication and authorization
- HTTPS/TLS encryption
- Rate limiting and request throttling
- Monitoring and logging (e.g., Spring Boot Actuator)
- Health checks and metrics
- Backup and disaster recovery procedures

### 12.4 Future Enhancements

The architecture supports easy addition of:
- User authentication and authorization
- Advanced search (full-text search, faceted search)
- Caching layer (Redis) for performance
- Event-driven architecture (message queues)
- Microservices decomposition (if needed)
- GraphQL API (alongside REST)

The layered architecture and clear separation of concerns ensure that these enhancements can be added without major refactoring.

---

## Appendix A: Technology Versions

- **Java**: 17
- **Spring Boot**: 4.0.0
- **Spring Data JDBC**: (via Spring Boot)
- **SQLite JDBC**: 3.44.1.0
- **Lombok**: (via Spring Boot)
- **SpringDoc OpenAPI**: 2.7.0
- **Jakarta Validation**: (via Spring Boot)
- **JUnit**: 5 (via Spring Boot Test)
- **Mockito**: (via Spring Boot Test)
- **JaCoCo**: 0.8.11
- **SonarQube Maven Plugin**: 3.11.0.3922

## Appendix B: API Endpoints Summary

| Method | Endpoint | Description | Status Codes |
|--------|----------|-------------|--------------|
| GET | `/api/books` | Get all books | 200, 500 |
| GET | `/api/books/{id}` | Get book by ID | 200, 400, 404, 500 |
| GET | `/api/books/isbn/{isbn}` | Get book by ISBN | 200, 400, 404, 500 |
| GET | `/api/books/author/{author}` | Get books by author | 200, 400, 500 |
| GET | `/api/books/search?title={title}` | Search books by title | 200, 400, 500 |
| POST | `/api/books` | Create new book | 201, 400, 409, 500 |
| PUT | `/api/books/{id}` | Update book (full) | 200, 400, 404, 409, 500 |
| PATCH | `/api/books/{id}` | Update book (partial) | 200, 400, 404, 409, 500 |
| DELETE | `/api/books/{id}` | Delete book | 204, 400, 404, 500 |
| GET | `/` | Home endpoint | 200 |
| GET | `/health` | Health check | 200 |

---

**Document End**
