# Setup & Installation Guide

## Prerequisites

- **Java 17 or higher**
- **Maven 3.6+**
- **Git**
- **Python 3** (optional, for serving frontend)
- **A modern web browser** (Chrome, Firefox, Safari, Edge)

### Verify Prerequisites

```bash
java -version
# Output should show Java 17 or higher

mvn -version
# Output should show Maven 3.6 or higher
```

---

## Installation Steps

### 1. Clone the Repository

```bash
git clone https://github.com/Nomdumiso/enviro365-investments.git
cd enviro365-investments
```

### 2. Build the Project

From the repository root:

```bash
mvn clean install
```

This will:
- Download all dependencies
- Compile the Java backend
- Run the test suite
- Package the application

**Expected Output:**
```
[INFO] BUILD SUCCESS
```

---

## Running the Application

### Step 1: Start the Backend Server

From the repository root:

```bash
mvn spring-boot:run
```

**Expected Output:**
```
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::        (v3.3.2)

2026-09-01 15:30:45.123 INFO: Enviro365Application : Starting Enviro365Application using Java 17.0.1
2026-09-01 15:30:46.456 INFO: TomcatWebServer : Tomcat started on port(s): 8080
2026-09-01 15:30:46.789 INFO: Enviro365Application : Started Enviro365Application in 1.234 seconds
```

**Backend is running at:** `http://localhost:8080`
- **REST API:** `http://localhost:8080/api`
- **H2 Console:** `http://localhost:8080/h2-console`

### Step 2: Start the Frontend

#### Option A: Python HTTP Server (Recommended)

Open a new terminal and run:

```bash
cd /path/to/enviro365-investments
python3 -m http.server 5500
```

**Expected Output:**
```
Serving HTTP on 0.0.0.0 port 5500 (http://0.0.0.0:5500/) ...
```

#### Option B: VS Code Live Server

1. Install the "Live Server" extension in VS Code
2. Right-click `index.html` → "Open with Live Server"

#### Option C: Direct Browser

Simply open `index.html` directly in your browser:
```
file:///path/to/enviro365-investments/index.html
```

### Step 3: Access the Application

Open your browser and navigate to:

- **Frontend:** `http://localhost:5500/index.html` (or `http://localhost:8000/index.html` depending on your server)
- **API Base:** `http://localhost:8080/api`
- **H2 Console:** `http://localhost:8080/h2-console`

---

## Database Access

### H2 Console

Access the in-memory H2 database at: `http://localhost:8080/h2-console`

**Connection Details:**
- **JDBC URL:** `jdbc:h2:mem:enviro365db`
- **User Name:** `sa`
- **Password:** (leave blank)

### Sample Queries

View all investors:
```sql
SELECT * FROM investors;
```

View all products:
```sql
SELECT * FROM products;
```

View all withdrawal notices:
```sql
SELECT * FROM withdrawal_notices;
```

---

## Running Tests

### Run All Tests

```bash
mvn test
```

### Run Specific Test Class

```bash
mvn test -Dtest=WithdrawalServiceTest
```

### Run with Coverage

```bash
mvn test jacoco:report
```

**Expected Output:**
```
[INFO] BUILD SUCCESS
[INFO] Tests run: 8, Failures: 0, Errors: 0, Skipped: 0
```

---

## Troubleshooting

### Issue: Port 8080 Already in Use

**Solution:** Kill the process or change the port:

```bash
# Linux/Mac
lsof -i :8080
kill -9 <PID>

# OR change port in application.properties
server.port=8081
```

### Issue: Maven Build Fails

**Solution:**

```bash
# Clear Maven cache
rm -rf ~/.m2/repository
# Rebuild
mvn clean install
```

### Issue: "Cannot find symbol" Compilation Error

**Solution:**

1. Ensure Java 17+ is installed
2. Clear IDE cache:
   - IntelliJ: File → Invalidate Caches → Restart
   - VS Code: Command Palette → Developer: Reload Window

### Issue: CORS Errors in Frontend

**Solution:** Ensure backend is running at `http://localhost:8080` and frontend is accessing `/api` endpoints correctly.

### Issue: H2 Database Empty

**Solution:** Check `data.sql` is in the repository root and the application restarted:

```bash
# Restart the backend
mvn spring-boot:run
```

---

## Project Structure

```
enviro365-investments/
├── pom.xml                           Maven configuration
├── application.properties            Spring Boot configuration
├── data.sql                          H2 seed data
├── README.md                         Project overview
├── API_DOCUMENTATION.md              API endpoints reference
├── SETUP_GUIDE.md                    This file
├── AI_USAGE.md                       AI tools used
│
├── index.html                        Frontend UI
├── app.js                            Frontend logic
├── api.js                            Frontend API wrapper
├── styles.css                        Frontend styling
│
└── src/main/java/com/enviro/assessment/junior/nomdumiso/
    ├── Enviro365Application.java     Spring Boot entry point
    ├── config/
    │   └── CorsConfig.java           CORS configuration
    ├── controller/
    │   ├── PortfolioController.java  Portfolio endpoints
    │   └── WithdrawalController.java Withdrawal endpoints
    ├── dto/
    │   ├── PortfolioDto.java
    │   ├── ProductDto.java
    │   ├── WithdrawalRequestDto.java
    │   └── WithdrawalResponseDto.java
    ├── entity/
    │   ├── Investor.java
    │   ├── Portfolio.java
    │   ├── Product.java
    │   ├── ProductType.java
    │   ├── WithdrawalNotice.java
    │   └── WithdrawalStatus.java
    ├── exception/
    │   ├── ApiError.java
    │   ├── BusinessRuleException.java
    │   └── ResourceNotFoundException.java
    ├── repository/
    │   ├── InvestorRepository.java
    │   ├── PortfolioRepository.java
    │   ├── ProductRepository.java
    │   └── WithdrawalNoticeRepository.java
    └── service/
        ├── WithdrawalService.java    Business logic & validation
        └── CsvExportService.java     CSV export utility
```

---

## Quick Start Summary

```bash
# 1. Clone and navigate
git clone https://github.com/Nomdumiso/enviro365-investments.git
cd enviro365-investments

# 2. Build the project
mvn clean install

# 3. Start backend (Terminal 1)
mvn spring-boot:run

# 4. Start frontend (Terminal 2)
python3 -m http.server 5500

# 5. Open in browser
# Frontend: http://localhost:5500/index.html
# API: http://localhost:8080/api
```

---

## IDE Setup

### IntelliJ IDEA

1. Open Project → Select `enviro365-investments` folder
2. Mark `src/main/java` as Sources Root
3. Run → Edit Configurations → Add new Maven Configuration
   - Name: `Spring Boot`
   - Command line: `spring-boot:run`
4. Run → Spring Boot

### VS Code

1. Install extensions:
   - Extension Pack for Java
   - Spring Boot Extension Pack
   - Live Server (for frontend)

2. Open folder: `enviro365-investments`

3. Terminal → New Terminal → Run:
   ```bash
   mvn spring-boot:run
   ```

---

## Environment Configuration

Edit `application.properties` to customize:

```properties
# Server port
server.port=8080

# H2 Database
spring.datasource.url=jdbc:h2:mem:enviro365db
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

# JPA/Hibernate
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=create-drop

# H2 Console
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
```

---

## Deployment (Optional)

### Build JAR File

```bash
mvn clean package
```

This creates `target/enviro365-withdrawal-system-1.0.0.jar`

### Run JAR

```bash
java -jar target/enviro365-withdrawal-system-1.0.0.jar
```

---

## Support & Questions

For issues or questions:
1. Check the README.md for overview
2. Review API_DOCUMENTATION.md for endpoints
3. Examine the test cases in WithdrawalServiceTest.java
4. Enable debug logging in application.properties:
   ```properties
   logging.level.root=DEBUG
   logging.level.com.enviro=DEBUG
   ```

