# Test Automation Design Blueprint: Engineering Suite Framework

## 🎯 1. Design Philosophy
The **Engineering Suite Automation Framework** is engineered as an enterprise-grade solution that moves beyond traditional "scripts" toward a robust, "Screenplay-driven" architecture.

### 1.1 Core Pillars
*   **Playwright-First**: A single, high-performance engine for both UI and REST API automation.
*   **Screenplay Pattern**: A SOLID-based design that replaces brittle Page Objects with reusable Tasks, Interactions, and Questions.
*   **Persona-Centric**: Tests are written using real-world personas (Alex, Chris, etc.), making the Gherkin layer a true "Living Documentation".

---

## 🔄 2. The Test Execution Lifecycle (The "Script")

### Phase 1: Initiation (The Trigger)
*   **Source**: GitHub Actions (Workflow Dispatch).
*   **Inputs**: Environment (DEV/QA/PROD) and Browser (MSEdge/Chrome).
*   **Containerization**: The framework is bundled into a Docker image based on `mcr.microsoft.com/playwright/java`.
*   **Action**: The CI/CD pipeline triggers the execution within the container.

### Phase 2: Setup (The Stage)
*   **Mechanism**: Serenity BDD's `OnlineCast`.
*   **Action**: The framework initializes a "Stage" where Actors will perform.
*   **Mapping**: The Gherkin step (`Given Alex...`) is parsed, and the corresponding **Persona** is summoned from the `Persona` enum.

### Phase 3: Capability (The Abilities)
*   **UI Actors**: Equipped with `BrowseTheWebWithPlaywright` (targeting MSEdge or Chrome).
*   **API Actors**: Equipped with `CallAnApiWithPlaywright` (using Playwright's APIRequestContext).
*   **Hybrid**: Capability to share state (tokens/cookies) between UI and API contexts.

### Phase 4: Security (The Auth Flow)
*   **Handler**: `OAuth2PKCEHandler`.
*   **Flow**: 
    1.  Generate PKCE Code Verifier & Challenge.
    2.  Interactively navigate the Azure B2C login screen.
    3.  Extract the Auth Code and exchange it for a secure token.
    4.  Store the token in the Actor's memory for all subsequent calls.

### Phase 5: Performance (The Tasks)
*   **Logic**: Actors perform high-level **Tasks** (e.g., `CreateEngineeringProject`).
*   **Decomposition**: Tasks are broken down into **Interactions** (e.g., `Click`, `EnterText`, `Hover`).
*   **Stability**: Playwright's native auto-waiting ensures these actions are resilient to UI latency.

### Phase 6: Validation (The Questions)
*   **Assertion**: Actors ask **Questions** (e.g., `CurrentDashboardTitle.displayed()`).
*   **Comparison**: The retrieved state is compared against the expected value using Hamcrest matchers.

### Phase 7: Evidence (The Reporting)
*   **Serenity HTML**: A rich, interactive dashboard for engineers to perform root-cause analysis.
*   **PDF Audit Log**: A professional, step-by-step PDF generated from `cucumber.json`.
*   **Screenshots**: Every step (and especially failures) includes a full-page screenshot embedded in both reports.

### Phase 8: Synchronization (The Sync)
*   **Client**: `XrayCloudClient`.
*   **Final Action**: Test status, execution metrics, and audit evidence are pushed back to Xray Cloud via the V2 API, closing the loop with Test Management.

---

## 🛠️ 3. Maintenance & Scalability
*   **Adding Personas**: Update `Persona.java` to define new user roles or browser targets.
*   **Adding Tasks**: Create new classes in the `tasks` package to encapsulate new business workflows.
*   **Environment Changes**: Update `serenity.conf` to add or modify environment URLs and API endpoints.
