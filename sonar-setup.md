# SonarCloud Setup Guide

This guide explains how to configure SonarCloud for both local development and GitHub Actions CI/CD.

## Prerequisites

1. SonarCloud account: Sign up at https://sonarcloud.io
2. SonarCloud token: Get from https://sonarcloud.io/account/security
3. Organization key: Found in your SonarCloud organization settings

## Token Configuration

**⚠️ Security Warning:** Never commit tokens to version control!

Get your SonarCloud token from: https://sonarcloud.io/account/security

**Your SonarCloud Token:** `your-sonarcloud-token-here` (replace with your actual token)

## Local Setup

### Option 1: Environment Variable (Recommended)

**macOS/Linux:**
```bash
export SONAR_TOKEN=your-sonarcloud-token-here
export SONAR_ORGANIZATION=shrivasanjali
./mvnw clean test sonar:sonar
```

**Windows (PowerShell):**
```powershell
$env:SONAR_TOKEN="your-sonarcloud-token-here"
$env:SONAR_ORGANIZATION="shrivasanjali"
.\mvnw.cmd clean test sonar:sonar
```

**Windows (CMD):**
```cmd
set SONAR_TOKEN=your-sonarcloud-token-here
set SONAR_ORGANIZATION=shrivasanjali
.\mvnw.cmd clean test sonar:sonar
```

### Option 2: Command Line Parameter

```bash
./mvnw clean test sonar:sonar \
  -Dsonar.login=your-sonarcloud-token-here \
  -Dsonar.organization=shrivasanjali
```

### Option 3: Create .env file (for convenience)

Create a `.env` file in the project root (this file is gitignored):

```bash
SONAR_TOKEN=your-sonarcloud-token-here
SONAR_ORGANIZATION=shrivasanjali
```

Then source it before running:
```bash
source .env  # macOS/Linux
# or
set -a; source .env; set +a  # Alternative
```

## GitHub Actions Setup

1. Go to your GitHub repository
2. Navigate to **Settings** → **Secrets and variables** → **Actions**
3. Add the following secrets:
   - `SONAR_TOKEN`: Your SonarCloud token (get from https://sonarcloud.io/account/security)
   - `SONAR_ORGANIZATION`: `shrivasanjali`
   - `GITHUB_TOKEN`: Automatically provided by GitHub Actions

The workflow file (`.github/workflows/sonarcloud.yml`) is already configured to use these secrets.

## Verify Setup

Run locally to test:
```bash
./mvnw clean test sonar:sonar
```

Check SonarCloud dashboard: https://sonarcloud.io

## Troubleshooting

- **Token not found**: Ensure environment variable is set or token is passed via `-Dsonar.login`
- **Organization not found**: Set `SONAR_ORGANIZATION` environment variable or use `-Dsonar.organization`
- **Connection issues**: Verify `sonar.host.url=https://sonarcloud.io` in `sonar-project.properties`
