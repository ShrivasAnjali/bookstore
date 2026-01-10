#!/bin/bash
# SonarCloud Analysis Script
# This script runs SonarCloud analysis with the configured token
# 
# Usage:
#   export SONAR_TOKEN=your-token
#   ./run-sonar.sh
#
# Or create a .env file (gitignored) with:
#   SONAR_TOKEN=your-token
#   SONAR_ORGANIZATION=shrivasanjali

# Load .env file if it exists (automatically loads environment variables)
if [ -f .env ]; then
    echo "Loading environment variables from .env file..."
    set -a  # Automatically export all variables
    source .env
    set +a  # Stop automatically exporting
fi

# Check if token is set, otherwise prompt or exit
if [ -z "$SONAR_TOKEN" ]; then
    echo "ERROR: SONAR_TOKEN environment variable is not set"
    echo ""
    echo "Please set it with one of the following methods:"
    echo "  1. export SONAR_TOKEN=your-sonarcloud-token"
    echo "  2. Create a .env file with SONAR_TOKEN=your-token (this script will auto-load it)"
    echo "  3. Get your token from: https://sonarcloud.io/account/security"
    exit 1
fi

# Set organization key (default: shrivasanjali)
export SONAR_ORGANIZATION=${SONAR_ORGANIZATION:-"shrivasanjali"}

echo "Running SonarCloud analysis..."
echo "Token: ${SONAR_TOKEN:0:10}..."
echo "Organization: $SONAR_ORGANIZATION"
echo "Host: https://sonarcloud.io"

# Run tests and SonarCloud analysis
# Explicitly pass SonarCloud configuration to ensure it's used
./mvnw clean test sonar:sonar \
  -Dsonar.host.url=https://sonarcloud.io \
  -Dsonar.organization=$SONAR_ORGANIZATION \
  -Dsonar.login=$SONAR_TOKEN

echo ""
echo "Analysis complete! Check results at: https://sonarcloud.io"
