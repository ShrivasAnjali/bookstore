# Bookstore Application - Complete Command Reference

This document provides all the necessary commands to build, run, test, and access the Bookstore Spring Boot application.

## Prerequisites

- Java 17 or higher
- Maven (or use the Maven wrapper included: `.\mvnw.cmd` on Windows or `./mvnw` on macOS/Linux)
- Internet connection (for first-time dependency downloads)

---

## 🏗️ Building the Application

### Clean Build
**Windows:**
```cmd
.\mvnw.cmd clean install
```

**macOS/Linux:**
```bash
./mvnw clean install
```

Removes previous build artifacts and compiles the application.

### Compile Only (No Tests)
**Windows:**
```cmd
.\mvnw.cmd clean compile
```

**macOS/Linux:**
```bash
./mvnw clean compile
```

Compiles the source code without running tests.

### Package Application
**Windows:**
```cmd
.\mvnw.cmd clean package
```

**macOS/Linux:**
```bash
./mvnw clean package
```

Builds the application and creates a JAR file in the `target/` directory.

### Skip Tests During Build
**Windows:**
```cmd
.\mvnw.cmd clean package -DskipTests
```

**macOS/Linux:**
```bash
./mvnw clean package -DskipTests
```

Packages the application without running tests.

### Skip Tests and Test Compilation
**Windows:**
```cmd
.\mvnw.cmd clean package -Dmaven.test.skip=true
```

**macOS/Linux:**
```bash
./mvnw clean package -Dmaven.test.skip=true
```

Faster build that completely skips test compilation and execution.

---

## 🚀 Running the Application

### Run with Maven
**Windows:**
```cmd
.\mvnw.cmd spring-boot:run
```

**macOS/Linux:**
```bash
./mvnw spring-boot:run
```

Starts the application using Spring Boot Maven plugin. The application will be available at `http://localhost:8080`

### Run the JAR File
**Windows:**
```cmd
REM First, build the JAR
.\mvnw.cmd clean package

REM Then run it
java -jar target\bookstore-0.0.1-SNAPSHOT.jar
```

**macOS/Linux:**
```bash
# First, build the JAR
./mvnw clean package

# Then run it
java -jar target/bookstore-0.0.1-SNAPSHOT.jar
```

### Run with Custom Port
**Windows:**
```cmd
.\mvnw.cmd spring-boot:run -Dspring-boot.run.arguments=--server.port=9090
```

**macOS/Linux:**
```bash
./mvnw spring-boot:run -Dspring-boot.run.arguments=--server.port=9090
```

Starts the application on port 9090 instead of the default 8080.

### Run with Profile
**Windows:**
```cmd
.\mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=dev
```

**macOS/Linux:**
```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

Runs the application with a specific Spring profile.

### Run with Debug Mode
**Windows:**
```cmd
.\mvnw.cmd spring-boot:run -Dspring-boot.run.jvmArguments="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=5005"
```

**macOS/Linux:**
```bash
./mvnw spring-boot:run -Dspring-boot.run.jvmArguments="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=5005"
```

Starts the application in debug mode, listening on port 5005 for remote debugging.

---

## 🧪 Testing

### Run All Tests
**Windows:**
```cmd
.\mvnw.cmd test
```

**macOS/Linux:**
```bash
./mvnw test
```

Executes all unit and integration tests. This will also generate JaCoCo code coverage reports.

### Run Specific Test Class
**Windows:**
```cmd
.\mvnw.cmd test -Dtest=BookServiceTest
```

**macOS/Linux:**
```bash
./mvnw test -Dtest=BookServiceTest
```

Runs only the specified test class.

### Run Specific Test Method
**Windows:**
```cmd
.\mvnw.cmd test -Dtest=BookServiceTest#shouldCreateBookWhenIsbnDoesNotExist
```

**macOS/Linux:**
```bash
./mvnw test -Dtest=BookServiceTest#shouldCreateBookWhenIsbnDoesNotExist
```

Runs only a specific test method within a test class.

### Run Tests with Coverage Report
**Windows:**
```cmd
.\mvnw.cmd clean test jacoco:report
```

**macOS/Linux:**
```bash
./mvnw clean test jacoco:report
```

Runs tests and explicitly generates the JaCoCo coverage report.

### View Test Results
After running tests, detailed reports are available at:
- **Surefire Reports**: `target/surefire-reports/`
- **HTML Reports**: Open `target/site/surefire-report.html` in a browser

---

## 📊 Code Coverage (JaCoCo)

### Generate Coverage Report
**Windows:**
```cmd
.\mvnw.cmd clean test jacoco:report
```

**macOS/Linux:**
```bash
./mvnw clean test jacoco:report
```

Generates comprehensive code coverage reports.

### View HTML Coverage Report
**Windows:**
```cmd
start target\site\jacoco\index.html
```

**macOS/Linux:**
```bash
open target/site/jacoco/index.html
```

Or manually navigate to: `target/site/jacoco/index.html`

### Coverage Report Locations
- **HTML Report**: `target/site/jacoco/index.html`
- **XML Report**: `target/site/jacoco/jacoco.xml` (useful for CI/CD integration)
- **CSV Report**: `target/site/jacoco/jacoco.csv` (useful for data analysis)
- **Execution Data**: `target/jacoco.exec` (binary data file)

### Coverage Exclusions
The following packages are excluded from coverage (as configured in `pom.xml`):
- `**/dto/**` - DTO classes
- `**/entity/**` - Entity classes
- `**/config/**` - Configuration classes
- `**/BookstoreApplication.class` - Main application class

---

## 🔍 Static Code Analysis (SonarQube / SonarCloud)

### Prerequisites
- SonarQube Server running (for SonarQube) OR SonarCloud account (for SonarCloud)
- Authentication token configured (for SonarCloud) or credentials (for SonarQube Server)
- `sonar-project.properties` file configured at project root

### SonarCloud Token Configuration
**⚠️ Security Note:** Never commit tokens to version control. Use environment variables or GitHub Secrets.

**For Local Use:**
Set environment variable before running:
```bash
export SONAR_TOKEN=your-sonarcloud-token-here
export SONAR_ORGANIZATION=shrivasanjali
```

Get your token from: https://sonarcloud.io/account/security

**For GitHub Actions:**
Add as repository secrets (Settings → Secrets and variables → Actions):
- `SONAR_TOKEN`: Your SonarCloud token
- `SONAR_ORGANIZATION`: `shrivasanjali`

### Run SonarQube Analysis (Local SonarQube Server)
**Windows:**
```cmd
.\mvnw.cmd clean test sonar:sonar
```

**macOS/Linux:**
```bash
./mvnw clean test sonar:sonar
```

Runs tests, generates JaCoCo coverage reports, and performs SonarQube analysis. Requires SonarQube Server running at configured URL (default: `http://localhost:9000`).

### Run SonarQube Analysis with Custom Server URL
**Windows:**
```cmd
.\mvnw.cmd clean test sonar:sonar -Dsonar.host.url=http://your-sonarqube-server:9000
```

**macOS/Linux:**
```bash
./mvnw clean test sonar:sonar -Dsonar.host.url=http://your-sonarqube-server:9000
```

### Run SonarQube Analysis with Authentication
**Windows:**
```cmd
.\mvnw.cmd clean test sonar:sonar -Dsonar.login=your-token
```

**macOS/Linux:**
```bash
./mvnw clean test sonar:sonar -Dsonar.login=your-token
```

### Run SonarCloud Analysis
**Windows:**
```cmd
REM Set token as environment variable
set SONAR_TOKEN=your-sonarcloud-token-here
set SONAR_ORGANIZATION=shrivasanjali
.\mvnw.cmd clean test sonar:sonar
```

**macOS/Linux:**
```bash
# Set token as environment variable
export SONAR_TOKEN=your-sonarcloud-token-here
export SONAR_ORGANIZATION=shrivasanjali
./mvnw clean test sonar:sonar
```

**Alternative: Pass token directly**
**Windows:**
```cmd
.\mvnw.cmd clean test sonar:sonar -Dsonar.login=your-sonarcloud-token-here -Dsonar.organization=shrivasanjali
```

**macOS/Linux:**
```bash
./mvnw clean test sonar:sonar -Dsonar.login=your-sonarcloud-token-here -Dsonar.organization=shrivasanjali
```

**Note**: Replace `your-sonarcloud-token-here` with your actual token from https://sonarcloud.io/account/security

### Run SonarCloud Analysis with Environment Variables (Recommended)
**Windows:**
```cmd
set SONAR_TOKEN=your-sonarcloud-token-here
set SONAR_ORGANIZATION=shrivasanjali
.\mvnw.cmd clean test sonar:sonar
```

**macOS/Linux:**
```bash
export SONAR_TOKEN=your-sonarcloud-token-here
export SONAR_ORGANIZATION=shrivasanjali
./mvnw clean test sonar:sonar
```

**Tip**: Add these to your shell profile (`.bashrc`, `.zshrc`, etc.) to persist across sessions. Never commit tokens to version control!

### Quick Run Scripts (Easiest Method)
**Windows:**
```cmd
.\run-sonar.cmd
```

**macOS/Linux:**
```bash
./run-sonar.sh
```

These scripts require `SONAR_TOKEN` environment variable to be set. They will prompt you if it's missing.

### Complete Analysis Workflow (Tests + Coverage + SonarQube)
**Windows:**
```cmd
REM Step 1: Clean, compile, and run tests
.\mvnw.cmd clean test

REM Step 2: Generate coverage reports
.\mvnw.cmd jacoco:report

REM Step 3: Run SonarQube analysis
.\mvnw.cmd sonar:sonar -Dsonar.login=your-token
```

**macOS/Linux:**
```bash
# Step 1: Clean, compile, and run tests
./mvnw clean test

# Step 2: Generate coverage reports
./mvnw jacoco:report

# Step 3: Run SonarQube analysis
./mvnw sonar:sonar -Dsonar.login=your-token
```

### Verify SonarQube Configuration
**Windows:**
```cmd
.\mvnw.cmd sonar:sonar -Dsonar.scanner.dumpToFile=sonar-config.txt
```

**macOS/Linux:**
```bash
./mvnw sonar:sonar -Dsonar.scanner.dumpToFile=sonar-config.txt
```

Dumps SonarQube configuration to `sonar-config.txt` for verification without running analysis.

### SonarQube Configuration File
The project includes `sonar-project.properties` at the root with:
- Project key: `com.example:bookstore`
- Source directories: `src/main/java`
- Test directories: `src/test/java`
- JaCoCo XML report path: `target/site/jacoco/jacoco.xml`
- Exclusions matching JaCoCo configuration

### CI/CD Integration Examples

#### GitHub Actions (SonarCloud)
The workflow file `.github/workflows/sonarcloud.yml` is already configured.

**Setup Steps:**
1. Go to GitHub repository → **Settings** → **Secrets and variables** → **Actions**
2. Add secrets:
   - `SONAR_TOKEN`: Your SonarCloud token (get from https://sonarcloud.io/account/security)
   - `SONAR_ORGANIZATION`: `shrivasanjali`
3. Push code - analysis runs automatically on push/PR

**Manual Workflow Trigger:**
The workflow can also be triggered manually from GitHub Actions tab.

#### GitLab CI (SonarQube)
```yaml
sonarqube:
  script:
    - ./mvnw clean test sonar:sonar
      -Dsonar.host.url=$SONAR_HOST_URL
      -Dsonar.login=$SONAR_TOKEN
```

#### Jenkins Pipeline (SonarQube)
```groovy
stage('SonarQube Analysis') {
    steps {
        sh './mvnw clean test sonar:sonar'
    }
}
```

### View Analysis Results
After running analysis:
- **SonarCloud**: Results available at `https://sonarcloud.io/dashboard?id=com.example:bookstore`
- **SonarQube Server**: Results available at configured server URL (default: `http://localhost:9000`)

### SonarQube Quality Gates
The analysis will check against configured quality gates for:
- Code coverage thresholds
- Code smells
- Security vulnerabilities
- Bugs
- Maintainability ratings

### Troubleshooting SonarQube

#### Authentication Issues
**Windows:**
```cmd
REM Verify token is set correctly
echo %SONAR_TOKEN%

REM Or pass token directly
.\mvnw.cmd sonar:sonar -Dsonar.login=your-token
```

**macOS/Linux:**
```bash
# Verify token is set correctly
echo $SONAR_TOKEN

# Or pass token directly
./mvnw sonar:sonar -Dsonar.login=your-token
```

#### Coverage Report Not Found
Ensure JaCoCo XML report exists:
**Windows:**
```cmd
dir target\site\jacoco\jacoco.xml
```

**macOS/Linux:**
```bash
ls -lh target/site/jacoco/jacoco.xml
```

If missing, regenerate:
**Windows:**
```cmd
.\mvnw.cmd clean test jacoco:report
```

**macOS/Linux:**
```bash
./mvnw clean test jacoco:report
```

#### Connection Issues
Verify SonarQube server is accessible:
**Windows:**
```cmd
curl http://localhost:9000/api/system/status
```

**macOS/Linux:**
```bash
curl http://localhost:9000/api/system/status
```

---

## 📚 API Documentation (Swagger/OpenAPI)

### Access Swagger UI
Once the application is running, access the interactive Swagger UI at:
```
http://localhost:8080/swagger-ui.html
```
or
```
http://localhost:8080/swagger-ui/index.html
```

### Access OpenAPI JSON Specification
```
http://localhost:8080/v3/api-docs
```
Returns the raw OpenAPI 3.0 specification in JSON format.

### Access OpenAPI YAML Specification
```
http://localhost:8080/v3/api-docs.yaml
```
Returns the OpenAPI 3.0 specification in YAML format.

### Download API Specification
**Windows (PowerShell):**
```powershell
# Download OpenAPI JSON
curl http://localhost:8080/v3/api-docs -OutFile bookstore-api.json

# Download OpenAPI YAML
curl http://localhost:8080/v3/api-docs.yaml -OutFile bookstore-api.yaml
```

**macOS/Linux:**
```bash
# Download OpenAPI JSON
curl http://localhost:8080/v3/api-docs -o bookstore-api.json

# Download OpenAPI YAML
curl http://localhost:8080/v3/api-docs.yaml -o bookstore-api.yaml
```

---

## 🌐 API Endpoints

### Base URL
```
http://localhost:8080
```

### Root Endpoints

#### Home/Info
**Windows (PowerShell):**
```powershell
curl http://localhost:8080/
```

**macOS/Linux:**
```bash
curl http://localhost:8080/
```

Returns welcome message and application status.

#### Health Check
**Windows (PowerShell):**
```powershell
curl http://localhost:8080/health
```

**macOS/Linux:**
```bash
curl http://localhost:8080/health
```

Returns application health status.

### Book API Endpoints

#### Get All Books
**Windows (PowerShell):**
```powershell
curl http://localhost:8080/api/books
```

**macOS/Linux:**
```bash
curl http://localhost:8080/api/books
```

#### Get Book by ID
**Windows (PowerShell):**
```powershell
curl http://localhost:8080/api/books/1
```

**macOS/Linux:**
```bash
curl http://localhost:8080/api/books/1
```

#### Get Book by ISBN
**Windows (PowerShell):**
```powershell
curl http://localhost:8080/api/books/isbn/123456
```

**macOS/Linux:**
```bash
curl http://localhost:8080/api/books/isbn/123456
```

#### Get Books by Author
**Windows (PowerShell):**
```powershell
curl http://localhost:8080/api/books/author/John%20Doe
```

**macOS/Linux:**
```bash
curl http://localhost:8080/api/books/author/John%20Doe
```

#### Search Books by Title
**Windows (PowerShell):**
```powershell
curl "http://localhost:8080/api/books/search?title=Java"
```

**macOS/Linux:**
```bash
curl "http://localhost:8080/api/books/search?title=Java"
```

#### Create a Book
**Windows (PowerShell):**
```powershell
curl -X POST http://localhost:8080/api/books `
  -H "Content-Type: application/json" `
  -d '{\"title\": \"Effective Java\", \"author\": \"Joshua Bloch\", \"isbn\": \"978-0134685991\", \"price\": 54.99, \"quantity\": 100}'
```

**macOS/Linux:**
```bash
curl -X POST http://localhost:8080/api/books \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Effective Java",
    "author": "Joshua Bloch",
    "isbn": "978-0134685991",
    "price": 54.99,
    "quantity": 100
  }'
```

#### Update a Book (Full Update)
**Windows (PowerShell):**
```powershell
curl -X PUT http://localhost:8080/api/books/1 `
  -H "Content-Type: application/json" `
  -d '{\"title\": \"Effective Java 3rd Edition\", \"author\": \"Joshua Bloch\", \"isbn\": \"978-0134685991\", \"price\": 59.99, \"quantity\": 150}'
```

**macOS/Linux:**
```bash
curl -X PUT http://localhost:8080/api/books/1 \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Effective Java 3rd Edition",
    "author": "Joshua Bloch",
    "isbn": "978-0134685991",
    "price": 59.99,
    "quantity": 150
  }'
```

#### Partially Update a Book (PATCH)
**Windows (PowerShell):**
```powershell
curl -X PATCH http://localhost:8080/api/books/1 `
  -H "Content-Type: application/json" `
  -d '{\"price\": 49.99, \"quantity\": 200}'
```

**macOS/Linux:**
```bash
curl -X PATCH http://localhost:8080/api/books/1 \
  -H "Content-Type: application/json" \
  -d '{
    "price": 49.99,
    "quantity": 200
  }'
```

#### Delete a Book
**Windows (PowerShell):**
```powershell
curl -X DELETE http://localhost:8080/api/books/1
```

**macOS/Linux:**
```bash
curl -X DELETE http://localhost:8080/api/books/1
```

---

## 🗄️ Database Commands

### View Database File
The application uses SQLite database stored in:
**Windows:**
```cmd
REM View database file location
dir bookstore.db

REM Access database using SQLite CLI (if installed)
sqlite3 bookstore.db
```

**macOS/Linux:**
```bash
# View database file location
ls -lh bookstore.db

# Access database using SQLite CLI (if installed)
sqlite3 bookstore.db
```

### Common SQLite Commands
```sql
-- View all books
SELECT * FROM books;

-- View table schema
.schema books

-- Exit SQLite
.quit
```

### Reset Database
**Windows:**
```cmd
REM Stop the application, then:
del bookstore.db

REM Restart the application - it will recreate the database from schema.sql
.\mvnw.cmd spring-boot:run
```

**macOS/Linux:**
```bash
# Stop the application, then:
rm bookstore.db

# Restart the application - it will recreate the database from schema.sql
./mvnw spring-boot:run
```

---

## 🛠️ Development Tools

### Dependency Tree
**Windows:**
```cmd
.\mvnw.cmd dependency:tree
```

**macOS/Linux:**
```bash
./mvnw dependency:tree
```

Shows all Maven dependencies and their versions.

### Check for Dependency Updates
**Windows:**
```cmd
.\mvnw.cmd versions:display-dependency-updates
```

**macOS/Linux:**
```bash
./mvnw versions:display-dependency-updates
```

Displays which dependencies have newer versions available.

### Clean Project
**Windows:**
```cmd
.\mvnw.cmd clean
```

**macOS/Linux:**
```bash
./mvnw clean
```

Removes all generated files from the `target/` directory.

### Validate POM
**Windows:**
```cmd
.\mvnw.cmd validate
```

**macOS/Linux:**
```bash
./mvnw validate
```

Validates the project's POM file structure.

### Compile Test Sources
**Windows:**
```cmd
.\mvnw.cmd test-compile
```

**macOS/Linux:**
```bash
./mvnw test-compile
```

Compiles test sources without running tests.

---

## 🔍 Debugging

### Run Application in Debug Mode
**Windows:**
```cmd
.\mvnw.cmd spring-boot:run -Dspring-boot.run.jvmArguments="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=5005"
```

**macOS/Linux:**
```bash
./mvnw spring-boot:run -Dspring-boot.run.jvmArguments="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=5005"
```

Then connect your IDE debugger to `localhost:5005`.

### Enable Debug Logging
Add to `application.properties`:
```properties
logging.level.root=DEBUG
logging.level.com.example.bookstore=DEBUG
```

### View Application Logs
Logs are printed to console. For production, you can redirect:
**Windows:**
```cmd
.\mvnw.cmd spring-boot:run > app.log 2>&1
```

**macOS/Linux:**
```bash
./mvnw spring-boot:run > app.log 2>&1
```

---

## 📦 Additional Maven Goals

### Generate Site Documentation
**Windows:**
```cmd
.\mvnw.cmd site
```

**macOS/Linux:**
```bash
./mvnw site
```

Generates project documentation site.

### Check Code Style
**Windows:**
```cmd
REM If Checkstyle is configured
.\mvnw.cmd checkstyle:check
```

**macOS/Linux:**
```bash
# If Checkstyle is configured
./mvnw checkstyle:check
```

### Run with Spring Boot DevTools (Hot Reload)
If Spring Boot DevTools were added:
**Windows:**
```cmd
.\mvnw.cmd spring-boot:run
REM Then modify code - it will auto-reload
```

**macOS/Linux:**
```bash
./mvnw spring-boot:run
# Then modify code - it will auto-reload
```

---

## 🚀 Quick Start Workflow

### Complete Development Cycle
**Windows:**
```cmd
REM 1. Clean and build
.\mvnw.cmd clean compile

REM 2. Run tests with coverage
.\mvnw.cmd clean test jacoco:report

REM 3. View coverage report
start target\site\jacoco\index.html

REM 4. Run SonarQube analysis (optional)
.\mvnw.cmd sonar:sonar -Dsonar.login=your-token

REM 5. Start the application
.\mvnw.cmd spring-boot:run

REM 6. In another terminal, test the API
curl http://localhost:8080/health

REM 7. Access Swagger UI
start http://localhost:8080/swagger-ui.html
```

**macOS/Linux:**
```bash
# 1. Clean and build
./mvnw clean compile

# 2. Run tests with coverage
./mvnw clean test jacoco:report

# 3. View coverage report
open target/site/jacoco/index.html

# 4. Run SonarQube analysis (optional)
./mvnw sonar:sonar -Dsonar.login=your-token

# 5. Start the application
./mvnw spring-boot:run

# 6. In another terminal, test the API
curl http://localhost:8080/health

# 7. Access Swagger UI
open http://localhost:8080/swagger-ui.html
```

---

## 📝 Useful URLs (When Application is Running)

| Purpose | URL |
|---------|-----|
| Home/Info | http://localhost:8080/ |
| Health Check | http://localhost:8080/health |
| Swagger UI | http://localhost:8080/swagger-ui.html |
| OpenAPI JSON | http://localhost:8080/v3/api-docs |
| OpenAPI YAML | http://localhost:8080/v3/api-docs.yaml |
| Books API Base | http://localhost:8080/api/books |

---

## 🔧 Troubleshooting

### Port Already in Use
If port 8080 is already in use:
**Windows:**
```cmd
REM Find and kill the process using port 8080
netstat -ano | findstr :8080
REM Note the PID, then:
taskkill /PID <PID> /F

REM Or use a different port:
.\mvnw.cmd spring-boot:run -Dspring-boot.run.arguments=--server.port=9090
```

**macOS/Linux:**
```bash
# Find and kill the process using port 8080
lsof -ti:8080 | xargs kill -9

# Or use a different port:
./mvnw spring-boot:run -Dspring-boot.run.arguments=--server.port=9090
```

### Maven Wrapper Permission Denied
**macOS/Linux:**
```bash
chmod +x mvnw
```

**Windows:** No action needed - `.\mvnw.cmd` should work without permission changes.

### Database Locked
If you see database locked errors:
**Windows:**
```cmd
REM Stop the application, then:
del bookstore.db
REM Restart - database will be recreated
```

**macOS/Linux:**
```bash
# Stop the application, then:
rm bookstore.db
# Restart - database will be recreated
```

### Clear Maven Cache
**Windows:**
```cmd
rmdir /s /q %USERPROFILE%\.m2\repository
.\mvnw.cmd clean install
```

**macOS/Linux:**
```bash
rm -rf ~/.m2/repository
./mvnw clean install
```

---

## 📚 Additional Resources

- **Spring Boot Documentation**: https://spring.io/projects/spring-boot
- **SpringDoc OpenAPI**: https://springdoc.org/
- **JaCoCo Documentation**: https://www.jacoco.org/jacoco/trunk/doc/
- **Maven Documentation**: https://maven.apache.org/guides/
- **SonarQube Documentation**: https://docs.sonarqube.org/
- **SonarCloud Documentation**: https://docs.sonarcloud.io/
- **SonarQube Maven Plugin**: https://docs.sonarqube.org/latest/analysis/scan/sonarscanner-for-maven/

---

## ✅ Verification Checklist

After setting up, verify everything works:

- [ ] Application starts without errors: `.\mvnw.cmd spring-boot:run` (Windows) or `./mvnw spring-boot:run` (macOS/Linux)
- [ ] Health check returns 200: `curl http://localhost:8080/health`
- [ ] Swagger UI is accessible: http://localhost:8080/swagger-ui.html
- [ ] All tests pass: `.\mvnw.cmd test` (Windows) or `./mvnw test` (macOS/Linux)
- [ ] Code coverage report is generated: `start target\site\jacoco\index.html` (Windows) or `open target/site/jacoco/index.html` (macOS/Linux)
- [ ] SonarQube analysis runs successfully: `.\mvnw.cmd sonar:sonar` (Windows) or `./mvnw sonar:sonar` (macOS/Linux)
- [ ] Can create a book via API
- [ ] Can retrieve books via API

---

**Last Updated**: January 2026  
**Spring Boot Version**: 4.0.0  
**Java Version**: 17
