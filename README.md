***

# 🚀 Engineering Suite Automation Framework

### *Unified Web + API + BDD Automation Platform with Full Jira/Xray Traceability*

***

## ✅ 1. Overview

The **Engineering Suite Automation Framework** is an enterprise-ready automation ecosystem built using **Java 21+**, designed to standardize and streamline test automation across Web UI, REST API, and BDD workflows.

It integrates seamlessly with:

*   **Playwright (Java)** for modern Web UI automation
*   **REST Assured / Playwright API** for API automation
*   **Cucumber JVM** for Gherkin-based BDD
*   **Xray + Jira** for requirement-level traceability
*   **Cluecumber** for modern, detailed HTML reporting

The framework ensures aligned, traceable, and reproducible results across engineering teams.

***

## ✅ 2. Core Features

### 🌐 Web UI Automation (Playwright Java)

*   Cross-browser automation: Chromium, Firefox, WebKit
*   Auto-waiting, stable actions, DOM-ready checks
*   Screenshots, logs, videos, and trace capture
*   Extensible Page Object & Screenplay compatibility

### 🔗 REST API Automation

*   REST Assured / Playwright API client
*   JSON, XML, GraphQL validations
*   Contract, integration, and end‑to‑end API tests

### 🧩 BDD with Cucumber JVM

*   Business-readable `.feature` files
*   Step definitions in Java
*   Hooks, tag filtering, environment support

### 📘 Gherkin Tests Managed in Xray

*   Xray is the **single source of truth**
*   All scenarios are authored, versioned, and maintained in Jira/Xray
*   CI pulls FeatureBundle during execution

### 🔄 Jira + Xray Integration

*   Fetch `.feature` files dynamically
*   Upload Cucumber JSON results
*   Attach evidence (screenshots, logs)
*   Auto-update Test Execution status

### 📊 Cluecumber HTML Reporting

*   Clean, modern HTML dashboards
*   Step, scenario, and tag breakdown
*   Screenshots embedded at step level
*   Works fully offline

### ⚙️ CI/CD Ready

*   GitHub Actions
*   Jenkins
*   GitLab
*   Azure DevOps
*   Parallel execution support

***

<img width="1320" height="881" alt="image" src="https://github.com/user-attachments/assets/45872625-4e82-4255-aa47-e0be5f4b2516" />

***

## ✅ 6. Project Structure

    engineering-suite/
     ├── automation-framework/
     │    ├── src/test/java/
     │    │    ├── runners/
     │    │    ├── steps/
     │    │    ├── pages/
     │    │    ├── api/
     │    │    ├── hooks/
     │    │    └── utils/
     │    ├── src/test/resources/
     │    │    └── features/       # filled dynamically from Xray
     │    └── pom.xml

     ├── xray-integration-library/
     │    ├── src/main/java/
     │    │    ├── client/
     │    │    ├── dto/
     │    │    └── service/
     │    └── pom.xml

     ├── test-suites/
     │    ├── login/
     │    ├── checkout/
     │    ├── api/
     │    └── pom.xml

     ├── docs/
     │    └── diagrams/
     │         ├── architecture.png
     │         └── architecture.svg

     └── README.md

***

## ✅ 7. Sample Gherkin Scenario (maintained in Xray)

```gherkin
@JIRA-123 @Smoke @Web
Feature: Login validation

  Scenario: Successful login
    Given user navigates to login page
    When user logs in with valid credentials
    Then user should be redirected to the dashboard
```

***

## ✅ 8. Sample Step Definitions (Java + Playwright)

```java
@Given("user navigates to login page")
public void navigateToLogin() {
    page.navigate("https://example.com/login");
}

@When("user logs in with valid credentials")
public void login() {
    page.fill("#username", "validuser");
    page.fill("#password", "secret");
    page.click("#submit");
}

@Then("user should be redirected to the dashboard")
public void verifyDashboard() {
    page.waitForSelector("#dashboard");
}
```

***

## ✅ 9. Running Tests & Generating Reports

### Run automated tests:

```bash
mvn clean verify
```

### Generate Cluecumber report:

```bash
mvn cluecumber:reporting
```

Report output:

    target/generated-report/index.html

***

## ✅ 10. GitHub Actions CI Example

```yaml
name: Engineering Suite Automation

on:
  workflow_dispatch:
  push:

jobs:
  run-tests:
    runs-on: ubuntu-latest

    steps:
      - uses: actions/checkout@v3

      - name: Setup Java
        uses: actions/setup-java@v3
        with:
          distribution: temurin
          java-version: 21

      - name: Download FeatureBundle from Xray
        run: echo "Download + unzip Xray FeatureBundle into resources/features"

      - name: Run Tests
        run: mvn clean verify -pl automation-framework

      - name: Generate Cluecumber Report
        run: mvn cluecumber:reporting

      - name: Upload Results to Xray
        run: echo "Upload cucumber.json to Xray"
```

***

## ✅ 11. Getting Started

```bash
git clone <your-repo>
cd engineering-suite
mvn clean verify
mvn cluecumber:reporting
```

***
