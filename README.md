# Online Judge

## Overview
Online Judge is an automated code judging system built with Spring Boot. It allows users to register, log in, view programming problems, submit source code, and receive judged results. The backend processes submissions asynchronously, runs Java code inside isolated Docker containers, compares program output against test cases, and stores the results in PostgreSQL.

## Features
- Automated code evaluation per test case
- Docker-based sandboxed execution with disabled networking, CPU limits, and memory limits
- JWT authentication with stateless Spring Security
- Role-based access control for problem management APIs
- Submission result storage, including status, runtime, memory usage, errors, and per-test-case details

## Tech Stack
- Java 21
- Spring Boot 4.0.1
- Spring Web MVC, Spring Data JPA, Spring Security, Spring Validation
- PostgreSQL 
- Docker, Docker Java SDK (Judge image: `eclipse-temurin:17-jdk-alpine`)
- JWT 

## Architecture
Main system flow:

```text
User submits code
        ->
REST API (/api/submissions)
        ->
SubmissionService creates PENDING submission
        ->
JudgeService runs async job
        ->
DockerService writes Main.java + input.txt
        ->
Docker container compiles/runs code
        ->
Output is compared with expected output
        ->
Submission result is stored in PostgreSQL
```

Main modules:

| Module | Responsibility |
| --- | --- |
| `auth` | User registration, login, and current-user profile API |
| `user` | User entity, repository, and custom user details service |
| `problems` | Problem creation, problem listing, and test cases |
| `submissions` | Code submission intake, submission persistence, and judge results |
| `worker` | Asynchronous judging and Docker container execution |
| `contest` | Contest entities and repositories, currently under development |
| `common` | Security configuration, JWT filter, exception handling, and base entity |

## Getting Started

### Prerequisites
- Java 21
- Docker Desktop or Docker Engine
- PostgreSQL or Docker Compose
- Docker daemon TCP endpoint enabled at `tcp://localhost:2375` so the backend can communicate with Docker through the Docker Java SDK

### Installation
Clone the project:

```bash
git clone <repository-url>
cd online-judge-core/online-judge-core
```

Create a `.env` file in `online-judge-core/online-judge-core`:

```properties
DB_HOST=localhost
DB_PORT=5432
DB_NAME=judge_db
DB_USERNAME=postgres
DB_PASSWORD=your_secure_password

JWT_SECRET=your-secret-key-minimum-256-bits
JWT_EXPIRATION=86400000

ADMIN_USERNAME=admin
ADMIN_PASSWORD=secure_admin_password
ADMIN_EMAIL=admin@example.com

TEST_USERNAME=student1
TEST_PASSWORD=123456
TEST_EMAIL=student1@example.com
```

Start PostgreSQL with Docker Compose:

```bash
docker compose up -d
```

Build the project:

```bash
./gradlew build
```

Run the backend:

```bash
./gradlew bootRun
```

The API runs by default at:

```text
http://localhost:8080
```

## API Endpoints

| Method | Path | Auth | Description |
| --- | --- | --- | --- |
| `POST` | `/api/auth/register` | Public | Register a new user account |
| `POST` | `/api/auth/login` | Public | Log in and receive a JWT |
| `GET` | `/api/auth/me` | JWT | Get the current authenticated user |
| `GET` | `/api/problems` | Public | Get the problem list |
| `POST` | `/api/problems` | ADMIN | Create a new problem with test cases |
| `POST` | `/api/submissions` | JWT | Submit code for automated judging |
| `GET` | `/api/submissions` | JWT | Get the submission list |

Example request body for submitting code:

```json
{
  "problemId": "problem-id",
  "language": "JAVA",
  "sourceCode": "public class Main { public static void main(String[] args) { } }"
}
```

## TODO
- Support judging for more languages. Plan next: C++,Python.
- Apply separate time limits and memory limits from each problem definition
- Complete the contest module: contest creation, contestant registration, and leaderboard
- Add WebSocket or polling support so the frontend can track submission status in real time
- Add frontend :((
