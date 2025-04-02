# User Data API

A RESTful API for managing user data with JWT authentication and PostgreSQL database integration.

## Technologies

- **Java 21**
- **Spring Boot 3.x**
- **Spring Security** & **JWT** (Authentication)
- **PostgreSQL** (Database)
- **Spring Data JPA**
- **Maven** (Dependency Management)
- **Lombok** (Boilerplate Reduction)
- **RFC 7807 Problem Details** (Error Responses)

## Features

- CRUD operations for user management
- JWT-based authentication/authorization
- Request validation
- Custom error handling
- Age-based user filtering
- Average age calculation

---

## API Endpoints

| Method | Endpoint                         | Description                          | Auth Required |
|--------|----------------------------------|--------------------------------------|---------------|
| POST   | `/api/users/createUser`          | Create new user                      | Yes           |
| GET    | `/api/users/findById/{id}`       | Get user by ID                       | Yes           |
| POST   | `/api/users/updateUser`          | Update existing user                 | Yes           |
| GET    | `/api/users/deleteUserById/{id}` | Delete user by ID                    | Yes           |
| GET    | `/api/users/getUsersAVGAge`      | Get average age of all users         | Yes           |
| GET    | `/api/users/getUsersBetweenSpecifiedAge` | Filter users by age range    | Yes           |
