# RepairConnect

RepairConnect is a full-stack local repair and service provider platform. Customers discover approved providers, request jobs, track booking status, and review completed work. Providers manage incoming requests, while admins monitor the marketplace and approve providers.

## Stack

- Frontend: React, Vite, JavaScript, React Router, Axios, Bootstrap 5, CSS
- Backend: Java 17, Spring Boot, Spring Web, Spring Data JPA, Hibernate, Spring Security, JWT, Maven
- Database: MySQL through XAMPP

## Architecture

`frontend/` is an independent Vite application calling the REST API at `http://localhost:8080/api`. `backend/` is an independent Spring Boot application. Hibernate creates and updates the `users`, `customers`, `service_providers`, `service_categories`, `services`, `bookings`, and `reviews` tables in `repairconnect_db`.

## Setup

1. Install Node.js, Java JDK 17+, Maven, and XAMPP.
2. Start Apache and MySQL in XAMPP.
3. Open phpMyAdmin and create a database named `repairconnect_db` (the JDBC URL also creates it when permitted).
4. Check `backend/src/main/resources/application.properties`. Override `DB_USERNAME`, `DB_PASSWORD`, or `DB_URL` as environment variables when needed.
5. Start the backend:

   ```powershell
   cd repairconnect/backend
   mvn spring-boot:run
   ```

6. Start the frontend in another terminal:

   ```powershell
   cd repairconnect/frontend
   npm install
   npm run dev
   ```

Open `http://localhost:5173`.

## Development accounts

The first backend start seeds these accounts with password `RepairConnect@123`:

| Role | Email |
| --- | --- |
| Admin | admin@repairconnect.com |
| Customer | customer@repairconnect.com |
| Provider | provider@repairconnect.com |

Change the development JWT secret and passwords before deploying anywhere public.

## API overview

- `POST /api/auth/register/customer`, `POST /api/auth/register/provider`, `POST /api/auth/login`
- `GET /api/providers`, `GET /api/providers/{id}`
- `GET /api/categories`
- `POST /api/bookings`, `GET /api/bookings/my`, `GET /api/bookings/provider`
- `PUT /api/bookings/{id}/accept|reject|start|complete|cancel`
- `POST /api/reviews`, `GET /api/reviews/provider/{providerId}`
- `GET /api/admin/dashboard`, `GET /api/admin/providers`, `PUT /api/admin/providers/{id}/approve|reject`

Authenticated requests use `Authorization: Bearer <token>`. Passwords are BCrypt-hashed and are never returned by the API.

## Folder structure

```text
repairconnect/
├── frontend/
│   └── src/{components,context,pages,services}
├── backend/
│   └── src/main/java/com/repairconnect/{config,controller,entity,repository,security}
└── database/
```
