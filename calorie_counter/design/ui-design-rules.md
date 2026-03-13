
# UI Design Rules for AI Calorie App
Version: 1.0

This document defines how UI should be interpreted, generated, or extended when using Figma designs with AI coding agents.

---

# 1. Source of Truth

The **Figma file is the primary source of truth for UI layout and styling.**

Rules:

1. If a screen exists in Figma → replicate it as closely as possible.
2. If a screen does NOT exist in Figma but is required by the app flow → generate it using the same design system and visual style found in Figma.
3. Do not invent new design languages or visual styles.
4. Maintain consistency with the existing design patterns.

---

# 2. Design System Consistency

Generated screens must follow the same visual language found in Figma.

Maintain consistency in:

- Colors
- Typography
- Spacing
- Border radius
- Shadows
- Button style
- Card style
- Layout grid

Example style cues (derived from current Figma):

Primary Color: Lime Green  
Secondary Color: Orange  
Background: White or very light gray  

Cards:
- Rounded corners
- Soft shadow
- White background

Buttons:
- Rounded pill style
- Primary = Green
- Secondary = Orange

Spacing:
- Large vertical spacing
- Centered content
- Minimal text

---

# 3. Screen Generation Policy

If a screen required by the product flow is missing from Figma:

1. Reuse existing UI components whenever possible.
2. Follow the same layout hierarchy used in other screens.
3. Maintain visual spacing patterns.
4. Do not introduce new navigation patterns.
5. Mark generated screens as inferred.

Example comment in generated code:

// inferred_from_design_system

---

# 4. Component Reuse Rule

When generating UI:

Prefer using existing components visible in Figma.

Examples:

- PrimaryButton
- MealCard
- NutritionRow
- ProgressCard
- FoodItemRow

Avoid creating new components unless absolutely necessary.

---

# 5. Navigation Rules

Navigation must follow the defined application flow.

Main flow:

Onboarding  
→ Dashboard  
→ Meal Type Selection  
→ Camera Capture  
→ Photo Preview  
→ AI Analysis  
→ Meal Results  
→ Save Meal  
→ Dashboard Update

Secondary flow:

Dashboard  
→ Meal History  
→ Meal Detail

Agents must not introduce new flows unless explicitly instructed.

---

# 6. Handling Missing Screens

If a required screen is missing from Figma:

1. Generate the screen using the same design patterns.
2. Reuse components from existing screens.
3. Keep layout consistent with other screens.
4. Report the missing screen.

Example message:

"Screen 'PhotoPreview' was not found in Figma and was generated using the existing design system."

---

# 7. Ignoring Extra Figma Screens

If Figma contains experimental or unused screens:

The agent must ignore them unless they are listed in the screen registry.

Common ignored categories:

- Experiments
- Archive
- Admin
- Tablet layouts
- Marketing pages

---

# 8. Screen Naming Convention

Figma frames should follow predictable names:

Correct:

- MealSelectionScreen
- CameraScreen
- AnalysisScreen
- ResultsScreen
- HistoryScreen

Avoid:

- Frame 1
- Screen Copy
- Final Final 2
- Untitled

---

# 9. Layout Principles

Generated UI should follow these layout rules:

- Central content focus
- Clear call-to-action buttons
- Minimal text
- Visual hierarchy
- Consistent spacing
- Clear scanning states

---

# 10. AI-Specific UI States

The application must support these states even if not present in Figma:

Loading states:
- AI food analysis
- Image upload

Error states:
- Food not recognized
- Camera error
- Network error

Empty states:
- No meals logged
- No progress data

---

# 11. Accessibility Guidelines

Generated UI must follow basic accessibility rules:

- Sufficient color contrast
- Readable text size
- Clear button tap areas
- Avoid tiny interactive elements

---

# 12. Mobile Design Constraints

Target device: modern smartphones.

Design constraints:

- Vertical scrolling layouts
- Large touch targets
- Simple navigation
- Avoid deep menus

---

# 13. Design Change Policy

If Figma updates later:

1. Figma takes precedence.
2. Generated inferred screens may need redesign.
3. Agents should re-align with the latest design system.

---

# 14. Final Rule

Figma defines the **visual language**, but the **application flow defines the product behavior**.

If conflicts occur:

Product flow > Figma layout.

The agent must prioritize functional app flow over purely visual interpretations.

---

End of Document
