# 🚀 User Management Backend (Spring Boot)

This is the backend service for the User Management Application built using **Spring Boot**. It connects to an external API to load user data and provides RESTful endpoints for user operations.

---

## 🛠 Setup Instructions

### 📋 Prerequisites
- **Java 21**
- **Maven** for dependency management
- **H2 Database** (in-memory storage)
- Internet connection to fetch external API data

---

### 🚀 How to Run the Backend

1. Navigate to the backend directory:
   ```bash
   cd User_Management_Java_Backend
   ```

2. Build and run the backend:
   ```bash
   mvn spring-boot:run
   ```

3. Access the backend service at:
   ```
   http://localhost:8080
   ```

---

### 🔄 Endpoints

| Method | Endpoint                          | Description                     |
|--------|------------------------------------|---------------------------------|
| POST   | `/api/users/load`                  | Load users from the API        |
| GET    | `/api/users/{id}`                  | Fetch user by ID               |
| GET    | `/api/users/email?email=<email>`   | Fetch user by email            |
| GET    | `/api/users?keyword=<keyword>`     | Search users by keyword        |

---

### 🛠 Data Source
Data is fetched from an external API:  
[https://dummyjson.com/users](https://dummyjson.com/users)

---

### 📑 Data Model
- **User:** Core entity containing embedded `Address`, `Company`, and other user details.
- **Address:** Shared embedded structure for user and company addresses.
- **Company:** Embedded in `User` and includes its own `Address`.

---

## 📚 Special Backend Features
- Embedded entities for `Address` and `Company` to handle address data at multiple levels.
- Bulk save operations for user data.
- `HttpURLConnection` used instead of Spring Beans for API requests.

---
