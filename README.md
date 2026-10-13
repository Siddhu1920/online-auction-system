# BidSphere — Online Auction System

A runnable full-stack MVP based on the supplied project specification: Java 21 / Spring Boot 3, Spring Data JPA, MySQL, JWT authentication, React + TypeScript + Vite, and transactional bidding.

## Included in this starter
- Registration and login with BCrypt password hashing and JWT access tokens
- Roles (`BUYER`, `SELLER`, `ADMIN`) and protected API routes
- Create and browse auctions
- Bid submission with database row locking, minimum-increment validation, seller self-bid prevention, and server-side time checks
- Auction bid history and highest-bid display
- Scheduled auction completion / winner selection
- Responsive React marketplace, sign-in/register, create-auction form, auction details and bid form
- MySQL Docker Compose configuration and environment examples

This is a functional **MVP foundation**, not a payment-ready production marketplace. Simulated payments, admin moderation UI, image storage, email delivery, refresh-token rotation, rate limiting, and comprehensive automated tests are not included yet. Do not use it to process real payments or sensitive production data.

## Requirements
- JDK 21+
- Node.js 20+
- Docker Desktop (recommended) or MySQL 8+
- Maven 3.9+ (or use `./mvnw` after adding the Maven Wrapper)

## Run with Docker for MySQL
1. Copy `.env.example` to `.env` and set a long random `JWT_SECRET` (at least 32 characters).
2. Start MySQL:
   ```bash
   docker compose up -d mysql
   ```
3. Start backend in `backend/`:
   ```bash
   mvn spring-boot:run
   ```
4. Start frontend in `frontend/`:
   ```bash
   npm install
   npm run dev
   ```
5. Open http://localhost:5173. API base URL is http://localhost:8080/api/v1. Swagger UI: http://localhost:8080/swagger-ui.html

## Windows PowerShell
```powershell
Copy-Item .env.example .env
docker compose up -d mysql
cd backend
mvn spring-boot:run
```
Open another PowerShell window:
```powershell
cd frontend
npm install
npm run dev
```

Spring Boot reads environment variables from the process environment. If not using Docker Compose, set `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`, and `JWT_SECRET` in your terminal before starting the backend. Example:
```powershell
$env:DB_URL="jdbc:mysql://localhost:3306/bidsphere?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC"
$env:DB_USERNAME="bidsphere"
$env:DB_PASSWORD="change-me"
$env:JWT_SECRET="replace-this-with-a-random-secret-at-least-32-characters"
mvn spring-boot:run
```

## Demo accounts
Register accounts through the UI. Registration defaults to `BUYER`; users can select `SELLER` during signup for local demo purposes. Public self-registration for `ADMIN` is deliberately disabled. Create an admin directly in the database only for local development and never expose role escalation in production.

## Core API
- `POST /api/v1/auth/register` — `{ "name":"Demo Buyer", "email":"buyer@example.com", "password":"DemoPass123!", "role":"BUYER" }`
- `POST /api/v1/auth/login` — `{ "email":"buyer@example.com", "password":"DemoPass123!" }`
- `GET /api/v1/auctions`
- `GET /api/v1/auctions/{id}`
- `POST /api/v1/auctions` (SELLER/ADMIN)
- `GET /api/v1/auctions/{id}/bids`
- `POST /api/v1/auctions/{id}/bids` (authenticated buyer)
- `GET /api/v1/users/me`

## Notes
- Prices use `BigDecimal` / MySQL `DECIMAL`.
- Bid acceptance is decided by the backend, not the browser.
- Auction deadline checks use server time. Scheduler marks ended auctions and chooses the highest bid.
- JWT secret and database credentials must be changed before deployment.
- Configure `FRONTEND_ORIGIN` for the deployed frontend. Add HTTPS, observability, backups, security review, and production-grade rate limiting before deployment.
