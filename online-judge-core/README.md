# Online Judge System

An simple online code judge system built with Spring Boot 3, Java 21, and Docker. This system provides automated code evaluation with secure user authentication, role-based access control, and isolated code execution environment.

### Configure environment variables

Edit `.env` file with your configurations:

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
```

