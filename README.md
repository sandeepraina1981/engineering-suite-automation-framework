# Engineering Suite Automation Framework

## 🚀 Overview
An elite enterprise-grade, BDD-driven test automation framework for **Lenze Engineering Suites**. This solution leverages a **Playwright-First** philosophy and the **Screenplay Pattern** via **Serenity BDD** to provide a unified, high-performance execution engine for both UI and REST API testing.

## ✨ Core Pillars
- **Unified Playwright Engine**: High-speed, stable execution for UI (MSEdge/Chrome) and API testing with shared session context.
- **Screenplay Pattern**: SOLID-based design that ensures modular, reusable, and readable test logic.
- **Ability-Driven Casting**: Intelligent staging via `EngineeringSuiteCast` that dynamically provisions environments and roles based on Personas.
- **Persona-Centric Testing**: Human-centric Gherkin steps using real-world personas (Alex, Chris, Charlie, Casey, etc.).
- **Docker-Native Execution**: Absolute environment parity between local and CI/CD via a containerized execution model.
- **Secure Credential Management**: Role-based authentication via `CredentialsProvider`, pulling from secure environment variables (no hardcoding).

## 🏗️ Project Structure
```text
engineering-suite-automation-framework/
├── automation-framework/       # Core logic: Cast, Personas, OAuth2/PKCE, PDF Generator, Xray Client
├── automation-tests/           # Test scripts: Gherkin Features, Tasks, Interactions, Questions
├── confluence-pages/           # Page-by-page detailed documentation for Confluence
└── Dockerfile                  # Container definition for consistent execution
```

## 🛠️ Getting Started
### Prerequisites
- **Java 17**
- **Maven 3.8+**
- **Docker** (Optional but Recommended)

### Quick Run (Local Maven)
```bash
# Default: QA environment, MSEdge browser
mvn clean verify

# Custom Environment & Browser
mvn clean verify -Denvironment=dev -Dplaywright.browser=chrome
```

### Quick Run (Docker)
```bash
docker build -t engineering-suite-qa .
docker run -e ENVIRONMENT=qa -e BROWSER=msedge engineering-suite-qa
```

## 🔐 Security & Auth
The framework handles **OAuth2 with PKCE** for Azure B2C login screens. Credentials are provided via environment variables:
- `ADMIN_USER` / `ADMIN_PASS`
- `NORMAL_USER` / `NORMAL_PASS`
- `XRAY_CLIENT_ID` / `XRAY_CLIENT_SECRET`

## 📊 Reporting & Compliance
- **Serenity BDD HTML**: Interactive technical dashboard for engineers.
- **Professional PDF Audit Log**: Step-by-step compliance document with embedded screenshots.
- **Xray Cloud Sync**: Automatic synchronization of automated execution results to Jira.

---
For deep-dive technical details and professional diagrams, see [ARCHITECTURE.md](./ARCHITECTURE.md) and the [Confluence Pages](./confluence-pages/01_Space_Home.md).
