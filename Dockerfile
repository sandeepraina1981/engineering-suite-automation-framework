# Use a Microsoft Playwright base image with Java 17 and browsers pre-installed
FROM mcr.microsoft.com/playwright/java:v1.40.0-jammy

ARG SOURCE_PATH
ENV SOURCE_PATH=${SOURCE_PATH}

ARG HOST_NAME
ENV HOST_NAME=${HOST_NAME}

# Set the working directory inside the container
WORKDIR /app

RUN echo "Source folder: ${SOURCE_PATH}"

# Install Maven
RUN apt-get update && \
    apt-get install -y maven && \
    rm -rf /var/lib/apt/lists/*

RUN apt-get update && \
    apt-get install -y wget gnupg ca-certificates && \
    wget -q -O - https://dl.google.com/linux/linux_signing_key.pub | gpg --dearmor -o /usr/share/keyrings/google-chrome.gpg && \
    echo "deb [arch=amd64 signed-by=/usr/share/keyrings/google-chrome.gpg] http://dl.google.com/linux/chrome/deb/ stable main" > /etc/apt/sources.list.d/google-chrome.list && \
    apt-get update && \
    apt-get install -y google-chrome-stable && \
    rm -rf /var/lib/apt/lists/*

RUN google-chrome --headless --no-sandbox --disable-dev-shm-usage --version

# Copy the Maven project structure
COPY . .

# Pre-fetch dependencies to leverage Docker cache
RUN mvn dependency:go-offline -B

# Copy the source code
COPY src src

# Set environment variables (defaults)
ENV ENVIRONMENT=qa
ENV BROWSER=chrome
ENV XRAY_CLIENT_ID=""
ENV XRAY_CLIENT_SECRET=""

# Command to run tests and generate reports
# We use 'sh -c' to allow environment variable expansion in the command
CMD ["sh", "-c", "mvn clean verify && echo Host: ${HOST_NAME} && echo Source Path: ${HOST_NAME}/${SOURCE_PATH}/report/index.html"]