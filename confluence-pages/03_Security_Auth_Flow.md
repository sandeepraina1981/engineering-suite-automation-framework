# Confluence Page 3: Security & Auth Flow

## 🔐 1. OAuth2 with PKCE Handshake
The framework is specialized for secure Azure B2C login screens using the PKCE (Proof Key for Code Exchange) flow.

### 1.1 Technical Sequence Diagram
```mermaid
sequenceDiagram
    autonumber
    participant A as Actor
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

## 🔑 2. Secure Credential Management
The framework avoids hardcoded strings through the **`CredentialsProvider`** utility.

### 2.1 Dynamic Loading Logic
1.  **Actor Memory**: During casting, the actor is assigned a `UserLevel` (Admin or Normal).
2.  **Provider Request**: When a login task executes, it requests credentials for that level from the provider.
3.  **Environment Sync**: The provider pulls from System Environment Variables (e.g., `ADMIN_USER`, `ADMIN_PASS`).
4.  **Fallback**: If no environment variables are found (local dev), it uses defaults specified in the configuration.

---
*Next Page: [Execution & CI/CD](./04_Execution_CICD.md)*
