# Mermaid Diagrams: Engineering Suite Automation Framework

Use the following Mermaid.js code blocks in Confluence (via the Mermaid macro) or any Markdown-compatible tool to generate the architecture and sequence diagrams.

---

## 🏛️ 1. Enterprise Framework Architecture
This diagram shows the layered structure of the framework and how it connects to external systems.

```mermaid
graph TB
    subgraph "Test Management & Requirements"
        Xray[Xray Cloud / Jira]
        Gherkin[Cucumber Gherkin Features]
    end

    subgraph "Automation Framework (Serenity BDD)"
        Actor[Actor / Persona Manager]
        Ability[Abilities: Playwright UI/API]
        Task[Tasks: Business Logic]
        Inter[Interactions: Low-level UI/API]
        Quest[Questions: Assertions/State]
    end

    subgraph "Execution Engine (Playwright)"
        Browser[Browser Context: MSEdge/Chrome]
        APIContext[API Request Context]
    end

    subgraph "Audit & Reporting"
        Serenity[Serenity HTML Dashboard]
        AuditLog[Professional PDF Audit Log]
    end

    Xray --> Gherkin
    Gherkin --> Actor
    Actor --> Ability
    Ability --> Task
    Task --> Inter
    Inter --> Quest
    Inter --> Browser
    Inter --> APIContext
    Browser --> Serenity
    APIContext --> Serenity
    Serenity --> AuditLog
    AuditLog --> Xray
```

---

## 🔄 2. Detailed Test Execution Sequence
This shows the step-by-step lifecycle of a single test execution.

```mermaid
sequenceDiagram
    participant J as Jira / Xray
    participant M as Maven / Runner
    participant P as Persona (Alex/Chris)
    participant S as Screenplay Stage
    participant PL as Playwright Engine
    participant R as Reporting (PDF/HTML)

    J->>M: 1. Trigger Workflow Dispatch
    M->>J: 2. Import Gherkin Features (API V2)
    M->>S: 3. Initialize OnlineCast
    S->>P: 4. Actor Summoned (Persona Type)
    P->>PL: 5. Equipped with UI/API Ability
    P->>PL: 6. Perform Tasks (Login, Create, Verify)
    PL-->>P: 7. Task Success / Screenshot Capture
    P->>R: 8. Aggregate JSON Result & Screenshots
    R->>R: 9. Generate PDF Audit Log
    R->>J: 10. Push Results back to Xray (API V2)
```

---

## 🔐 3. OAuth2 with PKCE Authentication Flow
This illustrates the interaction between the framework and the B2C login screens.

```mermaid
sequenceDiagram
    participant F as Framework (Actor)
    participant H as OAuth2PKCEHandler
    participant P as Playwright (Browser)
    participant B2C as Azure B2C Login
    participant T as Token Endpoint

    F->>H: Request Authentication
    H->>H: Generate Code Verifier & Challenge
    H->>P: Navigate to Auth URL (with Challenge)
    P->>B2C: Load Login Screen
    B2C->>P: User Enters Credentials
    P->>B2C: Submit Login
    B2C->>P: Redirect with Auth Code
    P-->>H: Extract Auth Code from URL
    H->>T: Exchange Code + Verifier for Token
    T-->>H: Return Access Token
    H-->>F: Equipped with CallAnApi Ability
```

---

## 👥 4. Persona & Capability Matrix
This shows how the framework maps personas to their specific roles and targets.

```mermaid
graph LR
    subgraph "Persona Definition"
        A[Alex]
        C[Chris]
        B[Blake]
        K[Casey]
        R[Charlie]
    end

    subgraph "Role & Browser Mapping"
        A -- Admin --> Edge[MSEdge UI]
        C -- Admin --> Chrome[Chrome UI]
        B -- User --> Edge
        K -- User --> Chrome
        R -- Admin --> API[API Only]
    end

    subgraph "Execution Capability"
        Edge --> PW_UI[Playwright UI Ability]
        Chrome --> PW_UI
        API --> PW_API[Playwright API Ability]
    end
```
