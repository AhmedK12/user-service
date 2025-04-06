# HealthAI-Harbor User Service

## Overview
HealthAI-Harbor User Service is responsible for user authentication, authorization, and management. It includes features like JWT-based authentication, role-based access control, and logging using Kafka.

## Features
- User authentication using JWT
- Role-based access control (RBAC)
- Secure password encoding with multiple algorithms
- Bloom filter optimization for user existence checks
- Kafka-based activity logging
- Admin control over user roles and permissions

## API Documentation

### Authentication APIs
#### 1. User Login
**Endpoint:** `POST /api/auth/login`  
**Description:** Authenticates a user and returns a JWT token.  
**Request Body:**
```json
{
  "username": "user@example.com",
  "password": "password123"
}
```
**Response:**
```json
{
  "accessToken": "jwt-token",
  "refreshToken": "refresh-token",
  "expiresIn": 3600
}
```

#### 2. User Registration
**Endpoint:** `POST /api/auth/register`  
**Description:** Registers a new user in the system.  
**Request Body:**
```json
{
  "username": "user@example.com",
  "password": "password123",
  "fullName": "John Doe",
  "roles": ["ROLE_USER"]
}
```
**Response:**
```json
{
  "message": "User registered successfully",
  "userId": 1
}
```

#### 3. Token Refresh
**Endpoint:** `POST /api/auth/refresh`  
**Description:** Refreshes the JWT token.  
**Request Body:**
```json
{
  "refreshToken": "existing-refresh-token"
}
```
**Response:**
```json
{
  "accessToken": "new-jwt-token",
  "expiresIn": 3600
}
```

### User Management APIs
#### 4. Get User By ID
**Endpoint:** `GET /user/{id}`  
**Description:** Fetches user details based on ID.  
**Headers:**
```
Authorization: Bearer <jwt-token>
```
**Response:**
```json
{
  "id": 1,
  "username": "user@example.com",
  "roles": ["ROLE_USER"]
}
```

#### 5. Get All Users (Admin Only)
**Endpoint:** `GET /admin/users`  
**Description:** Fetches a list of all users (Admin Access Required).  
**Headers:**
```
Authorization: Bearer <jwt-token>
```
**Response:**
```json
[
  {
    "id": 1,
    "username": "user1@example.com",
    "roles": ["ROLE_USER"]
  },
  {
    "id": 2,
    "username": "user2@example.com",
    "roles": ["ROLE_ADMIN"]
  }
]
```

#### 6. Update User Role (Admin Only)
**Endpoint:** `PUT /admin/users/{id}/roles`  
**Description:** Updates user roles (Admin Only).  
**Headers:**
```
Authorization: Bearer <jwt-token>
```
**Request Body:**
```json
{
  "roles": ["ROLE_ADMIN", "ROLE_USER"]
}
```
**Response:**
```json
{
  "message": "User roles updated successfully"
}
```

#### 7. Delete User (Admin Only)
**Endpoint:** `DELETE /admin/users/{id}`  
**Description:** Deletes a user from the system (Admin Access Required).  
**Headers:**
```
Authorization: Bearer <jwt-token>
```
**Response:**
```json
{
  "message": "User deleted successfully"
}
```

## Security & Logging
- **JWT Authentication**: Every request (except login/register) requires an `Authorization: Bearer <jwt-token>` header.
- **Role-Based Access**:
  - `ROLE_USER`: Can access own profile and perform basic actions.
  - `ROLE_ADMIN`: Can manage users (view, update, delete).
- **Kafka Logging**:
  - User activity is logged (Login, Logout, Role Updates, etc.).
  - Admin actions are logged separately.

## Installation
### Prerequisites
- Java 17+
- Spring Boot 3+
- PostgreSQL/MySQL (Database)
- Kafka (For logging events)

### Steps to Run
1. Clone the repository:
   ```sh
   git clone https://github.com/your-repo-url.git
   cd healthaiharbor-user-service
   ```
2. Configure the database in `application.properties`.
3. Run the application:
   ```sh
   mvn spring-boot:run
   ```
4. API will be available at `http://localhost:8080`.

## Contributing
Feel free to raise issues or submit PRs to improve this service.

## License
This project is licensed under the MIT License.
