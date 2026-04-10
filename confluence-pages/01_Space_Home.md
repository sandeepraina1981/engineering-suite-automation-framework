# Confluence Page 1: Space Home - Engineering Suite QA

## 🎯 Vision
To provide a high-performance, audit-ready, and human-centric test automation solution for Lenze Engineering Suites, ensuring maximum reliability across both UI and REST API layers.

## 🏛️ Core Pillars
*   **Playwright-First**: A single engine for all automation, ensuring speed and context sharing.
*   **Screenplay Pattern**: Modular, reusable, and readable test logic based on SOLID principles.
*   **Ability-Driven Casting**: Intelligent staging where Actors are strictly provisioned with only the capabilities they need.
*   **Persona-Centric**: Gherkin steps written from the perspective of real-world names (Alex, Charlie, etc.).
*   **Docker-Native**: Zero environment drift between local development and CI/CD.

## 👥 Persona Management Matrix
| Persona | Name | Role | Browser | Primary Type | Description |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **ALEX** | Alex | ADMIN | **MSEdge** | UI | Admin workflows on Edge. |
| **CHRIS** | Chris | ADMIN | **Chrome** | UI | Admin workflows on Chrome. |
| **BLAKE** | Blake | NORMAL | **MSEdge** | UI | Standard user on Edge. |
| **CASEY** | Casey | NORMAL | **Chrome** | UI | Standard user on Chrome. |
| **CHARLIE**| Charlie| ADMIN | N/A | API | Admin security/data API testing. |
| **DANA** | Dana | NORMAL | N/A | API | Standard user API verification. |

---
*Next Page: [Architecture Deep Dive](./02_Architecture_Deep_Dive.md)*
