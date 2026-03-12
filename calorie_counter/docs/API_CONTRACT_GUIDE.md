# API Contract Guide

This file is a guide for the backend/API contract the Android app expects.

## Example analysis request shape

Potential fields:
- mealType
- image upload or image reference
- client timestamp
- optional device metadata if truly needed

## Example analysis response shape

- analysisId
- mealType
- overallConfidence
- warnings[]
- foods[]
  - name
  - quantity
  - unit
  - calories
  - protein_g
  - carbs_g
  - fat_g
  - confidence
- totals
  - calories
  - protein_g
  - carbs_g
  - fat_g

## Contract discipline

- numeric fields must be typed consistently
- nullability must be explicit
- units must be explicit
- partial results must be allowed and handled
- confidence fields should be optional only if product/backend intentionally allows omission
