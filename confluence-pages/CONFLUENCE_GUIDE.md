# Confluence Documentation: Engineering Suite Automation Framework

## 🎯 1. Objective & Design Philosophy
The **Engineering Suite Automation Framework** is an enterprise-level, BDD-driven solution designed to provide high-performance, audit-ready testing for Lenze Engineering Suites. 

### 1.1 Core Pillars
*   **Playwright-First**: A single engine for UI and REST API automation.
*   **Screenplay Pattern**: SOLID-based design using Tasks, Interactions, and Questions.
*   **Ability-Driven Casting**: Staging and casting depend strictly on defined **Abilities** (via `EngineeringSuiteCast`).
*   **Secure Credential Management**: Role-based credentials (Admin/User) are retrieved from secure stores via `CredentialsProvider`.
*   **Docker-Native**: Absolute environment parity via containerization.

---

## 🏗️ 2. Architectural Overview

### 2.1 Framework Architecture Diagram (Top-Down Flow)
```mermaid
graph TD
    %% Layer 1: Requirements
    subgraph L1 [1. TEST MANAGEMENT & REQUIREMENTS]
        Xray([Xray Cloud / Jira])
        Gherkin[Cucumber Gherkin Features]
    end

    %% Layer 2: Intelligent Orchestration
    subgraph L2 [2. SCREENPLAY ORCHESTRATION]
        Stage[Stage / Cast: EngineeringSuiteCast]
        Actor[Actor / Persona]
        Creds[CredentialsProvider: Secure Auth]
        Ability[Abilities: UI/API]
    end

    %% Layer 3: Technical Execution
    subgraph L3 [3. TECHNICAL EXECUTION ENGINE]
        Task[Tasks: Role-Based Logic]
        Playwright[Playwright Engine]
    end

    %% Layer 4: Reporting
    subgraph L4 [4. REPORTING & COMPLIANCE]
        Serenity[Serenity HTML Report]
        AuditLog[Professional PDF Audit Log]
    end

    %% Vertical Navigation
    Xray -->|Imports| Gherkin
    Gherkin -->|Summons| Stage
    Stage -->|Identifies Persona| Actor
    Actor -->|Fetches Role-Based Data| Creds
    Actor -->|Equipped with| Ability
    Actor -->|Performs| Task
    Task -->|Drives| Playwright
    Playwright -->|Evidence| Serenity
    Serenity -->|Formats| AuditLog
    AuditLog -->|Syncs| Xray

    style L1 fill:#f9f9f9,stroke:#333
    style L2 fill:#e1f5fe,stroke:#01579b
    style L3 fill:#fff3e0,stroke:#e65100
    style L4 fill:#f1f8e9,stroke:#33691e
```

### 2.2 Detailed Generic Class Diagram
*Visual representation of framework structural flow and relationships.*

```mermaid
classDiagram
    direction TB

    %% 1. Configuration & Persona Definition
    namespace Configuration {
        class Persona { <<enumeration>> Role, Browser, Target }
        class UserLevel { <<enumeration>> ADMIN, NORMAL_USER }
        class BrowserType { <<enumeration>> MSEDGE, CHROME }
    }

    %% 2. Orchestration & Intelligence
    namespace Orchestration {
        class EngineeringSuiteCast { +actorNamed(name) Actor }
        class CredentialsProvider { +getCredentials(UserLevel) }
    }

    %% 3. The Screenplay Core (Actor & Abilities)
    namespace Actor_Core {
        class Actor { +attemptsTo(Task), +remember(Role) }
        class ActorMemory { <<state>> UserLevel, Tokens, Context }
        class Ability { <<interface>> }
        class UIAbility { BrowseTheWebWithPlaywright }
        class APIAbility { CallAnApiWithPlaywright }
    }

    %% 4. Logical Abstractions (The Flow)
    namespace Logic_Abstractions {
        class Task { <<interface>> performAs(Actor) }
        class GenericUITask { +interactWithPage() }
        class GenericAPITask { +callAPIContext() }
    }

    %% 5. Infrastructure & Engines
    namespace Execution_Engines {
        class PlaywrightUI { <<Page>> Click, Fill, Screenshot }
        class PlaywrightAPI { <<RequestContext>> GET, POST, PUT }
    }

    %% RELATIONSHIPS (Strictly Vertical)
    Persona --> UserLevel : Defines
    Persona --> BrowserType : Targets
    EngineeringSuiteCast ..> Persona : Consults Config
    EngineeringSuiteCast --> Actor : Summons & Equips
    
    Actor "1" *-- "1" ActorMemory : Manages State
    Actor "1" *-- "n" Ability : Possesses
    
    UIAbility --|> Ability : implements
    APIAbility --|> Ability : implements
    
    GenericUITask ..|> Task : implements
    GenericAPITask ..|> Task : implements
    
    GenericUITask --> UIAbility : Delegates UI Actions
    GenericAPITask --> APIAbility : Delegates API Actions
    
    UIAbility --> PlaywrightUI : Drives
    APIAbility --> PlaywrightAPI : Drives
    
    GenericUITask ..> CredentialsProvider : Secures Auth Data
    GenericAPITask ..> CredentialsProvider : Secures Auth Data
```

---

## 🔄 3. Detailed Test Execution Lifecycle

### 3.1 Generic Architecture & Execution Sequence
*End-to-end lifecycle of a generic test (UI or API).*

```mermaid
sequenceDiagram
    autonumber
    participant D as Docker Container
    participant S as Stage (EngineeringSuiteCast)
    participant A as Actor (Persona)
    participant CP as CredentialsProvider
    participant T as Generic Task (UI/API)
    participant P as Playwright Engine
    participant R as Reporting (Xray/PDF)

    Note over D, R: Generic Framework Execution Lifecycle

    D->>S: 1. Initiate Cast with Actor Name
    S->>S: 2. Match Actor Name to Persona Logic
    S->>A: 3. Create Actor & Initialize Memory
    S->>P: 4. Launch Specific Env (Browser or API Context)
    A->>CP: 5. Request Role-Based Credentials (Admin/User)
    CP-->>A: 6. Provision Secure Credentials
    A->>T: 7. Perform Business Logic (Task)
    T->>P: 8. Execute Technical Actions via Ability
    P-->>T: 9. Return Response & Evidence (Screenshots)
    T-->>A: 10. Task Completed
    A->>R: 11. Aggregate Evidence into JSON
    R->>R: 12. Transform JSON to PDF Audit Log
    R->>R: 13. Synchronize Results to Xray Cloud
```

---

## 🔐 4. OAuth2 with PKCE Authentication Flow
*Technical handshake using Playwright to handle B2C login.*

```mermaid
sequenceDiagram
    autonumber
    participant A as Actor (User/Admin)
    participant H as OAuth2PKCEHandler
    participant B as Playwright Browser
    participant B2C as Identity Provider (B2C)
    participant TS as Token Service

    A->>H: 1. Request Secure Auth
    H->>H: 2. Generate S256 Challenge & Verifier
    H->>B: 3. Navigate to Auth URL + Challenge
    B->>B2C: 4. Present B2C Login Screen
    B2C-->>B: 5. User Enters Credentials
    B2C->>B: 6. Redirect with Authorization Code
    B-->>H: 7. Extract Auth Code from URL
    H->>TS: 8. POST Auth Code + PKCE Verifier
    TS-->>H: 9. Return JWT Access Token
    H-->>A: 10. Actor Equipped with API Ability
```

---

## 👥 5. Persona Management Matrix
| Persona | Name | Role | Browser | Primary Type |
| :--- | :--- | :--- | :--- | :--- |
| **ALEX** | Alex | ADMIN | **MSEdge** | UI |
| **CHRIS** | Chris | ADMIN | **Chrome** | UI |
| **BLAKE** | Blake | NORMAL | **MSEdge** | UI |
| **CASEY** | Casey | NORMAL | **Chrome** | UI |
| **CHARLIE**| Charlie| ADMIN | N/A | API |
| **DANA** | Dana | NORMAL | N/A | API |

---

## 📊 6. Reporting & Artifacts
1.  **Serenity HTML**: Interactive engineering dashboard.
2.  **PDF Audit Log**: Professional compliance document with screenshots.
3.  **Xray Sync**: Automated bidirectional requirement sync.
