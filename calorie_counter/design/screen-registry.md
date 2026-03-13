# Screen Registry — AI Calorie Counter App

Version: 1.0

## Purpose

This file maps application screens to their product role, expected code artifacts, and Figma source status.

It is the source of truth for:
- which screens belong to the app
- which screens are required vs optional
- which screens exist in Figma
- which screens should be inferred from the design system if missing

Agents must only implement screens listed here unless explicitly instructed otherwise.

---

## 1. Registry Rules

1. If a screen is listed as required, it belongs to the app scope.
2. If a screen exists in Figma, replicate it closely.
3. If a required screen is missing from Figma, generate it using the same design language.
4. If a Figma frame exists but is not listed here, do not implement it automatically.
5. Duplicate visual variants should not produce duplicate production screens unless explicitly approved.

---

## 2. Screen Registry

| Screen ID | Kotlin File | Product Area | Required | Figma Status | Build Rule | Notes |
|---|---|---|---|---|---|---|
| welcome | WelcomeScreen.kt | Onboarding | yes | present | use_figma | Intro/value proposition screen |
| age | AgeScreen.kt | Onboarding | yes | present | use_figma | Age selection |
| weight | WeightScreen.kt | Onboarding | yes | present | use_figma | Current weight + units |
| goal | GoalScreen.kt | Onboarding | yes | present | use_figma | Goal selection |
| gender | GenderScreen.kt | Onboarding | yes | present | use_figma | Gender selection |
| dashboard | DashboardScreen.kt | Core | yes | present | use_best_figma_variant | One dashboard variant should be selected as canonical |
| meal-type-selection | MealTypeSelectionScreen.kt | Core | yes | missing | infer_from_design_system | Must be added for the meal logging flow |
| camera-capture | CameraCaptureScreen.kt | Core | yes | partial | infer_or_implement | Current Figma shows scanning/analysis rather than a full capture screen |
| photo-preview | PhotoPreviewScreen.kt | Core | yes | missing | infer_from_design_system | Must exist between capture and analysis |
| ai-analysis | AIAnalysisScreen.kt | Core | yes | present | use_figma | Current scanning/loading screen |
| meal-result | MealResultScreen.kt | Core | yes | partial | infer_from_figma_detail | Nutrition detail exists, but final AI result/save flow should be explicit |
| daily-nutrition | DailyNutritionScreen.kt | Tracking | yes | present | use_figma | Daily meal list and totals |
| meal-history | MealHistoryScreen.kt | Tracking | optional | missing | infer_from_design_system | Useful but not mandatory for first MVP if Daily Nutrition covers history-lite |
| meal-detail | MealDetailScreen.kt | Tracking | yes | present | use_figma | Existing nutrition detail screen can serve as meal detail |
| progress | ProgressScreen.kt | Tracking | yes | present | choose_one_variant | Two progress variants exist; only one production screen should be canonical |
| profile | ProfileScreen.kt | Secondary | optional | present | use_figma | Secondary screen, not core to meal logging |
| settings | SettingsScreen.kt | Secondary | optional | missing | infer_from_design_system | Only add if product requires it in MVP |
| nutrition-report | NutritionReportScreen.kt | Secondary | optional | missing_or_unclear | defer_or_infer | Mentioned in profile menu but not clearly shown |
| favorites | FavoritesScreen.kt | Secondary | optional | missing_or_unclear | defer_or_infer | Mentioned in profile menu but not clearly shown |
| manual-meal-entry | ManualMealEntryScreen.kt | Secondary | optional | missing | infer_from_design_system | Useful fallback for failed AI detection |
| edit-meal | EditMealScreen.kt | Secondary | optional | missing | infer_from_design_system | Post-save correction; listed in app-flow.md optional section |
| analysis-error | AnalysisErrorScreen.kt | State | yes | missing | infer_from_design_system | Required error state |
| empty-history | EmptyHistoryState.kt | State | yes | missing | infer_from_design_system | Required empty state |
| empty-progress | EmptyProgressState.kt | State | yes | missing | infer_from_design_system | Required empty state |
| camera-permission | CameraPermissionScreen.kt | State | yes | missing | infer_from_design_system | Required permission state |

---

## 3. Canonical Interpretation of Current Figma

Based on the current Figma screenshot set, these mappings are recommended:

### Present screens
- WelcomeScreen
- AgeScreen
- WeightScreen
- GoalScreen
- GenderScreen
- ProfileScreen
- MealDetailScreen (currently shown as Nutrition detail)
- AIAnalysisScreen (currently shown as Scanning)
- DashboardScreen
- ProgressScreen
- DailyNutritionScreen

### Present but ambiguous
- DashboardScreen has more than one visual variant
- ProgressScreen has more than one visual variant
- MealDetailScreen exists, but MealResultScreen for post-analysis review/save is not clearly separated

### Missing from current Figma
- MealTypeSelectionScreen
- PhotoPreviewScreen
- explicit CameraCaptureScreen
- AnalysisErrorScreen
- CameraPermissionScreen
- EmptyHistoryState
- EmptyProgressState
- MealHistoryScreen (if treated separately from DailyNutritionScreen)

---

## 4. Required Figma Follow-Up

The design team should ideally add these frames to Figma:
1. MealTypeSelectionScreen
2. CameraCaptureScreen
3. PhotoPreviewScreen
4. MealResultScreen
5. AnalysisErrorScreen
6. CameraPermissionScreen
7. Empty state variants for history/progress

Until then, these screens should be generated using the existing design language.

---

## 5. Duplicate / Extra Screen Policy

### Progress
Current Figma contains multiple progress/dashboard-style visualizations.
Rule:
- choose one canonical ProgressScreen for production
- treat others as alternates or archive

### Dashboard
If more than one dashboard/home variant exists:
- pick one canonical DashboardScreen
- do not implement duplicate production routes

### Profile menu destinations
Items visible inside Profile such as:
- Daily Intake
- My Goals
- Nutrition Report
- Favorites Food

must not automatically become fully separate screens unless they are added to the confirmed product scope.

---

## 6. Ignore List Policy

If Figma contains any of the following, ignore them unless explicitly added to this registry:
- archived concepts
- experiments
- duplicate variants
- tablet layouts
- marketing pages
- admin/internal tooling

---

## 7. Build Instructions for Missing Screens

If a required screen is missing from Figma:
1. Reuse existing design tokens, spacing, cards, and button treatments
2. Preserve the same visual language
3. Keep navigation aligned with docs/app-flow.md
4. Mark the implementation as inferred from design

Example code comment:
`// inferred_from_design_system`

---

## 8. Implementation Priority

### Highest priority
- WelcomeScreen
- AgeScreen
- WeightScreen
- GoalScreen
- GenderScreen
- DashboardScreen
- MealTypeSelectionScreen
- CameraCaptureScreen
- PhotoPreviewScreen
- AIAnalysisScreen
- MealResultScreen
- DailyNutritionScreen
- MealDetailScreen
- ProgressScreen

### Medium priority
- ProfileScreen
- MealHistoryScreen
- CameraPermissionScreen
- AnalysisErrorScreen

### Lower priority
- SettingsScreen
- FavoritesScreen
- NutritionReportScreen
- ManualMealEntryScreen

---

## 9. Agent Rule

Before generating or modifying UI:
1. Read `design/ui-design-rules.md`
2. Read `docs/app-flow.md`
3. Follow this screen registry
4. Do not create screens outside this registry unless instructed

---

End of document
