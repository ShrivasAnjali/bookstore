# Bookstore Application - Windows Commands

Quick reference for all Windows commands to build, run, test, and access the Bookstore Spring Boot application.

## Prerequisites
- Java 17 or higher
- Maven wrapper included: `.\mvnw.cmd`

---

## 🏗️ Building

**Clean Build**
```cmd
.\mvnw.cmd clean install
```

**Compile Only (No Tests)**
```cmd
.\mvnw.cmd clean compile
```

**Package Application**
```cmd
.\mvnw.cmd clean package
```

**Skip Tests During Build**
```cmd
.\mvnw.cmd clean package -DskipTests
```

**Skip Tests and Test Compilation**
```cmd
.\mvnw.cmd clean package -Dmaven.test.skip=true
```

---

## 🚀 Running

**Run Application**
```cmd
.\mvnw.cmd spring-boot:run
```

**Run JAR File**
```cmd
.\mvnw.cmd clean package
java -jar target\bookstore-0.0.1-SNAPSHOT.jar
```

**Run with Custom Port**
```cmd
.\mvnw.cmd spring-boot:run -Dspring-boot.run.arguments=--server.port=9090
```

**Run with Profile**
```cmd
.\mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=dev
```

**Run in Debug Mode**
```cmd
.\mvnw.cmd spring-boot:run -Dspring-boot.run.jvmArguments="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=5005"
```

---

## 🧪 Testing

**Run All Tests**
```cmd
.\mvnw.cmd test
```

**Run Specific Test Class**
```cmd
.\mvnw.cmd test -Dtest=BookServiceTest
```

**Run Specific Test Method**
```cmd
.\mvnw.cmd test -Dtest=BookServiceTest#shouldCreateBookWhenIsbnDoesNotExist
```

**Run Tests with Coverage Report**
```cmd
.\mvnw.cmd clean test jacoco:report
```

**View Coverage Report**
```cmd
start target\site\jacoco\index.html
```

---

## 📊 Code Coverage

**Generate Coverage Report**
```cmd
.\mvnw.cmd clean test jacoco:report
```

**View HTML Coverage Report**
```cmd
start target\site\jacoco\index.html
```

---

## 🔍 SonarCloud Analysis

**Set Environment Variables**
```cmd
set SONAR_TOKEN=your-sonarcloud-token-here
set SONAR_ORGANIZATION=shrivasanjali
```

**Run SonarCloud Analysis**
```cmd
.\mvnw.cmd clean test sonar:sonar
```

**Run with Token Directly**
```cmd
.\mvnw.cmd clean test sonar:sonar -Dsonar.login=your-token -Dsonar.organization=shrivasanjali
```

**Verify Token**
```cmd
echo %SONAR_TOKEN%
```

---

## 🌐 API Endpoints

**Base URL:** `http://localhost:8080`

**Home/Info**
```powershell
curl http://localhost:8080/
```

**Health Check**
```powershell
curl http://localhost:8080/health
```

**Get All Books**
```powershell
curl http://localhost:8080/api/books
```

**Get Book by ID**
```powershell
curl http://localhost:8080/api/books/1
```

**Get Book by ISBN**
```powershell
curl http://localhost:8080/api/books/isbn/123456
```

**Get Books by Author**
```powershell
curl http://localhost:8080/api/books/author/John%20Doe
```

**Search Books by Title**
```powershell
curl "http://localhost:8080/api/books/search?title=Java"
```

**Create a Book**
```powershell
curl -X POST http://localhost:8080/api/books -H "Content-Type: application/json" -d '{\"title\": \"Effective Java\", \"author\": \"Joshua Bloch\", \"isbn\": \"978-0134685991\", \"price\": 54.99, \"quantity\": 100}'
```

**Update a Book (PUT)**
```powershell
curl -X PUT http://localhost:8080/api/books/1 -H "Content-Type: application/json" -d '{\"title\": \"Effective Java 3rd Edition\", \"author\": \"Joshua Bloch\", \"isbn\": \"978-0134685991\", \"price\": 59.99, \"quantity\": 150}'
```

**Partially Update a Book (PATCH)**
```powershell
curl -X PATCH http://localhost:8080/api/books/1 -H "Content-Type: application/json" -d '{\"price\": 49.99, \"quantity\": 200}'
```

**Delete a Book**
```powershell
curl -X DELETE http://localhost:8080/api/books/1
```

---

## 📚 API Documentation

**Swagger UI**
```
http://localhost:8080/swagger-ui.html
```

**OpenAPI JSON**
```
http://localhost:8080/v3/api-docs
```

**OpenAPI YAML**
```
http://localhost:8080/v3/api-docs.yaml
```

**Download OpenAPI JSON**
```powershell
curl http://localhost:8080/v3/api-docs -OutFile bookstore-api.json
```

**Download OpenAPI YAML**
```powershell
curl http://localhost:8080/v3/api-docs.yaml -OutFile bookstore-api.yaml
```

---

## 🗄️ Database

**View Database File**
```cmd
dir bookstore.db
```

**Access Database**
```cmd
sqlite3 bookstore.db
```

**Reset Database**
```cmd
del bookstore.db
.\mvnw.cmd spring-boot:run
```

---

## 🛠️ Development Tools

**Dependency Tree**
```cmd
.\mvnw.cmd dependency:tree
```

**Check for Dependency Updates**
```cmd
.\mvnw.cmd versions:display-dependency-updates
```

**Clean Project**
```cmd
.\mvnw.cmd clean
```

**Validate POM**
```cmd
.\mvnw.cmd validate
```

**Compile Test Sources**
```cmd
.\mvnw.cmd test-compile
```

---

## 🔍 Debugging

**Run in Debug Mode**
```cmd
.\mvnw.cmd spring-boot:run -Dspring-boot.run.jvmArguments="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=5005"
```

**Save Logs to File**
```cmd
.\mvnw.cmd spring-boot:run > app.log 2>&1
```

---

## 🔧 Troubleshooting

**Kill Process on Port 8080**
```cmd
netstat -ano | findstr :8080
taskkill /PID <PID> /F
```

**Use Different Port**
```cmd
.\mvnw.cmd spring-boot:run -Dspring-boot.run.arguments=--server.port=9090
```

**Clear Maven Cache**
```cmd
rmdir /s /q %USERPROFILE%\.m2\repository
.\mvnw.cmd clean install
```

---

## 📝 Useful URLs

| Purpose | URL |
|---------|-----|
| Home/Info | http://localhost:8080/ |
| Health Check | http://localhost:8080/health |
| Swagger UI | http://localhost:8080/swagger-ui.html |
| OpenAPI JSON | http://localhost:8080/v3/api-docs |
| OpenAPI YAML | http://localhost:8080/v3/api-docs.yaml |
| Books API Base | http://localhost:8080/api/books |

---

**Last Updated**: January 2026  
**Spring Boot Version**: 4.0.0  
**Java Version**: 17
