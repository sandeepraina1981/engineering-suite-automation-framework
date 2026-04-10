# Confluence Page 4: Execution & CI/CD

## 🐳 1. Docker-Native Execution
To ensure "Zero Drift" between local machines and the cloud, all tests are initiated through the **`Dockerfile`**.

### 1.1 Docker Environment Details
*   **Base Image**: `mcr.microsoft.com/playwright/java:v1.40.0-jammy`
*   **Pre-installed**: Maven 3.x, MSEdge, Google Chrome, and required Linux libraries.
*   **Auto-Execution**: The container CMD initiates both the test run and the subsequent PDF audit log generation.

## 🔄 2. CI/CD Lifecycle (GitHub Actions)
The pipeline is fully automated and triggered on-demand via **Workflow Dispatch**.

### 2.1 Workflow Steps (Vertical Flow)
```mermaid
sequenceDiagram
    autonumber
    participant GA as GitHub Action
    participant D as Docker (Ubuntu)
    participant J as Jira / Xray

    GA->>D: 1. Build engineering-suite-qa image
    D->>J: 2. Import Gherkin Features via API
    D->>D: 3. Run Maven Clean Verify (inside Container)
    D->>D: 4. Generate PDF Audit Log
    D->>GA: 5. Extract Artifacts (Reports/Logs)
    D->>J: 6. Sync Results to Xray Execution
    GA-->>GA: 7. Upload Artifacts to Build Run
```

## 🚀 3. How to Run
### 3.1 Local (Maven)
```bash
mvn clean verify -Denvironment=qa -Dplaywright.browser=chrome
```

### 3.2 Local (Docker)
```bash
docker build -t engineering-suite-qa .
docker run -e ENVIRONMENT=qa -e BROWSER=msedge engineering-suite-qa
```

---
*Next Page: [Reporting & Compliance](./05_Reporting_Compliance.md)*
