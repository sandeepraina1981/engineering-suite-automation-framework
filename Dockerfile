# Use a Microsoft Playwright base image with Java 17 and browsers pre-installed
FROM mcr.microsoft.com/playwright/java:v1.40.0-jammy

# Set the working directory inside the container
WORKDIR /app

# Install Maven
RUN apt-get update && \
    apt-get install -y maven && \
    rm -rf /var/lib/apt/lists/*

# Copy the Maven project structure
COPY pom.xml .
COPY automation-framework/pom.xml automation-framework/
COPY automation-tests/pom.xml automation-tests/

# Pre-fetch dependencies to leverage Docker cache
RUN mvn dependency:go-offline -B

# Copy the source code
COPY automation-framework/src automation-framework/src
COPY automation-tests/src automation-tests/src

# Set environment variables (defaults)
ENV ENVIRONMENT=qa
ENV BROWSER=msedge
ENV XRAY_CLIENT_ID=""
ENV XRAY_CLIENT_SECRET=""

# Command to run tests and generate reports
# We use 'sh -c' to allow environment variable expansion in the command
CMD ["sh", "-c", "mvn clean verify -Denvironment=${ENVIRONMENT} -Dplaywright.browser=${BROWSER} && \
     mvn exec:java -pl automation-framework -Dexec.mainClass='com.lenze.engineeringsuites.qa.framework.reporting.ReportGeneratorMain' -Dexec.args='automation-tests/target/cucumber-reports/cucumber.json automation-tests/target/engineering-suite-audit-log.pdf'"]
