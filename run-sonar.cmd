@echo off
REM SonarCloud Analysis Script for Windows
REM This script runs SonarCloud analysis with the configured token
REM 
REM Usage:
REM   set SONAR_TOKEN=your-token
REM   run-sonar.cmd
REM
REM Or create a .env file (gitignored) - this script will auto-load it

REM Load .env file if it exists (automatically loads environment variables)
if exist .env (
    echo Loading environment variables from .env file...
    for /f "usebackq eol=# tokens=1,* delims==" %%a in (".env") do (
        if not "%%a"=="" (
            set "%%a=%%b"
        )
    )
)

REM Check if token is set, otherwise prompt or exit
if "%SONAR_TOKEN%"=="" (
    echo ERROR: SONAR_TOKEN environment variable is not set
    echo.
    echo Please set it with one of the following methods:
    echo   1. set SONAR_TOKEN=your-sonarcloud-token
    echo   2. Create a .env file with SONAR_TOKEN=your-token (this script will auto-load it)
    echo   3. Get your token from: https://sonarcloud.io/account/security
    exit /b 1
)

REM Set organization key (default: shrivasanjali)
if "%SONAR_ORGANIZATION%"=="" set SONAR_ORGANIZATION=shrivasanjali

echo Running SonarCloud analysis...
echo Token: %SONAR_TOKEN:~0,10%...
echo Organization: %SONAR_ORGANIZATION%
echo Host: https://sonarcloud.io

REM Run tests and SonarCloud analysis
REM Explicitly pass SonarCloud configuration to ensure it's used
call mvnw.cmd clean test sonar:sonar -Dsonar.host.url=https://sonarcloud.io -Dsonar.organization=%SONAR_ORGANIZATION% -Dsonar.login=%SONAR_TOKEN%

echo.
echo Analysis complete! Check results at: https://sonarcloud.io
