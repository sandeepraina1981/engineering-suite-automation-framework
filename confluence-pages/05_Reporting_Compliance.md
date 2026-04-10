# Confluence Page 5: Reporting & Compliance

## 📊 1. Dual-Reporting Strategy
The framework produces two distinct types of reports to satisfy both technical and business stakeholders.

### 1.1 Serenity HTML Dashboard (Technical)
*   **Target**: Developers and QA Engineers.
*   **Purpose**: Root-cause analysis and interactive debugging.
*   **Features**: Full stack traces, Playwright step performance metrics, and categorized living documentation.
*   **Location**: `automation-tests/target/site/serenity/index.html`

### 1.2 PDF Audit Log (Compliance)
*   **Target**: Management, Stakeholders, and Auditors.
*   **Purpose**: Permanent, non-technical proof of execution.
*   **Features**: 
    *   Chronological step-by-step log.
    *   **Mandatory Screenshots**: Every step includes a visual snapshot.
    *   **Failure Highlight**: Errors are clearly flagged in red with associated error messages.
*   **Location**: `automation-tests/target/engineering-suite-audit-log.pdf`

## 🧪 2. Xray Cloud Sync
All execution data is automatically synchronized back to Jira via the Xray Cloud V2 API.

*   **Authentication**: Managed via `XRAY_CLIENT_ID` and `XRAY_CLIENT_SECRET`.
*   **Evidence**: The `cucumber.json` result is pushed to the Jira Test Execution issue, creating a bi-directional link between requirements and automation results.

---
*Next Page: [Maintenance Guide](./06_Maintenance_Guide.md)*
