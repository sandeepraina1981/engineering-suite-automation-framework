# Confluence Page 6: User & Maintenance Guide

## 🛠️ 1. Adding a New Test
The framework is designed for rapid scaling. Follow these 3 steps:

1.  **Feature File**: Create `your_feature.feature` in `src/test/resources/features`. Use Persona names in Gherkin.
2.  **Task**: Define the business logic in `src/test/java/.../tasks/`.
3.  **Step Def**: Map the Gherkin steps in `src/test/java/.../steps/` and set the stage using `EngineeringSuiteCast`.

## 👥 2. Managing Personas
To add a new user type or browser target:
1.  Open `Persona.java` in the `automation-framework` module.
2.  Add a new enum entry (e.g., `JORDAN_ADMIN_EDGE`).
3.  The `EngineeringSuiteCast` will automatically handle the new persona on the next run.

## 🆘 3. Troubleshooting & FAQ

### Q: Why is my API test trying to launch a browser?
**A**: Check your Persona definition. If `preferredBrowser` is NOT null, the cast will assume it is a UI persona.

### Q: How do I rotate passwords?
**A**: Do not change code. Update the `ADMIN_PASS` or `NORMAL_PASS` environment variables in your local machine or GitHub Secrets.

### Q: The PDF report is missing screenshots.
**A**: Ensure your `serenity.conf` has `take.screenshots = FOR_EACH_ACTION` set. The PDF generator depends on these being embedded in the `cucumber.json`.

---
**End of Multi-Page Documentation.**
