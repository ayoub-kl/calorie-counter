# Cursor starter pack — Calorie Counter Android App

This package is a ready-to-drop Cursor project setup for the Android calorie counter app we scoped.

## What this package includes

- `.cursor/rules/` with focused Cursor rules files
- `docs/` with product and architecture reference docs for the agent
- `templates/` with starter prompt templates you can paste into Cursor Agent

## App context

This app is an Android meal logging app where the user:

1. selects a meal type (breakfast, lunch, dinner, snack)
2. opens the camera
3. takes a meal photo
4. sends it to an AI/backend analysis pipeline
5. reviews detected foods, calories, and macros
6. corrects the result if needed
7. saves the meal into history

The product principle is simple:

**AI helps, but the user stays in control.**

## Recommended tech baseline

- Kotlin
- Jetpack Compose
- Hilt
- Navigation Compose
- Retrofit + OkHttp
- Room
- Coroutines + Flow
- CameraX
- Coil
- JUnit / MockK / Turbine / Compose UI tests

## How to use

1. Copy the `.cursor` folder into your repository root.
2. Copy the `docs/` folder into your repository if you want the agent to have local reference material.
3. Open the repo in Cursor.
4. Keep your architecture and contracts aligned with the rules.
5. Use the templates in `templates/` as starting prompts for feature implementation.

## Suggested first tasks in Cursor

1. Create app module and package structure
2. Implement meal type selection screen
3. Implement CameraX capture flow
4. Define network DTOs and domain models for meal analysis
5. Implement editable meal result screen
6. Persist meals locally with Room
7. Build history screen and meal detail screen

## Suggested MCP/server setup

Start minimal.

Good first integrations:
- GitHub or GitLab
- Figma (only if you have mocks)
- Sentry later
- Backend/API or database tools only when they are real and stable

Avoid loading too many tools on day 1.

## Notes

These rules are intentionally strict. They are optimized for:
- maintainability
- clear layering
- trustable AI UX
- reviewable diffs
- future extensibility without premature complexity

## UI rules for this project

Agents must read:
/design/ui-design-rules.md

before generating or modifying any UI.

All UI must follow the design language defined there.