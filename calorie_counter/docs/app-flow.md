# App Flow — AI Calorie Counter App

Version: 1.0

## Purpose

This document defines the canonical user flow for the AI calorie counter app. It is the product-level source of truth for navigation, screen sequence, and user intent.

The app's core use case is:

Choose meal type → capture food photo → run AI analysis → review calories/macros → save meal → track history and progress.

If a Figma layout conflicts with this flow, the product flow takes precedence.

---

## 1. Primary User Journey

### Main happy path

1. User opens the app
2. User completes onboarding if first launch
3. User lands on Dashboard / Home
4. User selects a meal type:
   - Breakfast
   - Lunch
   - Dinner
   - Snack
5. App opens Camera Capture
6. User takes a photo of the meal
7. App shows Photo Preview
8. User confirms the photo or retakes it
9. App runs AI Analysis
10. App shows Meal Result
11. User reviews:
    - detected food name
    - calories
    - protein
    - carbs
    - fats
    - optional portion/weight estimate
12. User saves meal
13. App updates Dashboard, Daily Nutrition, History, and Progress

---

## 2. App Sections

The app is organized into these functional sections:

### A. Onboarding
Used only on first launch or when profile setup is incomplete.

### B. Core Meal Logging
The primary product value:
meal selection, camera capture, AI analysis, result review, and save.

### C. Tracking
Daily nutrition, meal history, and progress analytics.

### D. Profile / Settings
User profile and app preferences.

---

## 3. Canonical Screen Flow

## 3.1 First Launch Flow

1. WelcomeScreen
2. AgeScreen
3. WeightScreen
4. GoalScreen
5. GenderScreen
6. Optional ProfileCompletionScreen
7. DashboardScreen

Notes:
- Onboarding should be skippable only if product requirements explicitly allow it.
- If skipped, missing profile data may be requested later.

---

## 3.2 Returning User Flow

1. DashboardScreen
2. User selects a meal slot or taps the main add/camera action
3. MealTypeSelectionScreen
4. CameraCaptureScreen
5. PhotoPreviewScreen
6. AIAnalysisScreen
7. MealResultScreen
8. Save meal
9. Return to DashboardScreen or DailyNutritionScreen

---

## 3.3 Tracking Flow

From DashboardScreen, user can navigate to:

- DailyNutritionScreen
- MealHistoryScreen
- MealDetailScreen
- ProgressScreen

Typical tracking flow:

DashboardScreen
→ DailyNutritionScreen
→ MealDetailScreen

Alternative:

DashboardScreen
→ ProgressScreen

---

## 3.4 Profile Flow

DashboardScreen
→ ProfileScreen
→ optional SettingsScreen
→ optional NutritionReportScreen
→ optional FavoritesScreen

Profile is secondary to the core calorie logging flow.

---

## 4. Required Screens

These screens are part of the expected app scope.

### Onboarding
- WelcomeScreen
- AgeScreen
- WeightScreen
- GoalScreen
- GenderScreen

### Core meal logging
- DashboardScreen
- MealTypeSelectionScreen
- CameraCaptureScreen
- PhotoPreviewScreen
- AIAnalysisScreen
- MealResultScreen

### Tracking
- DailyNutritionScreen
- MealHistoryScreen
- MealDetailScreen
- ProgressScreen

### Secondary
- ProfileScreen

### Optional
- SettingsScreen
- FavoritesScreen
- NutritionReportScreen
- ManualMealEntryScreen
- EditMealScreen

---

## 5. Screen Responsibilities

### WelcomeScreen
Introduce the product and its value proposition.

### AgeScreen
Capture user age for profile and calorie calculation context.

### WeightScreen
Capture current weight and unit.

### GoalScreen
Capture goal:
- lose weight
- maintain weight
- gain weight

### GenderScreen
Capture gender if required by the product's nutrition logic.

### DashboardScreen
Main landing screen after onboarding.
Responsibilities:
- display summary of current day
- provide access to meal logging
- surface quick stats
- provide access to Progress and Daily Nutrition

### MealTypeSelectionScreen
User selects:
- Breakfast
- Lunch
- Dinner
- Snack

This must happen before camera capture unless meal type is already known from the user's action.

### CameraCaptureScreen
Responsibilities:
- open camera
- capture meal photo
- provide retake/cancel actions
- optionally allow gallery upload if product supports it

### PhotoPreviewScreen
Responsibilities:
- show captured image
- let user confirm or retake
- prevent accidental analysis on bad photos

### AIAnalysisScreen
Responsibilities:
- show analysis in progress
- present loading state
- reassure user the scan is ongoing

### MealResultScreen
Responsibilities:
- show AI-detected food result
- show calories and macros
- allow save
- optionally allow edit/correction before save

### DailyNutritionScreen
Responsibilities:
- show meals logged for the selected day
- display totals and meal entries

### MealHistoryScreen
Responsibilities:
- browse past entries by day or date range

### MealDetailScreen
Responsibilities:
- show a saved meal in detail
- display nutrition breakdown
- optionally allow edit/delete

### ProgressScreen
Responsibilities:
- show trends over time
- visualize calories/macros adherence
- optionally show weight trend

### ProfileScreen
Responsibilities:
- expose user profile and secondary app actions

---

## 6. Required UI States

Even if not present in Figma, these states are required for production readiness.

### Loading states
- initial dashboard loading
- camera setup loading
- AI analysis loading
- history loading

### Empty states
- no meals logged today
- no progress data yet
- no favorites

### Error states
- camera unavailable
- permission denied
- image capture failed
- AI analysis failed
- network unavailable
- save meal failed
- no food recognized / low confidence result

### Confirmation states
- photo confirmed
- meal saved successfully
- delete meal confirmation
- discard photo confirmation

---

## 7. Navigation Rules

1. The plus/floating action button should map to meal logging, not an ambiguous generic action.
2. If the user taps a specific meal card on Dashboard, the app may skip MealTypeSelectionScreen and go directly to CameraCaptureScreen with the meal type preselected.
3. PhotoPreviewScreen must appear between CameraCaptureScreen and AIAnalysisScreen.
4. AIAnalysisScreen must appear before MealResultScreen.
5. MealResultScreen must allow save before the meal is counted in tracking views.
6. Tracking screens must reflect saved meals only.

---

## 8. MVP vs Later Additions

### MVP
- onboarding
- dashboard
- meal type selection
- camera capture
- photo preview
- AI analysis
- meal result
- save meal
- daily nutrition
- progress
- profile basic shell

### Later
- favorites
- nutrition report export
- manual meal entry
- editing AI result
- barcode scan
- social/community features

---

## 9. Out-of-Scope Screens

The following should not be implemented unless explicitly added to the product scope:
- workout coaching
- recipes/discovery feed
- social feed/community
- admin/internal dashboards
- tablet-only layouts
- marketing landing pages

---

## 10. Agent Implementation Rule

When implementing screens:
1. Follow this app flow first
2. Use Figma as the visual source of truth where available
3. If a required screen is missing from Figma, generate it using the existing design language
4. Do not invent new user journeys not described in this document

---

## 11. Flow Summary Diagram

First launch:
Welcome
→ Age
→ Weight
→ Goal
→ Gender
→ Dashboard

Meal logging:
Dashboard
→ MealTypeSelection
→ CameraCapture
→ PhotoPreview
→ AIAnalysis
→ MealResult
→ Save Meal
→ Dashboard / DailyNutrition

Tracking:
Dashboard
→ DailyNutrition
→ MealDetail

Dashboard
→ Progress

Secondary:
Dashboard
→ Profile

---

End of document
