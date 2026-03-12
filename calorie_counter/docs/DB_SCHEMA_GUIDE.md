# DB Schema Guide

## Suggested tables/entities

### meals
- id
- meal_type
- image_uri
- created_at
- saved_at
- total_calories
- total_protein_g
- total_carbs_g
- total_fat_g
- overall_confidence
- analysis_status
- user_edited

### meal_food_items
- id
- meal_id
- name
- quantity
- unit
- calories
- protein_g
- carbs_g
- fat_g
- confidence
- source_type (ai/corrected/manual if product wants it)

### analysis_attempts
- id
- meal_id or temp_capture_id
- started_at
- completed_at
- status
- error_type

### user_preferences
- id
- units preference
- onboarding completed
- other lightweight preferences

## Notes

Do not expose Room entities directly to UI.
Use mappers and domain models.
