# Architecture Deep Dive

## 🏛️ Design Patterns
This framework follows the **Screenplay Pattern**, which is the evolution of the Page Object Model (POM). It emphasizes SOLID principles and provides a clear separation of concerns.

- **Actors**: The "Who" of the test (e.g., `Alex`, `Chris`, `Charlie`).
- **Abilities**: The "How" (e.g., `BrowseTheWebWithPlaywright`, `CallAnApiWithPlaywright`).
- **Tasks**: The "What" - High-level business actions (e.g., `LoginToEngineeringSuite`).
- **Interactions**: Low-level actions (e.g., `SelectFromCustomDropdown`).
- **Questions**: How we verify state (e.g., `CurrentDashboardTitle`).

## 👥 Persona-Based Actor Model
Unlike traditional frameworks that use generic `Admin` or `User` strings, we use **Personas**.

### Why Personas?
- **Real-World Names**: Makes Gherkin steps more intuitive (`Given Alex is on the dashboard`).
- **Immutable State**: Each persona is mapped to a specific `UserLevel` and `BrowserType` in the `Persona` enum.
- **Strict Capabilities**: 
    - `UI` personas *only* have the `BrowseTheWeb` ability.
    - `API` personas *only* have the `CallAnApi` ability.
    - This prevents tests from performing unintended actions (e.g., an API-only persona trying to click a button).

## 🚀 Playwright Integration
Playwright is used as the **universal engine** for both UI and REST API automation.

## 🐳 Dockerized Execution
The framework is fully containerized to ensure environment parity across local development and CI/CD pipelines.
- **Base Image**: Uses `mcr.microsoft.com/playwright/java:v1.40.0-jammy` (pre-configured with Playwright browsers and Linux dependencies).
- **Maven Dependency Caching**: Optimized Docker layers to cache dependencies, speeding up subsequent runs.
- **Reporting Persistence**: Test results and PDF audit logs are generated within the container and can be extracted via Docker volumes.

### UI Automation
- Supports **MSEdge** and **Chrome**.
- Configured in `serenity.conf`.
- Automatic screenshot capture on failure for both Serenity and PDF reports.

### API Automation
- Custom ability **`CallAnApiWithPlaywright`** provides an `APIRequestContext`.
- Allows for **Fast & Secure** data exchange.
- Seamlessly shares state (cookies/tokens) between UI and API actions.

## 🔐 OAuth2 & PKCE
Handling B2C login screens can be complex. The framework includes a specialized **`OAuth2PKCEHandler`** that uses Playwright to:
1. Generate `Code Verifier` and `Code Challenge`.
2. Interactively navigate the B2C login screen.
3. Extract the authorization `code` and exchange it for a token.

## 🧪 Xray & Reporting Flow
1. **Import**: At the start of the execution, the framework can fetch Gherkin features from Xray using the Jira Issue keys.
2. **Execute**: Cucumber scenarios run and generate a standard JSON result.
3. **Export**: Upon completion, the `XrayCloudClient` automatically pushes the JSON results back to Xray Test Execution.
4. **Audit**: A post-processing step converts the Cluecumber HTML report into a professional **PDF audit log** with screenshots for long-term storage and compliance.

## 🌍 Environment Management
Environments are managed in `serenity.conf`. Using Maven profiles (`-Denvironment=dev`), you can switch between **DEV**, **QA**, and **PROD** without changing any code.
