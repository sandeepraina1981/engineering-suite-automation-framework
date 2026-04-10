# Confluence Page 2: Architecture & Design

## 🏛️ 1. The Screenplay Pattern
This framework implements the **Screenplay Pattern**, which treats tests as actors performing tasks to achieve goals.

### 1.1 Detailed Generic Class Diagram
*Visual representation of framework relationships, independent of specific test cases.*

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

## 🚀 2. Generic Architecture & Execution Sequence
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

## 🎭 3. Ability-Driven Casting
Unlike standard casting, our `EngineeringSuiteCast` dynamically provisions actors:
*   If **Alex** is summoned, the cast launches an **MSEdge** instance and assigns an **Admin** role.
*   If **Charlie** is summoned, the cast initializes only a **Playwright APIRequestContext**, ensuring zero UI overhead for API tests.

---
*Next Page: [Security & Auth Flow](./03_Security_Auth_Flow.md)*
